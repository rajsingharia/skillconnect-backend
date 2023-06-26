package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.model.PostRowResponseModel;
import com.ssds.skillconnect.service.PostService;
import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.dao.User;
import com.ssds.skillconnect.model.PostCreateRequestModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/all")
    @ResponseBody
    public List<PostRowResponseModel> getAllPosts(
            @RequestHeader(value="Authorization") String authorizationHeader,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) Integer sort
    ) {
        return postService.getAllPosts(authorizationHeader, departmentId, priority, skill, sort);
    }



    @PostMapping("/create")
    @ResponseBody
    public Post createPost(
            @RequestBody PostCreateRequestModel postCreateRequestModel,
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return postService.createPost(postCreateRequestModel, authorizationHeader);
    }

    @GetMapping("/{postId}")
    @ResponseBody
    public Post getPostById(
            @PathVariable Integer postId,
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return postService.getPostById(postId, authorizationHeader);
    }

    @GetMapping("{postId}/applicants")
    public List<User> getAllApplicantsToPost(@PathVariable Integer postId) {
        return postService.getAllApplicantsToPost(postId);
    }

    @PostMapping("{postId}/apply")
    public Post applyToPost(
            @PathVariable Integer postId,
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return postService.applyToPost(postId, authorizationHeader);
    }

    @PostMapping("{postId}/approve/{userId}")
    public Post approveTheUser(
            @PathVariable Integer postId,
            @PathVariable Integer userId,
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        return postService.approveTheUser(postId, userId, authorizationHeader);
    }

}
