package com.dongnaoedu.springcloud.uaa.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user")
public class UserDomain implements Serializable {
	private static final long serialVersionUID = -2485043500570220853L;

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long userId; // 自增长的用户ID
	@Column
	private String userName; // 用户名
	@Column
	private String phone; // 手机号码
	@Column
	private String email; // 邮箱
	@Column
	private String password; // 密码(切记切记，生产环境记得不要用明文存储哦)

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
