package com.example.chatapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatapp.entity.Group;
import com.example.chatapp.entity.GroupMember;
import com.example.chatapp.entity.User;

public interface GroupMemberRepository
extends JpaRepository<GroupMember, Long> {

boolean existsByGroupAndUser(Group group, User user);

List<GroupMember> findByUser(User user);

}
