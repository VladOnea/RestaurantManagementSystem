package com.inn.restaurant;

import com.inn.restaurant.dao.CategoryDao;
//import com.inn.restaurant.dao.ProductDao;
import com.inn.restaurant.model.Category;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvCategoryImporter {


    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    public CsvCategoryImporter(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @PostConstruct
    public void importCsvOnStartup() throws IOException, CsvValidationException {
        String csvFilePath = "C://Users//Vlad//Desktop//Facultate an 3,sem 2//SD (Software Design)//Project1//product.csv";
        Reader reader = new FileReader(csvFilePath);

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();

        List<CsvCategory> csvCategories = new ArrayList<>();

        String[] line;
        while ((line = csvReader.readNext()) != null) {
            CsvCategory csvCategory = new CsvCategory();
            csvCategory.setName(line[0]);
            csvCategories.add(csvCategory);
        }

        csvReader.close();

        List<Category> category = csvCategories.stream().map(CategoryMapper::toEntity).collect(Collectors.toList());
        categoryDao.saveAll(category);
    }



}