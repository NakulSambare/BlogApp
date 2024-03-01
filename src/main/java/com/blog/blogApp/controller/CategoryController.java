package com.blog.blogApp.controller;

import com.blog.blogApp.payloads.ApiResponse;
import com.blog.blogApp.payloads.CategoryDto;
import com.blog.blogApp.payloads.UserDto;
import com.blog.blogApp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/saveCategory")
    public ResponseEntity<CategoryDto> saveCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 =   categoryService.createCategory(categoryDto);
        return  new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    @PutMapping("/updateCategory/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Integer categoryId){
        CategoryDto categoryDto1 =  categoryService.updateCategory(categoryDto,categoryId);

        return  ResponseEntity.ok(categoryDto1);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public  ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){

        categoryService.deleteCategory(categoryId);

        return new ResponseEntity(new ApiResponse("Category deleted successfully",true), HttpStatus.OK);

    }

    @GetMapping("/getCategory/{categoryId}")
    public ResponseEntity<CategoryDto> getUser(@PathVariable Integer categoryId){
        return  ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
