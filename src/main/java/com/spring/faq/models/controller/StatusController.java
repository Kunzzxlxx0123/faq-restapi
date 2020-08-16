package com.spring.faq.models.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.faq.models.Status;
import com.spring.faq.payload.request.StatusRequest;
import com.spring.faq.repository.StatusRepository;
import com.spring.faq.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/request")
public class StatusController {

	@Autowired
	StatusRepository statusRepository;
	@Autowired
	UserRepository userRepository;

	@GetMapping("/statuses")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MODERATOR')")
	public ResponseEntity<List<Status>> getAllStatus() {
		try {
			List<Status> statuses = new ArrayList<>();
			statuses = statusRepository.findAll();
			if (statuses.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<List<Status>>(statuses, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/statuses/{title}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MODERATOR')")
	public ResponseEntity<List<Status>> getBytitle(@Valid @PathVariable String title){
		
		try {
			List<Status> statuses = statusRepository.findByTitleContaining(title).orElseThrow(() -> 
										new Exception("Status not found title: " + title));
			return new ResponseEntity<List<Status>>(statuses, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@PostMapping("/statuses")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MODERATOR')")
	public ResponseEntity<Status> createStatus(@Valid @RequestBody Status status) {
		try {

			UserDetails principal =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			status.setUser(userRepository.findByUsername(principal.getUsername()).get());
			statusRepository.save(status);
			return new ResponseEntity<Status>(status, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<Status>(status, HttpStatus.valueOf(e.getMessage()));
		}
	}
	
	@PutMapping("/statues/{statusId}")
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
	public ResponseEntity<Status> updateStatus(@Valid @RequestBody StatusRequest statusRequest
			,@PathVariable Long statusId){
			try {
				return statusRepository.findById(statusId)
					.map(status -> {
					status.setTitle(statusRequest.getTitle());
					status.setDescription(statusRequest.getDescription());
					statusRepository.save(status);
					return ResponseEntity.ok(status);
				}).orElseThrow(() -> new Exception("Status not found with statusId: " + statusId));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.valueOf(e.getMessage()));
			}	
	}
}
