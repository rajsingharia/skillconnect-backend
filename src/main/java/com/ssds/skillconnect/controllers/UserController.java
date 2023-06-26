package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.model.UserDetailResponseModel;
import com.ssds.skillconnect.model.UserModel;
import com.ssds.skillconnect.model.UserSearchResponseModel;
import com.ssds.skillconnect.service.UserService;
import com.ssds.skillconnect.dao.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping("/get")
    private UserDetailResponseModel getUserById(
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return userService.getUser(authorizationHeader);
    }

    @GetMapping("/get/{userId}")
    private UserDetailResponseModel getUserById(
            @PathVariable Integer userId
    ) {
        return userService.getUserById(userId);
    }

    @GetMapping("/search/{projectId}/{name}")
    private List<UserSearchResponseModel> getAllUsersLike(
            @PathVariable Integer projectId,
            @PathVariable String name
    ) {
        return userService.getAllUsersLike(projectId, name);
    }

    @PutMapping("/update")
    private UserDetailResponseModel updateUserById(
            @RequestHeader(value="Authorization") String authorizationHeader,
            @RequestBody UserModel userModel
    ) {
        return userService.updateUserById(authorizationHeader, userModel);
    }

    @GetMapping("/get-all-saved-posts")
    private List<Integer> getAllSavedPosts(
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return userService.getAllSavedPostsId(authorizationHeader);
    }

    @GetMapping("/get-all-saved-posts-details")
    private List<Post> getAllSavedPostsDetails(
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return userService.getAllSavedPosts(authorizationHeader);
    }

    @GetMapping("/save-post/{postId}")
    private List<Integer> savePost(
            @RequestHeader(value="Authorization") String authorizationHeader,
            @PathVariable Integer postId
    ) {
        logger.log(Level.INFO, "Saving post" + postId);
        return userService.savePost(authorizationHeader, postId);
    }

    @GetMapping("/un-save-post/{postId}")
    private List<Integer> unSavePost(
            @RequestHeader(value="Authorization") String authorizationHeader,
            @PathVariable Integer postId
    ) {
        return userService.unSavePost(authorizationHeader, postId);
    }

    @GetMapping("get-all-users/{departmentId}")
    private List<UserDetailResponseModel> getAllUsersByDepartmentId(
            @PathVariable Integer departmentId
    ) {
        logger.log(Level.INFO, "Getting all users by department id");
        return userService.getAllUsersByDepartmentId(departmentId);
    }

}
