package com.ssds.skillconnect.model;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestModel {
    private String taskTitle;
    private String taskDescription;
    private String taskStatus = TaskStatus.PENDING.toString();
    private Timestamp taskCreatedOn = new Timestamp(System.currentTimeMillis());
}
