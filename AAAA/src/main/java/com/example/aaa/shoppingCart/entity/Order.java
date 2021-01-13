package com.example.aaa.shoppingCart.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
	@JoinColumn(referencedColumnName = "id")
	private Users user;
	
	private String address;
	
	private String shipping;
	
	private Double shippingPrice;
	
	private Double subtotal;
	
	private Double total;
		
}
