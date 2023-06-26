package com.ssds.skillconnect.service;

import com.ssds.skillconnect.config.JwtService;
import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.dao.Project;
import com.ssds.skillconnect.dao.User;
import com.ssds.skillconnect.model.PostCreateRequestModel;
import com.ssds.skillconnect.model.PostRowResponseModel;
import com.ssds.skillconnect.repository.PostRepository;
import com.ssds.skillconnect.repository.ProjectRepository;
import com.ssds.skillconnect.repository.UserRepository;
import com.ssds.skillconnect.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final JwtService jwtService;

    final Integer Descending = 0;
    final Integer Ascending = 1;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public List<Post> getAllPosts( String authorizationHeader ) {
        try {
            List<Post> allPosts = postRepository.findAll();

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);
            User currentUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            for(Post post : allPosts) {
                if(!Objects.equals(post.getUser().getUserId(), currentUser.getUserId())) {
                    // If the post is not created by the current user, then remove the list of applicants
                    //post.setListOfApplicants(null);
                }
            }

            return allPosts;
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public List<PostRowResponseModel> getAllPosts(
            String authorizationHeader,
            Integer departmentId,
            Integer priority,
            String skill,
            Integer sort
    ) {

        try {
            List<PostRowResponseModel> allPosts;
            if(departmentId != null || priority != null || skill != null || sort != null) {

                sort = (sort == null) ? Descending: sort;

                List<PostRowResponseModel> filteredPostList =
                        (sort.equals(Descending)) ?
                        postRepository.findByDepartmentIdAndPriorityDESCRowResponseModel(departmentId, priority)
                        :
                        postRepository.findByDepartmentIdAndPriorityASCRowResponseModel(departmentId, priority);

                if(skill != null) {
                    filteredPostList = filteredPostList.stream()
                            .filter(post -> post.getListOfSkillsRequired().stream().map(String::toLowerCase).toList().contains(skill.toLowerCase()))
                            .collect(Collectors.toList());
                }
                allPosts = filteredPostList;
            } else {
                allPosts = postRepository.findAllPostsRowResponseModel();
            }

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);
            Integer currentUserUserId = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND)).getUserId();

            List<Integer> savedPostIds = userRepository.findSavedPostsIdByUserId(currentUserUserId).orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            for(PostRowResponseModel post : allPosts) {
                post.setIsSaved(savedPostIds.contains(post.getPostId()));
            }
            
            return allPosts;

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }

    }

    public Post createPost(PostCreateRequestModel postCreateRequestModel, String authorizationHeader) {
        try {

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User currentUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            Project currentProject = projectRepository
                    .findById(postCreateRequestModel.getProjectId())
                    .orElseThrow(() -> new ApiRequestException("Project not found", HttpStatus.NOT_FOUND));



            if(!Objects.equals(currentProject.getProjectCreator().getUserId(), currentUser.getUserId())
                    && !currentProject.getUsersAssignedProjectList().contains(currentUser)) {
                // If the current user is not the creator of the project and is not assigned to the project, then throw an exception
                throw new ApiRequestException("You are not authorized to create a post for this project");
            }

            Post newPost = new Post();

            newPost.setPostTitle(postCreateRequestModel.getPostTitle());
            newPost.setPostDescription(postCreateRequestModel.getPostDescription());
            newPost.setUrgencyLevel(postCreateRequestModel.getUrgencyLevel());
            newPost.setCreatedOn(postCreateRequestModel.getCreatedOn());
            newPost.setIsOpen(postCreateRequestModel.getIsOpen());
            newPost.setTotalApplicants(postCreateRequestModel.getTotalApplicants());
            newPost.setListOfSkillsRequired(postCreateRequestModel.getListOfSkillsRequired());

            newPost.setUser(currentUser);
            newPost.setProject(currentProject);

            return postRepository.save(newPost);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Post getPostById(Integer postId, String authorizationHeader) {

        return postRepository.findById(postId)
                .orElseThrow(() -> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));
    }


    public List<User> getAllApplicantsToPost(Integer postId) {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

            return post.getListOfApplicants();

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Post approveTheUser(Integer postId, Integer userId, String authorizationHeader) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);
            Post currentPost = postRepository.findById(postId)
                    .orElseThrow(() -> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

            User currentUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            User postCreatedUser = currentPost.getUser();
            if (!Objects.equals(userEmail, postCreatedUser.getEmail())) {
                throw new ApiRequestException("User has no authority to approve", HttpStatus.UNAUTHORIZED);
            }

            User applyingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            //Mark the post as close
            currentPost.setIsOpen(false);
            postRepository.save(currentPost);

            //Add current user to project
            Project project = currentPost.getProject();
            List<User> projectUsers = project.getUsersAssignedProjectList();
            projectUsers.add(applyingUser);
            project.setUsersAssignedProjectList(projectUsers);
            projectRepository.save(project);

            return currentPost;
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Post applyToPost(Integer postId, String authorizationHeader) {
        try{

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            Post currentPost = postRepository.findById(postId)
                    .orElseThrow(() -> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

            User currentUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            List<User> applicants = currentPost.getListOfApplicants();
            applicants.add(currentUser);
            currentPost.setListOfApplicants(applicants);
            currentPost.setTotalApplicants(currentPost.getTotalApplicants() + 1);
            return postRepository.save(currentPost);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }


    public void deleteClosedPostsOlderThan3Month() {
        try {
            postRepository.deleteAllByIsOpenAndCreatedOnBefore(false, LocalDate.now().minusMonths(3));
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }


}
