package com.ssds.skillconnect.model;


import com.ssds.skillconnect.dao.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponseModel {
    private Integer userId;
    private String name;
    private String email;
    private Department department;
    private String experience;
    private List<String> listOfSkills;
}
