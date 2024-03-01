package com.blog.blogApp.service;

import com.blog.blogApp.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {

    //create
    public CategoryDto createCategory(CategoryDto categoryDto);
    //update
    public CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);

    //delete
    public void deleteCategory(Integer categoryId);
    //get

    public CategoryDto getCategory(Integer categoryId);
    //getAll
    public List<CategoryDto> getAllCategories();

}
