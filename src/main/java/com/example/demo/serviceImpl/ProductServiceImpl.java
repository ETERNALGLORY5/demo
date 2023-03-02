package com.example.demo.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
//import java.util.Date;
import java.util.UUID;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.PageableResponse;
import com.example.demo.dtos.ProductDto;
import com.example.demo.entities.Category;
import com.example.demo.entities.Product;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.helper.Helper;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	

     @Autowired
     private ProductRepository productRepository;
     
     @Autowired
     private ModelMapper mapper;
     
     @Value("${products.image.path}")
 	private String imagePath;
 	   //=("images/products");
     
     private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
     
    @Autowired
     private CategoryRepository categoryRepository;
     

     //CREATE
	@Override
	public ProductDto create(ProductDto productDto) {
		
		 Product product =	mapper.map(productDto , Product.class);
		// random ID
		String productId = UUID.randomUUID().toString();
	    product.setProductId(productId);	    
	    // added date	    
	    java.time.LocalDate.of( 2023 , Month.JANUARY , 7 )
	    .plusMonths( 24 )
	    .toString();	    
	    //product.setMfgDate(new Date());
	    //product.setExpDate(new Date());
		//convert productDto to product entity	 
	    // save new product to product entity
		Product newProduct = productRepository.save(product);
		// convert entity to dto
     return mapper.map(newProduct, ProductDto.class);
	}
	
	

	//UPDATE
	@Override
	public ProductDto update(String productId, ProductDto productDto) {
		//get product through the Id
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found through this Id"));
		
		//update product details
		product.setTitle(productDto.getTitle());
		product.setProductPrescription(productDto.getProductPrescription());
		product.setPrice(productDto.getPrice());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.isStock());
		product.setLive(productDto.isLive());
		product.setMfgDate(productDto.getMfgDate());
		product.setExpDate(productDto.getExpDate());
		//save updated product to product entity
		Product updatedProduct = productRepository.save(product);
		// convert entity to dto
		return mapper.map(updatedProduct, ProductDto.class);
		
	}
	

	//GET BY ID
	@Override
	public ProductDto getById(String productId) {
		//get product of given Id
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product with given Id not found"));
		return mapper.map(product, ProductDto.class);
	}

	//DELETE
	@Override
	public void delete(String productId) {
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product with given Id not found"));
		productRepository.delete(product);
		
		
		//delete user profile Image name
		// fullPath is images/users/abc.png
		String fullPath = imagePath+product.getProductImage();
		
		//delete user Image
										
				Path path = Paths.get(fullPath);
				
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.info("product image not found in folder");
					e.printStackTrace();
				}
		
	}
	
	//GET ALL
	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ; 
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page , ProductDto.class);
		return response;
	}



	//GET ALL LIVE
	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}



	//SEARCH BY TITLE
	@Override
	public PageableResponse<ProductDto> getSearchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(page, ProductDto.class);
		return response;
	}

   // CREATE WITH CATEGORY

	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
	//fetch category from database
	Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("category with this Id not found")); 
	
	Product product =	mapper.map(productDto , Product.class);
	// random ID
	String productId = UUID.randomUUID().toString();
    product.setProductId(productId); 
    product.setCategory(category);
    // added date	    
          java.time.LocalDate.of( 2023 , Month.JANUARY , 7 )
             .plusMonths( 24 )
             .toString();	    
    //product.setMfgDate(new Date());
    //product.setExpDate(new Date());
	//convert productDto to product entity	 
    // save new product to product entity
	         Product newProduct = productRepository.save(product);
	        // convert entity to dto
            return mapper.map(newProduct, ProductDto.class);

}

    // UPDATE WITH CATEGORY

	@Override
	public ProductDto updateCategory(String productId, String categoryId) {
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found throw this Id"));
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found throw this Id"));
		// assign category within product
		product.setCategory(category);
		Product savedCategory = productRepository.save(product);
		return mapper.map(savedCategory, ProductDto.class);
	}

    //GET ALL PRODUCT OF CATEGORY

	@Override
	public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ; 
		Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category not found throw this Id"));
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByCategory(category, pageable);
		return Helper.getPageableResponse(page, ProductDto.class);
	}


	//UPLOAD PRODUCT IMAGE

	@Override
	public ProductDto uploadImage(String productId, ProductDto productDto) {
		Product product =	mapper.map(productDto , Product.class);
		Product newProduct = productRepository.save(product);
		return mapper.map(newProduct, ProductDto.class);
	}	
	
}
