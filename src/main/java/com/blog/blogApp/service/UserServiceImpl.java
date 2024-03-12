package com.blog.blogApp.service;

import com.blog.blogApp.config.AppConstants;
import com.blog.blogApp.dao.RoleRepo;
import com.blog.blogApp.dao.UserRepo;
import com.blog.blogApp.exceptions.ResourceNotFoundException;
import com.blog.blogApp.model.Role;
import com.blog.blogApp.model.User;
import com.blog.blogApp.payloads.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepo userRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;
    @Override
    public UserDto registerNeUser(UserDto userDto) {
       User user = this.modelMapper.map(userDto,User.class);
   user.setPassword(this.passwordEncoder.encode(user.getPassword()));

   //Add roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).orElseThrow(() ->
                new ResourceNotFoundException("Role", "id", AppConstants.NORMAL_USER)
        );

        user.getRoles().add(role);
       User newUser = this.userRepo.save(user);

        return this.modelMapper.map(newUser,UserDto.class);
    }

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
