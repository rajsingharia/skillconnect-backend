package com.ssds.skillconnect.controllers;

import com.ssds.skillconnect.dao.Department;
import com.ssds.skillconnect.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/department")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/get-all")
    private List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

}
