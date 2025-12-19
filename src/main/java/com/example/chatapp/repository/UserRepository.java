package com.example.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
    User findByUsernameAndPassword(String username , String password);

}
