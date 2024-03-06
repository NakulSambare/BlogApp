package com.blog.blogApp.controller;

import com.blog.blogApp.config.AppConstants;
import com.blog.blogApp.payloads.ApiResponse;
import com.blog.blogApp.payloads.PostDto;
import com.blog.blogApp.payloads.PostResponse;
import com.blog.blogApp.payloads.UserDto;
import com.blog.blogApp.service.FileService;
import com.blog.blogApp.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;


    @Value("${project.image}")
    private String path;

    @PostMapping("/userId/{userId}/category/{categoryId}/postPages")
    public ResponseEntity<PostDto> createPost(@RequestBody  PostDto postDto, @PathVariable Integer userId,@PathVariable Integer categoryId){
        PostDto postDto1= postService.createPost(postDto,userId,categoryId);
       return new ResponseEntity<>(postDto1, HttpStatus.CREATED);
    }

    @GetMapping("/getAllPostsByCategory/{categoryId}/postPages")
    public ResponseEntity<PostResponse> getAllPostsByCategory(@PathVariable Integer categoryId,@RequestParam(value = "pageNo",defaultValue = AppConstants.PAGE_NO,required = false) Integer pageNo,
                                                               @RequestParam(value = "PageSize",defaultValue =AppConstants.PAGE_SIZE,required = false) Integer pageSize){
        PostResponse postResponse = postService.getPostByCategory(categoryId,pageNo,pageSize);
       return  ResponseEntity.ok(postResponse);
    }

    @GetMapping("/getAllPostsByUser/{userId}/postPages")
    public ResponseEntity<PostResponse> getAllPostsByUser(@PathVariable Integer userId,@RequestParam(value = "pageNo",defaultValue = AppConstants.PAGE_NO,required = false) Integer pageNo,
                                                           @RequestParam(value = "PageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize){
        PostResponse posts = postService.getAllPostByUser(userId,pageNo,pageSize);
        return  ResponseEntity.ok(posts);
    }

    @GetMapping("/getAllPost/postPages")
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "pageNo",defaultValue = AppConstants.PAGE_NO,required = false) Integer pageNo,
                                                    @RequestParam(value = "PageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                    @RequestParam(value = "sortBy",defaultValue =AppConstants.SORT_BY,required = false) String sortBy,
                                                    @RequestParam(value = "sortDirection",defaultValue = AppConstants.SORT_DIR,required = false) String sortDirection

    ){
        PostResponse postResponse= postService.getAllPosts(pageNo,pageSize,sortBy,sortDirection);
        return  ResponseEntity.ok(postResponse);
    }
    @GetMapping("/getPostById/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
        PostDto posts = postService.getPostById(postId);
        return  ResponseEntity.ok(posts);
    }

    @DeleteMapping("/deletePost/{postId}")
    public  ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){

        postService.deletePost(postId);

        return new ResponseEntity(new ApiResponse("Post deleted successfully",true), HttpStatus.OK);

    }

    @PutMapping("/updatePost/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId){
        PostDto updatedPost = postService.updatePost(postDto,postId);
        return  ResponseEntity.ok(updatedPost);
    }

    //search
    @GetMapping("/searchPost/{keyword}")
    public ResponseEntity<List<PostDto>> searchPosts(@PathVariable String keyword){
        List<PostDto> postDtoList = postService.searchPosts(keyword);
        return new ResponseEntity<List<PostDto>>(postDtoList,HttpStatus.OK);
    }

    @PostMapping("/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadImage(@RequestParam("image")MultipartFile file,@PathVariable Integer postId) throws IOException {
        PostDto postDto =   this.postService.getPostById(postId);

        String fileName =  this.fileService.uploadImage(path,file);
      postDto.setImageName(fileName);
    PostDto updatedPost =   this.postService.updatePost(postDto,postId);

      return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
    }

    @GetMapping(value = "/iamge/getImage/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName,
                              HttpServletResponse response
                              )throws  IOException{
        InputStream resource = this.fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
