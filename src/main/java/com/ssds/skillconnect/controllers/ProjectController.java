package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.dao.Task;
import com.ssds.skillconnect.model.ProjectRowResponseModel;
import com.ssds.skillconnect.model.TaskRequestModel;
import com.ssds.skillconnect.model.UserDetailResponseModel;
import com.ssds.skillconnect.service.ProjectService;
import com.ssds.skillconnect.dao.Project;
import com.ssds.skillconnect.model.ProjectModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@RestController
@RequestMapping("api/v1/project")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private static final Logger logger = Logger.getLogger(ProjectController.class.getName());

    @GetMapping("/all")
    private List<ProjectRowResponseModel> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/user")
    private List<ProjectRowResponseModel> getProjectByUser(
            @RequestHeader(value="Authorization") String authorizationHeader) {
        return projectService.getProjectByUser(authorizationHeader);
    }

    @GetMapping("/all-open")
    private List<Project> getAllOpenProjects() {
        return projectService.getAllOpenProjects();
    }

    @PostMapping("/create")
    private Project createProject(
            @RequestBody ProjectModel projectModel,
            @RequestHeader(value="Authorization") String authorizationHeader
    ) {
        logger.log(INFO, projectModel.toString());
        return projectService.createProject(projectModel, authorizationHeader);
    }

    @GetMapping("/{projectId}")
    private Project getProjectById(@PathVariable Integer projectId) {
        return projectService.getProjectById(projectId);
    }

    @PostMapping("/{projectId}/add-user/{userId}")
    private Project addUserToProject(
            @PathVariable Integer projectId,
            @PathVariable Integer userId,
            @RequestHeader(value="Authorization") String authorizationHeader) {
        return projectService.addUserToProject(
                projectId,
                userId,
                authorizationHeader
        );
    }

    @DeleteMapping("/{projectId}/remove-user/{userId}")
    private Project removeUserFromProject(
            @PathVariable Integer projectId,
            @PathVariable Integer userId) {
        return projectService.removeUserFromProject(projectId, userId);
    }

    @GetMapping("/all-users")
    private List<UserDetailResponseModel> getAllUsersInProject(
            @RequestHeader(value="Authorization") String authorizationHeader,
            @RequestParam Integer projectId
    ) {
        return projectService.getAllUsersInProject(authorizationHeader, projectId);
    }

}
