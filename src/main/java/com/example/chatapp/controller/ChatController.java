package com.example.chatapp.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//辞書的な役割をする（キーを値で送る）
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chatapp.entity.Message;
import com.example.chatapp.entity.User;
import com.example.chatapp.repository.MessageRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

	private MessageRepository messageRepository;

	public ChatController(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@GetMapping("/chat")
	public String chat(Model model, HttpSession session) {
		System.out.println("ここはチャットコントローラーです");
		User loginUser = (User) session.getAttribute("loginUser"); //ログインしてその時のユーザーネームを渡される状況（セッションで）
		if (loginUser == null) {
			return "redirect:/login_place";
		}
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("messages", messageRepository.findAll());

		return "chat";

	}

	@PostMapping("/chat_send")
	public String chat_send(
			@RequestParam String message,
			HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		
		if (loginUser == null) {
			return "redirect:/login_place";
		}

		Message msg = new Message();
		msg.setUser(loginUser);        // ← User をそのまま入れる
		msg.setContent(message);
		msg.setTime(LocalDateTime.now());

		messageRepository.save(msg);

		return "redirect:/chat";

	}

}
