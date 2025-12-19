package com.example.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatapp.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}

//これが使えるようになる
//messageRepository.save(message);　
//messageRepository.findAll();
