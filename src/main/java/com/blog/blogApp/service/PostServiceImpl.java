package com.blog.blogApp.service;

import com.blog.blogApp.dao.CategoryRepo;
import com.blog.blogApp.dao.PostRepo;
import com.blog.blogApp.dao.UserRepo;
import com.blog.blogApp.exceptions.ResourceNotFoundException;
import com.blog.blogApp.model.Category;
import com.blog.blogApp.model.Post;
import com.blog.blogApp.model.User;
import com.blog.blogApp.payloads.PostDto;
import com.blog.blogApp.payloads.PostResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepo postRepo;

@Autowired
private ModelMapper modelMapper;

@Autowired
private UserRepo userRepo;

@Autowired
private CategoryRepo categoryRepo;
    @Override
    public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
       User user = userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId)
        );

        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","Category Id",categoryId)
        );

        Post post = modelMapper.map(postDto, Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(cat);
        Post newPost= postRepo.save(post);

        return modelMapper.map(newPost,PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post p = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post id",postId));
         p.setTitle(postDto.getTitle());
         p.setContent(postDto.getContent());
         p.setImageName(postDto.getImageName());
         p.setCategory(this.modelMapper.map( postDto.getCategory(), Category.class));

       return   modelMapper.map( postRepo.save(p),PostDto.class);

    }

    @Override
    public void deletePost(Integer postId) {
        Post p = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post id",postId));
         postRepo.delete(p);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNo , Integer pageSize,String sortBy,String sortDirection) {

  Sort sort = null;
  if(sortDirection.equalsIgnoreCase("asc")){
      sort = Sort.by(sortBy).ascending();
  }else{
      sort = Sort.by(sortBy).descending();
  }

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Post> pagePosts = this.postRepo.findAll(pageable);
        List<Post> posts = pagePosts.getContent();
        List<PostDto> postDtoList = posts.stream()
                                            .map(p -> this.modelMapper.map(p,PostDto.class))
                                        .collect(Collectors.toList());
        PostResponse postResponse =  new PostResponse();
        postResponse.setContent(postDtoList);
        postResponse.setPageNumber(pageNo);
        postResponse.setPageSize(pageSize);
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post p = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post id",postId));
        return this.modelMapper.map(p,PostDto.class);
    }

    @Override
    public PostResponse getPostByCategory(Integer categoryId,Integer pageNo , Integer pageSize) {
        Category cat = this.categoryRepo.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","Category Id",categoryId)
        );

        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Post> pagePosts = this.postRepo.findByCategory(cat,pageable);
      List<Post> posts = pagePosts.getContent() ;
    List<PostDto> postDtoList = posts.stream()
              .map(post -> modelMapper.map(post,PostDto.class) )
              .collect(Collectors.toList());
        PostResponse postResponse =  new PostResponse();
        postResponse.setContent(postDtoList);
        postResponse.setPageNumber(pageNo);
        postResponse.setPageSize(pageSize);
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public PostResponse getAllPostByUser(Integer userId,Integer pageNo , Integer pageSize) {
        User user = userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId)
        );
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Post> pagePosts = this.postRepo.findByUser(user,pageable);
        List<Post> posts= pagePosts.getContent();


        List<PostDto> postDtoList =  posts.stream()
                .map(post -> modelMapper.map(post,PostDto.class) )
                .collect(Collectors.toList());
        PostResponse postResponse =  new PostResponse();
        postResponse.setContent(postDtoList);
        postResponse.setPageNumber(pageNo);
        postResponse.setPageSize(pageSize);
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setLastPage(pagePosts.isLast());

        return  postResponse;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        List<Post> postList =  this.postRepo.findByTitleContaining(keyword);
        List<PostDto> postDtoList = postList.stream()
                                            .map(post -> this.modelMapper.map(post, PostDto.class))
                                            .collect(Collectors.toList());
                return  postDtoList;
    }
}
