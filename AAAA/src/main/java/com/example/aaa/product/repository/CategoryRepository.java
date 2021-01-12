package com.example.aaa.product.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.aaa.product.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Category findByProductCategory(String productCategory);
	
	Boolean existsByProductCategory(String productCategory);
	
	@Query(value="select * from Category p where p.product_category like :word ", nativeQuery = true)
	List<Category> findByWord(@Param("word") String word);
	
}
