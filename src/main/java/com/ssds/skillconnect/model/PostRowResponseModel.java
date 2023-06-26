package com.ssds.skillconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PostRowResponseModel {
    private Integer postId;
    private String postTitle;
    private String postDescription;
    private Integer urgencyLevel;
    private String projectName;
    private String departmentName;
    private List<String> listOfSkillsRequired;
    private Timestamp createdOn;
    private Boolean isSaved;

    public PostRowResponseModel(Integer postId, String postTitle, String postDescription, Integer urgencyLevel, String projectName, String departmentName, List<String> listOfSkillsRequired, Timestamp createdOn) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.urgencyLevel = urgencyLevel;
        this.projectName = projectName;
        this.departmentName = departmentName;
        this.listOfSkillsRequired = listOfSkillsRequired;
        this.createdOn = createdOn;
        isSaved = false;
    }

}
