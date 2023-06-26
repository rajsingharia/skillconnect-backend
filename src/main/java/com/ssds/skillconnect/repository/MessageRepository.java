package com.ssds.skillconnect.repository;

import com.ssds.skillconnect.dao.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.postedOn.postId = :postId ORDER BY m.createdOn DESC")
    List<Message> findByPostIdSortedByCreatedOn(@Param("postId") Integer postId);

}
