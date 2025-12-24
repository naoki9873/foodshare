package com.example.chatapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.chatapp.entity.GroupMember;
import com.example.chatapp.entity.Message;
import com.example.chatapp.entity.User;
import com.example.chatapp.repository.GroupMemberRepository;
import com.example.chatapp.repository.MessageRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

	private final MessageRepository messageRepository;
    private final GroupMemberRepository groupMemberRepository;

    public ChatController(
            MessageRepository messageRepository,
            GroupMemberRepository groupMemberRepository) {

        this.messageRepository = messageRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // ★ 画像保存先（絶対パス）
    private static final String UPLOAD_DIR =
            System.getProperty("user.home") + "/chatapp/uploads";

    @GetMapping("/chat")
    public String chat(Model model, HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login_place";
        }

        List<GroupMember> members =
        		groupMemberRepository.findByUser(loginUser);

        model.addAttribute("groups", members);

        return "chat";
    }


    @PostMapping("/chat_send")
    public String chat_send(
            @RequestParam(required = false) String message,
            @RequestParam(required = false) MultipartFile image,
            HttpSession session,
            Model model) throws IOException {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login_place";
        }

        Message msg = new Message();
        msg.setUser(loginUser);
        msg.setContent(message);
        msg.setTime(LocalDateTime.now());
        
        
     // ===== ① 1日3投稿までの制限 =====
        LocalDateTime startOfDay =
                LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long todayPostCount =
                messageRepository.countByUserAndTimeBetween(
                        loginUser, startOfDay, endOfDay);

        if (todayPostCount >= 3) {
            model.addAttribute("error", "投稿は1日3回までです");
            model.addAttribute("loginUser", loginUser);
            model.addAttribute("messages",
                    messageRepository.findAllByOrderByTimeDesc());
            return "chat";
        }

        // ★ 画像がある場合だけ保存
        if (image != null && !image.isEmpty()) {

            String fileName =
                    UUID.randomUUID() + "_" + image.getOriginalFilename();

            Path savePath = Paths.get(UPLOAD_DIR, fileName);

            // ディレクトリが無ければ作る
            Files.createDirectories(savePath.getParent());

            // ファイル保存
            image.transferTo(savePath.toFile());

            // DBには「パスだけ」保存
            msg.setImagePath("/uploads/" + fileName);

            System.out.println("画像保存成功: " + savePath.toAbsolutePath());
        }

        messageRepository.save(msg);
        return "redirect:/chat";
    }
}
