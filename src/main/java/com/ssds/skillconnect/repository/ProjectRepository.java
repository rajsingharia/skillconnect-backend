package com.ssds.skillconnect.repository;

import com.ssds.skillconnect.dao.Project;
import com.ssds.skillconnect.model.ProjectRowResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT new com.ssds.skillconnect.model.ProjectRowResponseModel(" +
            "p.projectId, " +
            "p.projectName, " +
            "p.projectDetails, " +
            "p.startDate, " +
            "p.endDate, " +
            "p.isFinished, " +
            "p.department.departmentName) " +
            "FROM Project p WHERE p.projectCreator.userId = :userId OR :userId IN (SELECT u.userId FROM p.usersAssignedProjectList u)")
    List<ProjectRowResponseModel> findByPersonsAssignedProjectList(@Param("userId") Integer userId);

    @Query("SELECT p FROM Project p WHERE p.isFinished = false")
    List<Project> findAllOpenProjects();

    @Query("SELECT new com.ssds.skillconnect.model.ProjectRowResponseModel(" +
            "p.projectId, " +
            "p.projectName, " +
            "p.projectDetails, " +
            "p.startDate, " +
            "p.endDate, " +
            "p.isFinished, " +
            "p.department.departmentName) " +
            "FROM Project p")
    List<ProjectRowResponseModel> findAllProjectRowResponseModels();


//    @Query("SELECT p.usersAssignedProjectList.userId FROM Project p WHERE p.projectId = :projectId")
//    List<Integer> findUserIdsByProjectId(@Param("projectId") Integer projectId);

}
