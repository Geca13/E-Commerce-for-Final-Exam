package com.example.aaa.shoppingCart.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingOption {
	
	@Id
	private Integer id;
	
	private String transporter;
	
	private String days;
	
	private Double price;

}
