package com.blog.blogApp.service;

import com.blog.blogApp.dao.CommentRepo;
import com.blog.blogApp.dao.PostRepo;
import com.blog.blogApp.dao.UserRepo;
import com.blog.blogApp.exceptions.ResourceNotFoundException;
import com.blog.blogApp.model.Comment;
import com.blog.blogApp.model.Post;
import com.blog.blogApp.model.User;
import com.blog.blogApp.payloads.CommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements  CommentService{

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    PostRepo postRepo;

    @Autowired
    UserRepo userRepo;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId,Integer userId) {
        Post p = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Post id",postId));
        User u = userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId)
        );
        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(p);
        comment.setUser(u);
       Comment savedComment = this.commentRepo.save(comment);

        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commmentId) {
           Comment c = this.commentRepo.findById(commmentId).orElseThrow(()->new ResourceNotFoundException("Comment","Comment id",commmentId));
    this.commentRepo.delete(c);
    }
}
