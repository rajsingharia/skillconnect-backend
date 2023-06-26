package com.ssds.skillconnect.repository;

import com.ssds.skillconnect.dao.Task;
import com.ssds.skillconnect.model.TaskResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {


    @Query("SELECT new com.ssds.skillconnect.model.TaskResponseModel(" +
            "t.taskId, " +
            "t.taskTitle, " +
            "t.taskDescription, " +
            "t.taskStatus, " +
            "t.taskCreatedOn) " +
            "FROM Task t")
    List<TaskResponseModel> findAllTaskResponseModel();

    @Query("SELECT new com.ssds.skillconnect.model.TaskResponseModel(" +
            "t.taskId, " +
            "t.taskTitle, " +
            "t.taskDescription, " +
            "t.taskStatus, " +
            "t.taskCreatedOn) " +
            "FROM Task t " +
            "WHERE t.project.projectId = :projectId")
    List<TaskResponseModel> findAllTaskResponseModelByProjectId(@Param("projectId") Integer projectId);
}
