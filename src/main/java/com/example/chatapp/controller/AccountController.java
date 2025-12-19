package com.example.chatapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;//辞書的な役割をする（キーを値で送る）
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chatapp.entity.User;
import com.example.chatapp.repository.UserRepository;

import jakarta.servlet.http.HttpSession; //セッションが使用可能になる

@Controller
public class AccountController {

	private final UserRepository userRepository;

	public AccountController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/register")
	public String Register() {
		System.out.println("ここは、ゲットマッピング（レジスタ）です。");
		return "register";
	}

	@PostMapping("/register_send")
	public String register(
			@RequestParam String username,
			@RequestParam String password) {

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);//ここでパスワードをハッシュ化する！
		System.out.println("このタイミングでパスワードをハッシュ化しました。");

		System.out.println("ここは、ポストマッピング（レジスタ）です。ログイン画面に再アクセスします。");

		userRepository.save(user);

		return "redirect:/login_place";
	}

	@GetMapping("/login_place") //login_placeのHTMLからリクエストされる。それがDBと正しいか見分けるメソッド
	public String login() {
		System.out.println("ここは、ゲットマッピング（ログイン）です。");
		return "login_place";

	}

	@PostMapping("/login_send") //login_placeのHTMLからリクエストされる。それがDBと正しいか見分けるメソッド
	public String login_send(
			@RequestParam String username,
			@RequestParam String password,
			Model model,
			HttpSession session //ログイン成功時にセッション発動する
	) {

		User user = userRepository.findByUsernameAndPassword(username, password); //「DBに保存されている user の中から、username と password が両方一致する1件を探して」

		if (user == null) {
			model.addAttribute("error", "ユーザー名かパスワードが違う"); //モデル.アドアトリビュートで「キーと値」になる
			System.out.println("ログイン失敗");
			return "login_place";
		}

		session.setAttribute("loginUser", user); //キーと値でDBから探してきた名前とパスが一致するものをキーのセッションとしておくる

		System.out.println("ログイン成功");
		return "redirect:/chat";

	}

}
