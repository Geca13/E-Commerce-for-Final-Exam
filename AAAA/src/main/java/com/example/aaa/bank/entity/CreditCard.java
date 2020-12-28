package com.example.aaa.bank.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

import com.example.aaa.shoppingCart.entity.Order;
import com.example.aaa.shoppingCart.entity.ShoppingCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
	
	

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String cardholderName;
	
	@ManyToOne
	private CardBrend brend;
	
	private String cardNumber;
	
    private String month;
	
    private String year;
    
    private String cvv;
    
    private Double balance;
    
    private String contact;
    
 
    
    
}
