package com.blog.blogApp.controller;

import com.blog.blogApp.exceptions.ApiException;
import com.blog.blogApp.payloads.JwtAuthRequest;
import com.blog.blogApp.payloads.JwtAuthResponse;
import com.blog.blogApp.payloads.UserDto;
import com.blog.blogApp.security.JwtTokenHelper;
import com.blog.blogApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {

        this.authenticate(request.getUsername(),request.getPassword());
        UserDetails userDetails =  this.userDetailsService.loadUserByUsername(request.getUsername());
     String generatedToken =    this.jwtTokenHelper.generateToken(userDetails);
JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
jwtAuthResponse.setToken(generatedToken);

return new ResponseEntity<JwtAuthResponse>(jwtAuthResponse, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try{
            this.authenticationManager.authenticate(authenticationToken);
        }
        catch (DisabledException e){
            throw new Exception("User is disabled");
        }
        catch (BadCredentialsException e){
            throw new ApiException("Invalid user details");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){
        UserDto userDto1 = this.userService.registerNeUser(userDto);

        return new ResponseEntity<UserDto>(userDto1,HttpStatus.CREATED);


    }
}
