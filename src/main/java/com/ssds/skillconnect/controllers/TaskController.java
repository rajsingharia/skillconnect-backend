package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.dao.Task;
import com.ssds.skillconnect.model.TaskRequestModel;
import com.ssds.skillconnect.model.TaskResponseModel;
import com.ssds.skillconnect.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/v1/task")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final Logger logger = Logger.getLogger(TaskController.class.getName());

    @GetMapping("/all")
    private List<TaskResponseModel> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/all/{projectId}")
    private List<TaskResponseModel> getAllTasksByProjectId( @PathVariable Integer projectId ) {
        return taskService.getAllTasksByProjectId(projectId);
    }

    @PostMapping("/{projectId}/add-new-task")
    private void addNewTask(
            @PathVariable Integer projectId,
            @RequestBody TaskRequestModel taskRequestModel,
            @RequestHeader(value="Authorization") String authorizationHeader) {
        taskService.addNewTask(projectId, taskRequestModel, authorizationHeader);
    }

    @PostMapping("/{taskId}/update-status/{status}")
    private void updateTaskStatus(
            @PathVariable Integer taskId,
            @PathVariable String status,
            @RequestHeader(value="Authorization") String authorizationHeader) {
        taskService.updateTaskStatus(taskId, status, authorizationHeader);
    }

}
