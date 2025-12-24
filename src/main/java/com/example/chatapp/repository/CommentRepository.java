package com.example.chatapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatapp.entity.Comment;
import com.example.chatapp.entity.Message;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByMessageOrderByTimeAsc(Message message);
}
