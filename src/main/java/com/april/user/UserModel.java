package com.april.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@Getter @Setter
public class UserModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	@NotBlank(message = "帳號不可為空")
	@UniqueUsername
	private String username;

	@Column(unique = true)
	@Email(message = "信箱格式錯誤")
	@NotBlank(message = "信箱不可為空")
	@UniqueEmail
	private String email;

	@Column
	@Size(min = 8, message = "密碼不可少於8位")
	@NotBlank(message = "密碼不可為空")
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
