package com.blog.blogApp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int userId;
    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String about;



}
