package com.example.aaa.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.users.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
	
	Users findByEmail(String email);
	
	Users findByToken(String token);

}
