package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Category;

public interface CategoryRepository extends JpaRepository<Category , String>{

    Page<Category> findByCategoryTitleContaining(String key, Pageable pageable);

}
