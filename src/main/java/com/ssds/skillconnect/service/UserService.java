package com.ssds.skillconnect.service;

import com.ssds.skillconnect.config.JwtService;
import com.ssds.skillconnect.dao.Department;
import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.dao.User;
import com.ssds.skillconnect.model.UserDetailResponseModel;
import com.ssds.skillconnect.model.UserModel;
import com.ssds.skillconnect.model.UserSearchResponseModel;
import com.ssds.skillconnect.repository.DepartmentRepository;
import com.ssds.skillconnect.repository.PostRepository;
import com.ssds.skillconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDetailResponseModel getUser(String authorizationHeader) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            return userRepository.findUserDetailResponseModelByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserDetailResponseModel updateUserById(String authorizationHeader, UserModel userModel) {

        String jwtToken = authorizationHeader.substring(7);
        String userEmail = jwtService.extractUserEmail(jwtToken);

        User userToUpdate = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userToUpdate.setName(userModel.getName());
        userToUpdate.setEmail(userModel.getEmail());
        //userToUpdate.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userToUpdate.setExperience(userModel.getExperience());
        userToUpdate.setListOfSkills(userModel.getListOfSkills());

        Department department = departmentRepository.findById(userModel.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        userToUpdate.setDepartment(department);

        try {
            userRepository.save(userToUpdate);

            return userRepository.findUserDetailResponseModelByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UserDetailResponseModel> getAllUsersByDepartmentId(Integer departmentId) {
        try {
            return userRepository.findUserDetailResponseModelByDepartmentId(departmentId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Integer> getAllSavedPostsId(String authorizationHeader) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return userRepository.findSavedPostsIdByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Saved posts not found"));
//            return user.getSavedPostsId();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Integer> savePost(String authorizationHeader, Integer postId) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            List<Post> userSavedPosts = user.getSavedPosts();
            userSavedPosts.add(post);
            user.setSavedPosts(userSavedPosts);

            userRepository.save(user);

            return userRepository.findSavedPostsIdByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Saved posts not found"));

//            List<Integer> userSavedPostsIds = user.getSavedPostsId();
//            userSavedPostsIds.add(postId);
//            user.setSavedPostsId(userSavedPostsIds);
//
//            userRepository.save(user);
//
//            return userSavedPostsIds;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Integer> unSavePost(String authorizationHeader, Integer postId) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            List<Post> userSavedPosts = user.getSavedPosts();
            userSavedPosts.remove(post);
            user.setSavedPosts(userSavedPosts);

            userRepository.save(user);

            return userRepository.findSavedPostsIdByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Saved posts not found"));

//            List<Integer> userSavedPostsIds = user.getSavedPostsId();
//            userSavedPostsIds.remove(postId);
//            user.setSavedPostsId(userSavedPostsIds);
//
//            userRepository.save(user);
//
//            return userSavedPostsIds;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Post> getAllSavedPosts(String authorizationHeader) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return userRepository.findSavedPostsByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Saved posts not found"));

//            List<Integer> userSavedPostsIds = userRepository.getSavedPostsId();
//            return postRepository.findAllById(userSavedPostsIds);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserDetailResponseModel getUserById(Integer userId) {
        try {

            return userRepository.findUserDetailResponseModelByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // For security reasons, we don't want to send the password and savedPostsId to the client
            // if the user is fetching someone else's profile (i.e. not his own)
            //user.setSavedPostsId(null);
//            user.setPassword(null);
//            return user;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UserSearchResponseModel> getAllUsersLike(Integer projectId, String name) {
        try {
            if(Objects.equals(name, "")) name = null;
            return userRepository.findByNameContainingAndNotInProject(projectId, name)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
