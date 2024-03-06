package com.blog.blogApp.dao;

import com.blog.blogApp.model.Category;
import com.blog.blogApp.model.Post;
import com.blog.blogApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Integer> {

    Page<Post> findByUser(User u, Pageable pageable);
    Page<Post> findByCategory(Category c, Pageable pageable);

    List<Post> findByTitleContaining(String title);


}
