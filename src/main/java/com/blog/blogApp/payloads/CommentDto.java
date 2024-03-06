package com.blog.blogApp.payloads;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDto {

    private Integer commentId;

    private String content;


}
