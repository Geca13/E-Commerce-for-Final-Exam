package com.example.aaa.users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.users.entity.Country;
import com.example.aaa.users.entity.Role;
import com.example.aaa.users.entity.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findByRole(RoleName role);

}
