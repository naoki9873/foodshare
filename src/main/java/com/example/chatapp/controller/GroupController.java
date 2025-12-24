package com.example.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chatapp.entity.Group;
import com.example.chatapp.entity.GroupMember;
import com.example.chatapp.entity.User;
import com.example.chatapp.repository.GroupMemberRepository;
import com.example.chatapp.repository.GroupRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class GroupController {

    private final GroupRepository groupRepo;
    private final GroupMemberRepository memberRepo;

    public GroupController(GroupRepository groupRepo,
                           GroupMemberRepository memberRepo) {
        this.groupRepo = groupRepo;
        this.memberRepo = memberRepo;
    }

    @PostMapping("/groups/create")
    public String createGroup(
            @RequestParam String name,
            HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        Group group = new Group();
        group.setName(name);
        group.setOwner(loginUser);
        groupRepo.save(group);

        // 作成者を自動参加
        GroupMember gm = new GroupMember();
        gm.setGroup(group);
        gm.setUser(loginUser);
        memberRepo.save(gm);

        return "redirect:/chat";
    }
}
