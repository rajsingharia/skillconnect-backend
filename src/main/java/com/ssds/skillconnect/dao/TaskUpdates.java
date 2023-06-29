package com.ssds.skillconnect.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//@Entity
//@Table(name = "task_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskUpdateId;
    private String taskUpdateDescription;
    private Timestamp taskUpdateCreatedOn;
}
