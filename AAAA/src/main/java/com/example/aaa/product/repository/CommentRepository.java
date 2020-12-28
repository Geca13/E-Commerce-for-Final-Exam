package com.example.aaa.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.product.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	Page<Comment> findAllByProductId(Integer id,Pageable pageable);  

}
