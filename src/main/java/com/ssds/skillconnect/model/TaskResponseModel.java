package com.ssds.skillconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseModel {
    private Integer taskId;
    private String taskTitle;
    private Integer taskAssignedUserId;
    private String taskAssignedUserName;
    private String taskDescription;
    private TaskStatus taskStatus;
    private Timestamp taskCreatedOn;
}
