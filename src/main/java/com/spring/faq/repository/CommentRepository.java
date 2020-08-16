package com.spring.faq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.faq.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	Optional<List<Comment>> findByStatusId(Long id);
}
