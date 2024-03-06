package com.blog.blogApp.service;

import com.blog.blogApp.model.Post;
import com.blog.blogApp.payloads.PostDto;
import com.blog.blogApp.payloads.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);

    PostDto updatePost(PostDto postDto,Integer postId);

    void deletePost(Integer postId);

    PostResponse getAllPosts(Integer pageNo , Integer pageSize,String sortBy,String sortDirection);

    PostDto getPostById(Integer postId);

    PostResponse getPostByCategory(Integer categoryId,Integer pageNo , Integer pageSize);

    PostResponse getAllPostByUser(Integer userId,Integer pageNo , Integer pageSize);

     List<PostDto> searchPosts(String keyword);

}
