package com.ssds.skillconnect.service;

import com.ssds.skillconnect.config.JwtService;
import com.ssds.skillconnect.dao.Project;
import com.ssds.skillconnect.dao.Task;
import com.ssds.skillconnect.dao.User;
import com.ssds.skillconnect.model.TaskRequestModel;
import com.ssds.skillconnect.model.TaskResponseModel;
import com.ssds.skillconnect.model.TaskStatus;
import com.ssds.skillconnect.repository.ProjectRepository;
import com.ssds.skillconnect.repository.TaskRepository;
import com.ssds.skillconnect.repository.UserRepository;
import com.ssds.skillconnect.utils.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepo;

    public List<TaskResponseModel> getAllTasks() {
        try {
            return taskRepository.findAllTaskResponseModel();
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public List<TaskResponseModel> getAllTasksByProjectId(Integer projectId) {
        try {
            return taskRepository.findAllTaskResponseModelByProjectId(projectId);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }


    public void addNewTask(
            Integer projectId,
            TaskRequestModel taskRequestModel,
            String authorizationHeader) {
        try {

            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User currentUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            Project project = projectRepo.findById(projectId).orElseThrow(() -> new ApiRequestException("Project not found"));

            if(!project.getProjectCreator().equals(currentUser)) {
                throw new ApiRequestException("Only project creator can add tasks to project");
            }

            Task task = new Task();
            task.setTaskTitle(taskRequestModel.getTaskTitle());
            task.setTaskDescription(taskRequestModel.getTaskDescription());
            task.setTaskStatus(TaskStatus.valueOf(taskRequestModel.getTaskStatus()));
            task.setTaskCreatedOn(taskRequestModel.getTaskCreatedOn());

            User taskAssignedUser = userRepository.findById(taskRequestModel.getTaskAssignedUserId())
                    .orElseThrow(() -> new ApiRequestException("Assigned User not found"));

            task.setTaskAssignedUser(taskAssignedUser);

            task.setProject(project);

            taskRepository.save(task);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    public void updateTaskStatus(Integer taskId, String status, String authorizationHeader) {
        try {
            String jwtToken = authorizationHeader.substring(7);
            String userEmail = jwtService.extractUserEmail(jwtToken);

            User currentUser = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new ApiRequestException("User not found"));

            Task task = taskRepository.findById(taskId).orElseThrow(() -> new ApiRequestException("Task not found"));

            if(task.getTaskStatus().equals(TaskStatus.valueOf(status))) {
                throw new ApiRequestException("Task status is already " + status);
            }

            if(!task.getProject().getProjectCreator().equals(currentUser) &&
               !task.getProject().getUsersAssignedProjectList().contains(currentUser)) {
                throw new ApiRequestException("Only project creator or assigned users can update task status");
            }

            task.setTaskStatus(TaskStatus.valueOf(status));

            taskRepository.save(task);

        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

}
