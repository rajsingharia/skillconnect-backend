package com.ssds.skillconnect.service;

import com.ssds.skillconnect.config.JwtService;
import com.ssds.skillconnect.dao.Department;
import com.ssds.skillconnect.dao.Project;
import com.ssds.skillconnect.dao.User;
import com.ssds.skillconnect.model.ProjectModel;
import com.ssds.skillconnect.repository.DepartmentRepository;
import com.ssds.skillconnect.repository.ProjectRepository;
import com.ssds.skillconnect.repository.TaskRepository;
import com.ssds.skillconnect.repository.UserRepository;
import com.ssds.skillconnect.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskRepository taskRepository;
    private final JwtService jwtService;

    public List<Project> getAllProjects() {
        try {
            return projectRepo.findAll();
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Project createProject(ProjectModel projectModel, String authorizationHeader) {
        try {
            Project project = new Project();

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User projectCreator = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            project.setProjectCreator(projectCreator);

            project.setProjectName(projectModel.getProjectName());
            project.setProjectDetails(projectModel.getProjectDetails());
            project.setStartDate(projectModel.getStartDate());
            project.setEndDate(projectModel.getEndDate());
            project.setIsFinished(projectModel.getIsFinished());

            Department userDepartment = departmentRepository.findById(projectModel.getDepartmentId())
                    .orElseThrow(() -> new ApiRequestException("Department not found"));

            project.setDepartment(userDepartment);

            List<User> usersAssignedProjectList = userRepository
                    .findAllById(projectModel.getUserIdsAssignedProjectList());

            project.setUsersAssignedProjectList(usersAssignedProjectList);

            return projectRepo.save(project);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Project getProjectById(Integer projectId) {
        try {
            return projectRepo.findById(projectId)
                    .orElseThrow(() -> new ApiRequestException("Project not found"));

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Project addUserToProject(
            Integer projectId,
            Integer userId,
            String authorizationHeader) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            Project project = projectRepo.findById(projectId)
                    .orElseThrow(() -> new ApiRequestException("Project not found"));


            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User projectCreator = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            if (!project.getProjectCreator().equals(projectCreator)) {
                throw new ApiRequestException("Only project creator can add users to project");
            }

            List<User> usersAssignedProjectList = project.getUsersAssignedProjectList();
            if (usersAssignedProjectList.contains(user)) {
                throw new ApiRequestException("User already assigned to project");
            }
            usersAssignedProjectList.add(user);
            return projectRepo.save(project);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public Project removeUserFromProject(Integer projectId, Integer userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            Project project = projectRepo.findById(projectId)
                    .orElseThrow(() -> new ApiRequestException("Project not found"));

            List<User> usersAssignedProjectList = project.getUsersAssignedProjectList();
            if (!usersAssignedProjectList.contains(user)) {
                throw new ApiRequestException("User not assigned to project");
            }
            usersAssignedProjectList.remove(user);
            return projectRepo.save(project);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public List<Project> getProjectByUser(String authorizationHeader) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            Integer userId = user.getUserId();

            List<Project> projects = projectRepo.findByPersonsAssignedProjectList(userId);
            if (projects == null) {
                throw new ApiRequestException("No projects found");
            }
            return projects;

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public List<Project> getAllOpenProjects() {
        try {
            List<Project> projects = projectRepo.findAllOpenProjects();
            if (projects == null) {
                throw new ApiRequestException("No projects found");
            }
            return projects;

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

}


