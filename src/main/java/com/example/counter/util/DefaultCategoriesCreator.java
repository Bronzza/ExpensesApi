package com.example.counter.util;


import com.example.counter.entiry.Category;
import com.example.counter.entiry.SubCategory;
import com.example.counter.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;


public class DefaultCategoriesCreator {
    private final Map<String, List<String>> defaultCategoriesSubCategoriesNames = new LinkedHashMap<>();
    private final Map<String, List<String>> adminStuff = new LinkedHashMap<>();

    public DefaultCategoriesCreator() {
        List<String> health = Arrays.asList("Medication", "Sport");

        List<String> products = Arrays.asList("Clothes", "Gadget", "Presents");
        List<String> travel = Arrays.asList("Tickets", "Services", "Others");
        List<String> entertainment = Arrays.asList("Events", "Hobby", "Books");
        List<String> house = Arrays.asList("House rent", "Water", "Electricity", "Internet", "House products", "House repair");
        List<String> transport = Arrays.asList("Bike repair", "Bike petrol", "Car repair", "Car petrol",
                "Public", "Taxi");
        List<String> other = Arrays.asList("Other");
        List<String> kids = Arrays.asList("Health", "Education", "Clothes");
        List<String> charity = Arrays.asList("Donation");
        List<String> degradation = Arrays.asList("Alcohol", "Tobacco", "Girls", "Junk Food");

        defaultCategoriesSubCategoriesNames.put("House", house);
        defaultCategoriesSubCategoriesNames.put("Family", kids);
        defaultCategoriesSubCategoriesNames.put("Products", products);
        defaultCategoriesSubCategoriesNames.put("Transport", transport);
        defaultCategoriesSubCategoriesNames.put("Travel", travel);
        defaultCategoriesSubCategoriesNames.put("Entertainment", entertainment);
        defaultCategoriesSubCategoriesNames.put("Charity", charity);
        defaultCategoriesSubCategoriesNames.put("Other", other);

        adminStuff.put("Products", products);
        adminStuff.put("House", house);
        adminStuff.put("Transport", transport);
        adminStuff.put("Family", kids);
        adminStuff.put("Travel", travel);
        adminStuff.put("Entertainment", entertainment);
        adminStuff.put("Charity", charity);
        adminStuff.put("Degradation", degradation);
        adminStuff.put("Other", other);
    }


    private List<Category> createCategories(Map<String, List<String>> map) {
        return map.entrySet().stream()
                .map(entry -> Category
                        .builder()
                        .name(entry.getKey())
                        .subCategories(nameToSubCategory(entry.getValue()))
//                        .user()
                        .build()
                )
                .peek(category -> category
                        .setSubCategories(category.getSubCategories()
                                .stream()
                                .peek(subCategory -> subCategory.setCategory(category)).toList()))
                .toList();
    }

    public List<Category> createAdminCategories() {
        return createCategories(adminStuff);
    }

    public List<Category> createDefaultCategories() {
        return createCategories(defaultCategoriesSubCategoriesNames);
    }

    private static List<SubCategory> nameToSubCategory(List<String> list) {
        return list.stream()
                .map(name -> SubCategory.builder().name(name).build())
                .toList();
    }

    public List<Category> subCategories() {
        return null;
    }
}