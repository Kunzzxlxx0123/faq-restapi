package com.spring.faq.payload.response;

import java.util.Set;

import com.spring.faq.models.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

	private Long id;
	private String username;
	private String email;
	private Set<Role> roles;
	public UserResponse(Long id, String username, String email, Set<Role> set) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = set;
	}
	
	
}
