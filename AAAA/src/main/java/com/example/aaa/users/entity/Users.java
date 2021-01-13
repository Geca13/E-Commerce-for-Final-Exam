package com.example.aaa.users.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import org.hibernate.annotations.NaturalId;
import com.example.aaa.shoppingCart.entity.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
	
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	
	@Email
	@NaturalId
	private String email;
	
	private String password;
	
    private String firstName;
	
	private String lastName;
	
	private Integer age;
	
	private LocalDate date;
	
	@Size(max = 45)
	private String token;
	
	
	@OneToOne
	@JoinColumn(referencedColumnName = "id")
	private ShoppingCart cart;
	
		
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles" , joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>() ;
	
	
	

	public Users(String password) {
		super();
		this.password = password;
	}


public Users(@Email String email, String password, String firstName, String lastName, Integer age, LocalDate date,
		Set<Role> roles) {
	super();
	this.email = email;
	this.password = password;
	this.firstName = firstName;
	this.lastName = lastName;
	this.age = age;
	this.date = date;
	this.roles = roles;
}
	
		
}
