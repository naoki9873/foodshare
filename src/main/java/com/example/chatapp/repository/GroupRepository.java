package com.example.chatapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatapp.entity.Group;


public interface GroupRepository extends JpaRepository<Group, Long> {
	
}