package com.blog.blogApp.controller;

import com.blog.blogApp.payloads.ApiResponse;
import com.blog.blogApp.payloads.UserDto;
import com.blog.blogApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto user){
        UserDto userDto =   userService.createUser(user);
        return  new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user,@PathVariable Integer userId){
      UserDto userDto =  userService.updateUser(user,userId);

      return  ResponseEntity.ok(userDto);
    }

    //Only admin can call this api
    //@PreAuthorize("hasRole('ADMIN')")
   // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteUser/{userId}")
    public  ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){

        userService.deleteUser(userId);

        return new ResponseEntity(new ApiResponse("User deleted successfully",true), HttpStatus.OK);

    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId){
        return  ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
