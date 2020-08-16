package com.spring.faq.models.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.spring.faq.models.User;
import com.spring.faq.payload.response.UserResponse;
import com.spring.faq.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/request")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUser() {
		try {
			List<User> users = new ArrayList<User>();
			users = userRepository.findAll();
			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				List<UserResponse> userResponse = new ArrayList<>();
				users.stream().forEach(user -> {
					userResponse.add(new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles()));
				});
				return new ResponseEntity<List<UserResponse>>(userResponse, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> findUserByUsername(@Valid @PathVariable String username) {
//		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//		return new ResponseEntity<User>(user,HttpStatus.OK);
		return userRepository.findByUsername(username).map(user -> {
			return new ResponseEntity<>(user, HttpStatus.OK);
		}).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

	}

	@DeleteMapping("/users/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserByUsername(@Valid @PathVariable String username) {
//		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//		userRepository.delete(user);
//		return new ResponseEntity<>("Delete user successfully",HttpStatus.OK);
		return userRepository.findByUsername(username).map(user -> {
			userRepository.delete(user);
			return ResponseEntity.ok("Delete user successfully");
		}).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}
