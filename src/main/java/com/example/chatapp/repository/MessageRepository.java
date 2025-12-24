package com.example.chatapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatapp.entity.Message;
import com.example.chatapp.entity.User;

public interface MessageRepository extends JpaRepository<Message, Long> {

	// 全投稿を新しい順
	List<Message> findAllByOrderByTimeDesc();

	// 今日の投稿数
	long countByUserAndTimeBetween(
			User user,
			LocalDateTime start,
			LocalDateTime end);

	// ★ ユーザーの投稿を新しい順
	List<Message> findByUserOrderByTimeDesc(User user);
}

//これが使えるようになる
//messageRepository.save(message);　
//messageRepository.findAll();
