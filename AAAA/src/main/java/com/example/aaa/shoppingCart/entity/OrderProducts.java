package com.example.aaa.shoppingCart.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.example.aaa.product.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProducts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer pid;
	
	private String name;
	
	private Integer qty;
	
	private Double price;
	
	private Double itemTotal;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private Store store ;
	
}
