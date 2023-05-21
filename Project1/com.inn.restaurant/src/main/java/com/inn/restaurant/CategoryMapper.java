package com.inn.restaurant;

//import com.inn.restaurant.model.Product;

import com.inn.restaurant.model.Category;

public class CategoryMapper {
    public static Category toEntity(CsvCategory csvCategory) {
        Category category = new Category();
        category.setName(csvCategory.getName());

        return category;
    }
}
