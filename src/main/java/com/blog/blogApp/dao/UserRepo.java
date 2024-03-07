package com.blog.blogApp.dao;

import com.blog.blogApp.model.User;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

  Optional<User> findByusername(String userName);
}
