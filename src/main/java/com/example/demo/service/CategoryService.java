package com.example.demo.service;

import com.example.demo.dtos.CategoryDto;
import com.example.demo.dtos.PageableResponse;

public interface CategoryService {

	
	//CREATE
	// here CategoryDto is a return type of this method
	CategoryDto create (CategoryDto categoryDto);
	
	
	//UPDATE
	CategoryDto update (String categoryId,CategoryDto categoryDto );
	
	//DELETE
	void delete(String categoryId);
	
	//GET ALL
	
	PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy, String sortDir);
	
	
	//GET SINGLE CATEGORY DETAIL
	CategoryDto get(String categoryId);
	
	
	//SEARCH CATEGORY
	PageableResponse<CategoryDto> getSearchCategory(String key, int pageNumber, int pageSize, String sortBy, String sortDir);

	
}
