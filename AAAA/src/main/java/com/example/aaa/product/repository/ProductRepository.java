package com.example.aaa.product.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.aaa.product.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	 
        Boolean existsByKeyword(String keyword);
	
        Product findByKeyword(String keyword);

	@Query("SELECT p FROM Product p WHERE CONCAT(p.productName, ' ', p.productPrice, ' ', p.keyword) LIKE %?1%")
	@Transactional(readOnly = true)
	Page<Product> findBySearch(String search,Pageable pageable );
	
	Page<Product>findAllProductByCategoryId(Integer pid, Pageable pageable);
	
	Page<Product>findAllProductByManufacturerId(Integer mid, Pageable pageable);
	
	Page<Product>findAllProductByCountryId(Integer cid, Pageable pageable);
	
	Page<Product>findAllProductByStoreId(Integer sid, Pageable pageable);
	
        List<Product> findAllProductById(Integer id, Pageable pageable);
	
	@Query("select u from Product u where u.availableQty > 0")
	Page<Product> findAllProductByAvailableQty(Pageable pageable);
		
}
