package com.example.aaa.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.aaa.admin.entity.Payment;
import com.example.aaa.admin.repository.PaymentRepository;

@Service
public class AdminService {
	
	@Autowired
	PaymentRepository paymentRepository;

	public Page<Payment> payments(Integer pageNumbersss, Integer pageSize, String sortField, String sortDirection) {
		
	    Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
		Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNumbersss - 1, pageSize, sort);
		
		return paymentRepository.findAll(pageable);
	}

}
