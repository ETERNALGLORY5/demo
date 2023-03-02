package com.example.demo.service;

import com.example.demo.dtos.PageableResponse;
import com.example.demo.dtos.ProductDto;

public interface ProductService {

	
	//CREATE 
	ProductDto create(ProductDto productDto);
	
	
	// UPDATE
	ProductDto update(String productId, ProductDto productDto);
	
	// GET BY ID
	ProductDto getById(String productId );
	
	//DELETE
	void delete(String productId);
	
	// GET ALL
	PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy, String sortDir);
	
	//GET ALL THAT LIVE
	PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	// SERACH PRODUCT
	PageableResponse<ProductDto> getSearchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
	

	// SAVE PRODUCT IMAGE
	ProductDto uploadImage( String productId, ProductDto productDto);
	
	//CREATE PRODUCT  WITH CATEGORY	
	ProductDto createWithCategory(ProductDto productDto, String categoryId);
	
	//UPDATE CATEGORY OF PRODUCT
	ProductDto updateCategory(String productId, String categoryId);
	
    // GET ALL PRODUCT OF CATEGORY
	PageableResponse<ProductDto> getAllOfCategory( String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
	
}
