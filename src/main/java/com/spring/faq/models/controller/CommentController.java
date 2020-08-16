package com.spring.faq.models.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.faq.models.Comment;
import com.spring.faq.repository.CommentRepository;
import com.spring.faq.repository.StatusRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/request")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	StatusRepository statusRepository;
	
	@GetMapping("/statuses/{statusId}/comments")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
	public ResponseEntity<?> getCommentByStatusId(@PathVariable Long statusId){
		
		try {
			List<Comment> comments = commentRepository.findByStatusId(statusId).orElseThrow(() -> 
					new Exception("Status not found with statusId: " + statusId));
			if (comments.isEmpty()) {
				return new ResponseEntity<>("Status haven't comment", HttpStatus.OK);
			} else return new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/statuses/{statusId}/comments")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
	public ResponseEntity<?> createComment(@ Valid @RequestBody Comment comment, @PathVariable Long statusId){
		try {
			return statusRepository.findById(statusId)
					.map(status -> {
						comment.setStatus(status);
						commentRepository.save(comment);
						return new ResponseEntity<>(comment, HttpStatus.OK);
					}).orElseThrow(() -> new Exception("Status not found with statusId: " + statusId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
