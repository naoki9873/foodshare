package com.example.chatapp.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name="chat_table")
@NoArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne //「１対多」になる
	@JoinColumn(name = "user_id")
	private User user;

	private String content;
	
	private String imagePath ;  //写真のパス

	private LocalDateTime time;
	
	@OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
	private List<Comment> comments;


}
