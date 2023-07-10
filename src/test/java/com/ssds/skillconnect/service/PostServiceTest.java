package com.ssds.skillconnect.service;

import com.ssds.skillconnect.config.JwtService;
import com.ssds.skillconnect.repository.PostRepository;
import com.ssds.skillconnect.repository.ProjectRepository;
import com.ssds.skillconnect.repository.SkillRepository;
import com.ssds.skillconnect.repository.UserRepository;
import com.ssds.skillconnect.utils.helper.Mapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Autowired
    private PostService underTest;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private Mapper mapper;

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SkillRepository skillRepository;

    @BeforeEach
    void setUp() {
        underTest = new PostService(
                postRepository,
                userRepository,
                projectRepository,
                skillRepository,
                jwtService,
                mapper
        );
    }

    @Test
    void getAllPosts() {

        //given
        String authHeader = "";
        Integer departmentId = 1;
        Integer priority = 1;
        String skill = "";
        Integer sort = 0;
        Integer pageNumber = 0;
        Integer pageSize = 1;

        //when
        underTest.getAllPosts(
                authHeader,
                departmentId,
                priority,
                skill,
                sort,
                pageNumber,
                pageSize
        );
        //then
        verify(postRepository).findPostByDepartmentIdAndSkillAndUrgencyLevel(
                departmentId,
                priority,
                skill,
                PageRequest.of(pageNumber, pageSize)
        );
    }

    @Test
    void createPost() {

    }

    @Test
    void getPostById() {

    }

    @Test
    void getAllApplicantsToPost() {

    }

    @Test
    void approveTheUser() {

    }

    @Test
    void applyToPost() {

    }

    @Test
    void deleteClosedPostsOlderThan3Month() {

    }
}