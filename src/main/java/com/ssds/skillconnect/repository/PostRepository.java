package com.ssds.skillconnect.repository;

import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.dao.Skill;
import com.ssds.skillconnect.model.PostRowResponseModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT new com.ssds.skillconnect.model.PostRowResponseModel(" +
            "p.postId, " +
            "p.postTitle, " +
            "p.postDescription, " +
            "p.urgencyLevel, " +
            "p.project.projectName, " +
            "p.project.department.departmentName, " +
            "p.createdOn) " +
            "FROM Post p " +
            "WHERE (:departmentId IS NULL OR p.project.department.departmentId = :departmentId) " +
            "AND (:urgencyLevel IS NULL OR p.urgencyLevel = :urgencyLevel) " +
            "AND (:skill IS NULL OR lower(:skill) IN (SELECT lower(s.skillName) FROM p.listOfSkillsRequired l JOIN Skill s ON l.skillId = s.skillId)) " +
            "ORDER BY p.createdOn DESC")
    List<PostRowResponseModel> findByDepartmentIdAndPriorityDESCRowResponseModelAndSkill(
            @Param("departmentId") Integer departmentId,
            @Param("urgencyLevel") Integer urgencyLevel,
            @Param("skill") String skill
    );

    @Query("SELECT new com.ssds.skillconnect.model.PostRowResponseModel(" +
            "p.postId, " +
            "p.postTitle, " +
            "p.postDescription, " +
            "p.urgencyLevel, " +
            "p.project.projectName, " +
            "p.project.department.departmentName, " +
            "p.createdOn) " +
            "FROM Post p " +
            "WHERE (:departmentId IS NULL OR p.project.department.departmentId = :departmentId) " +
            "AND (:urgencyLevel IS NULL OR p.urgencyLevel = :urgencyLevel) " +
            "AND (:skill IS NULL OR lower(:skill) IN (SELECT lower(s.skillName) FROM p.listOfSkillsRequired l JOIN Skill s ON l.skillId = s.skillId)) " +
            "ORDER BY p.createdOn ASC")
    List<PostRowResponseModel> findByDepartmentIdAndPriorityASCRowResponseModelAndSkill(
            @Param("departmentId") Integer departmentId,
            @Param("urgencyLevel") Integer urgencyLevel,
            @Param("skill") String skill
    );

    @Query("SELECT new com.ssds.skillconnect.model.PostRowResponseModel(" +
            "p.postId, " +
            "p.postTitle, " +
            "p.postDescription, " +
            "p.urgencyLevel, " +
            "p.project.projectName, " +
            "p.project.department.departmentName, " +
            "p.createdOn) " +
            "FROM Post p " +
            "ORDER BY p.createdOn DESC")
    List<PostRowResponseModel> findAllPostsRowResponseModel();

    @Transactional
    @Query("DELETE FROM Post p WHERE p.isOpen = :isOpen AND p.createdOn < :thresholdDate")
    void deleteAllByIsOpenAndCreatedOnBefore(
            @Param("isOpen") boolean isOpen,
            @Param("thresholdDate") LocalDate thresholdDate
    );

    @Query("SELECT p.listOfSkillsRequired FROM Post p WHERE p.postId = :postId")
    List<Skill> findSkillsByPostId(@Param("postId") Integer postId);

}
