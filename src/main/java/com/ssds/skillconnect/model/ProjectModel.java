package com.ssds.skillconnect.model;

import com.ssds.skillconnect.dao.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectModel {
    private String projectName;
    private String projectDetails;
    private Timestamp startDate;
    private Timestamp endDate;
    private Boolean isFinished;
    private Integer departmentId;
    private List<Integer> userIdsAssignedProjectList = new ArrayList<>();
}
