package com.blog.blogApp.service;

import com.blog.blogApp.payloads.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto,Integer postId,Integer userId);
    void deleteComment(Integer commmentId);

}
