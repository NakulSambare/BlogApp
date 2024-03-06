package com.blog.blogApp.payloads;

import com.blog.blogApp.model.Category;
import com.blog.blogApp.model.Comment;
import com.blog.blogApp.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
public class PostDto {

    private Integer postId;


    private String title;

    private String content;


    private String imageName;


    private Date addedDate;

    private  CategoryDto category;

    private UserDto user;

    private Set<CommentDto> comments = new HashSet<>();


}
