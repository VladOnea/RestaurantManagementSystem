package com.inn.restaurant.JWT;

import com.inn.restaurant.dao.UserDao;
import com.inn.restaurant.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private com.inn.restaurant.model.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Inside loadUserByUsername {}",username);
        userDetail = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail))
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found!");

    }

    public com.inn.restaurant.model.User getUserDetail(){

        //com.inn.restaurant.model.User user= userDetail;
        //user.setPassword(null); user password is not exposed in this case
        return userDetail; //pass all the data
    }
}
