package com.ssds.skillconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateRequestModel {
    private String projectName;
    private String projectDetails;
    private Timestamp startDate;
    private Timestamp endDate;
    private Boolean isFinished;
    private Integer departmentId;
    private List<Integer> userIdsAssignedProjectList = new ArrayList<>();
}
