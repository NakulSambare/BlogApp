package com.blog.blogApp.service;

import com.blog.blogApp.dao.CategoryRepo;
import com.blog.blogApp.exceptions.ResourceNotFoundException;
import com.blog.blogApp.model.Category;
import com.blog.blogApp.payloads.CategoryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CategoryserviceImpl implements CategoryService{

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
       Category c = this.modelMapper.map(categoryDto, Category.class);
        return this.modelMapper.map(categoryRepo.save(c), CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","Category Id",categoryId)
        );

        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());
        Category updatedCategory =categoryRepo.save(cat);

        return this.modelMapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","Category Id",categoryId)
        );

        categoryRepo.delete(cat);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {

        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","Category Id",categoryId)
        );
        return this.modelMapper.map(cat,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = this.categoryRepo.findAll();
       return categories.stream()
                .map((cat)-> this.modelMapper.map(cat,CategoryDto.class))
                .collect(Collectors.toList());

    }
}
