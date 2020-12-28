package com.example.aaa.shoppingCart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.aaa.shoppingCart.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
