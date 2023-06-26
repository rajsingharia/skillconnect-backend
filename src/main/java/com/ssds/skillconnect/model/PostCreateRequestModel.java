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
public class PostCreateRequestModel {

    private Integer projectId;

    private String postTitle;

    private String postDescription;

    private Integer urgencyLevel;

    private List<String> listOfSkillsRequired = new ArrayList<>();

    private Integer totalApplicants = 0;

    private Boolean isOpen = true;

    private Timestamp createdOn = new Timestamp(System.currentTimeMillis());

}
