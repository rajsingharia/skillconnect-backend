package com.ssds.skillconnect.model;

import com.ssds.skillconnect.dao.Post;
import com.ssds.skillconnect.dao.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel {
    private Integer postId;
    private String message;
    private Timestamp createdOn = new Timestamp(System.currentTimeMillis());
}
