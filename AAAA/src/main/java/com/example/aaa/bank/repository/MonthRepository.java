package com.example.aaa.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.aaa.bank.entity.CreditCard;
import com.example.aaa.bank.entity.Month;

@Repository
public interface MonthRepository extends JpaRepository<Month, Integer> {
	
    Boolean existsByMonth(Month month);
	
	Month findByMonth(Month month);

}
