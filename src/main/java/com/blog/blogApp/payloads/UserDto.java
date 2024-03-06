package com.blog.blogApp.payloads;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
public class UserDto {
    private int userId;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 4,message = "Username must be greater than 4 characters")
    private String username;

    @Email(message = "Email address is not valid")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 5,message = "Password should be of minimum 5 characters")
    private String password;

    @NotEmpty(message = "About should not be empty")
    private String about;

    private Set<CommentDto> comments = new HashSet<>();

}
