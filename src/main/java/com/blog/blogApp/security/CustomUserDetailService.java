package com.blog.blogApp.security;

import com.blog.blogApp.dao.UserRepo;
import com.blog.blogApp.exceptions.ResourceNotFoundException;
import com.blog.blogApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Loading user from database from username
     User user =   this.userRepo.findByusername(username).orElseThrow(()-> new ResourceNotFoundException("User","username= "+username,0));

        return user;
    }
}
