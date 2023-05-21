package com.inn.restaurant.restImpl;

import com.inn.restaurant.constants.RestaurantConstant;
import com.inn.restaurant.model.Category;
import com.inn.restaurant.rest.CategoryRest;
import com.inn.restaurant.service.CategoryService;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService categoryService;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            return categoryService.addNewCategory(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {

        try{
            return categoryService.getAllCategory(filterValue);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstant.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
