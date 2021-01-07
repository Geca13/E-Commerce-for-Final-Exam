package com.example.aaa.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.bank.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{
	
	Company findByCompanyName(String companyName);
	
	Company findByAccountNumber(String accountNumber);

}
