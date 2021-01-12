package com.example.aaa.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.aaa.bank.entity.CreditCard;
import com.example.aaa.bank.entity.Month;
import com.example.aaa.bank.entity.Year;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
	
	Boolean existsByCardNumber(@Param(value = "cardNumber") String cardNumber);
	
	CreditCard findByCardNumber(@Param(value = "cardNumber") String cardNumber);
	
        Boolean existsByCardholderName(@Param(value = "cardholderName") String cardholderName);
	
	CreditCard findByCardholderName(@Param(value = "cardholderName") String cardholderName);
	
	Boolean existsByCvv(@Param(value = "cvv") String cvv);
	
	CreditCard findByCvv(@Param(value = "cvv") String cvv);
	
        Boolean existsByMonth(@Param(value = "month") Month month);
    
	CreditCard findByMonth(@Param(value = "month") Month month);
	
        Boolean existsByYear(@Param(value = "year") Year year);
	
	CreditCard findByYear(@Param(value = "year") Year year);

}
