package com.spring.faq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.faq.models.Status;
import com.spring.faq.models.User;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long>{

	Optional<List<Status>> findByTitleContaining(String title);
	Optional<List<Status>> findByUser(User user);
}
