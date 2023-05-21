package com.inn.restaurant.serviceImpl;

import com.google.common.base.Strings;
import com.inn.restaurant.JWT.CustomerUsersDetailsService;
import com.inn.restaurant.JWT.JwtFilter;
import com.inn.restaurant.JWT.JwtUtils;
import com.inn.restaurant.constants.RestaurantConstant;
import com.inn.restaurant.dao.UserDao;
import com.inn.restaurant.model.User;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.EmailUtils;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

// in this class is the actual business logic
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return RestaurantUtils.getResponseEntity("Successfully registered!", HttpStatus.OK);
                } else {
                    return RestaurantUtils.getResponseEntity("Email already exists!", HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestaurantUtils.getResponseEntity(RestaurantConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSignUpMap(Map<String,String> requestMap){  //all fields sended
        if(requestMap.containsKey("name") && requestMap.containsKey("telephoneNumber")
                && requestMap.containsKey("email")  && requestMap.containsKey("password")){
            return true;
        }
        else {
            return false;
        }
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user= new User();
        user.setName(requestMap.get("name"));
        user.setTelephoneNumber(requestMap.get("telephoneNumber"));
        user.setEmail(requestMap.get("email"));
        //user.setPassword(requestMap.get("password"));
        String password = requestMap.get("password");
        String hashedPassword = new BCryptPasswordEncoder().encode(password);
        user.setPassword(hashedPassword);
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtils.generateToken(
                            customerUsersDetailsService.getUserDetail().getEmail(), customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval!" + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }

        } catch (Exception e) {
            log.error("{}", e);
        }

        return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials!" + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);
            }else
            {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                    userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                    return RestaurantUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                }
                else{
                    return RestaurantUtils.getResponseEntity("User id doesn't exist!", HttpStatus.OK);
                }
            } else {
                return RestaurantUtils.getResponseEntity(RestaurantConstant.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);


    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account approved!","USER:  "+user+"\n is approved by \nADMIN" + jwtFilter.getCurrentUser(),allAdmin);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account disabled!","USER:  "+user+"\n is disabled by \nADMIN" + jwtFilter.getCurrentUser(),allAdmin);
        }
    }
    @Override
    public ResponseEntity<String> checkToken() {
        return RestaurantUtils.getResponseEntity("true", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)) {
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);//just the password will be updated
                    return RestaurantUtils.getResponseEntity("Password uppdatated Succ", HttpStatus.OK);
                }
                return RestaurantUtils.getResponseEntity("Incorrect old passwod", HttpStatus.INTERNAL_SERVER_ERROR);

            }
            return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(),"Credentials from Restaurant",user.getPassword());
            return RestaurantUtils.getResponseEntity("Check your mail for credentials", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
