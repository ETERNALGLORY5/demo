package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Category;
import com.example.demo.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	Page<Product> findByTitleContaining( String subTitle, Pageable pageable);
	
	//List<Product> findByLive(boolean live);
	Page<Product> findByLiveTrue(Pageable pageable);


	Page<Product> findByCategory( Category category, Pageable pageable);
	
}
