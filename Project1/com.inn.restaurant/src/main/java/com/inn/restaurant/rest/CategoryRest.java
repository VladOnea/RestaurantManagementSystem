package com.inn.restaurant.rest;

import com.inn.restaurant.model.Category;
import com.inn.restaurant.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path="/category")
public interface CategoryRest{

    @PostMapping(path="/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true)
                                          Map<String, String> requestMap);
    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestBody(required = false)
                                                  String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestMap);
}
