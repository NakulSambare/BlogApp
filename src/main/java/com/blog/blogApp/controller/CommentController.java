package com.blog.blogApp.controller;

import com.blog.blogApp.model.Comment;
import com.blog.blogApp.model.User;
import com.blog.blogApp.payloads.ApiResponse;
import com.blog.blogApp.payloads.CommentDto;
import com.blog.blogApp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/post/{postId}/user/{userId}")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto c, @PathVariable Integer postId, @PathVariable Integer userId){

        CommentDto commentDto = commentService.createComment(c,postId,userId);

        return new ResponseEntity<CommentDto>(commentDto, HttpStatus.CREATED);

    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){

         commentService.deleteComment(commentId);

        return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted successfully!!",true), HttpStatus.OK);

    }

}
