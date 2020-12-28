package com.example.aaa.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.bank.entity.Year;

@Repository
public interface YearRepository extends JpaRepository<Year, Integer>{

}
