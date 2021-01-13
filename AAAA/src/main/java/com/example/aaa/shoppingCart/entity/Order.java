package com.example.aaa.shoppingCart.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.aaa.product.entity.Store;
import com.example.aaa.users.entity.Address;
import com.example.aaa.users.entity.Role;
import com.example.aaa.users.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
	
	private LocalDate date;
	
	@ManyToMany
	private List<OrderProducts>products;
	
	@ManyToOne
	private Users user;
	
	private String address;
	
	private String shipping;
	
	private Double shippingPrice;
	
	private Double subtotal;
	
	private Double total;
		
}
