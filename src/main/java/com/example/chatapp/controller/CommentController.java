package com.example.chatapp.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chatapp.entity.Comment;
import com.example.chatapp.entity.Message;
import com.example.chatapp.entity.User;
import com.example.chatapp.repository.CommentRepository;
import com.example.chatapp.repository.MessageRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommentController {

    private final CommentRepository commentRepository;
    private final MessageRepository messageRepository;

    public CommentController(CommentRepository commentRepository,
                             MessageRepository messageRepository) {
        this.commentRepository = commentRepository;
        this.messageRepository = messageRepository;
    }

    @PostMapping("/comment_send")
    public String commentSend(
            @RequestParam Long messageId,
            @RequestParam String content,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login_place";
        }

        Message message =
                messageRepository.findById(messageId).orElse(null);

        if (message == null) {
            return "redirect:/chat";
        }

        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setUser(loginUser);
        comment.setContent(content);
        comment.setTime(LocalDateTime.now());

        commentRepository.save(comment);

        return "redirect:/chat";
    }
}
