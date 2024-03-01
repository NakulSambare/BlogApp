package com.blog.blogApp.service;

import com.blog.blogApp.dao.UserRepo;
import com.blog.blogApp.exceptions.ResourceNotFoundException;
import com.blog.blogApp.model.User;
import com.blog.blogApp.payloads.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public UserDto createUser(UserDto user) {

//        User u = User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .email(user.getEmail())
//                .about(user.getAbout())
//                .build();
        User u = userDtoToUser(user);
        u=userRepo.save(u);

        return userToUserDto(u);
    }

    @Override
    public UserDto updateUser(UserDto user, Integer userId) {
        User u = userRepo.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId)
        );
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        u.setAbout(user.getAbout());
        User updatedUser = userRepo.save(u);

        return userToUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User u = userRepo.findById(userId).orElseThrow(() ->
             new ResourceNotFoundException("User", "id", userId)
        );
        return userToUserDto(u);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users= userRepo.findAll();
     List<UserDto> userDtoList =    users.stream()
                                    .map(u -> userToUserDto(u))
             .collect(Collectors.toList());

        return userDtoList;
    }

    @Override
    public void deleteUser(Integer userId) {
        User u = userRepo.findById(userId).orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", userId));
            userRepo.delete(u);
    }

    public User userDtoToUser(UserDto userDto){
         return  this.modelMapper.map(userDto,User.class );

    }

    public UserDto userToUserDto(User user){
            return this.modelMapper.map(user,UserDto.class);
    }
}
