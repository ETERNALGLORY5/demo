package com.example.demo.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.ApiResponseMessage;
import com.example.demo.dtos.ProductDto;
import com.example.demo.dtos.ImageResponse;
import com.example.demo.dtos.PageableResponse;
import com.example.demo.service.ProductService;

import jakarta.servlet.http.HttpServletResponse;

import com.example.demo.service.FileService;


@RestController
@RequestMapping("")
public class ProductController {

	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;

	
	@Value("${products.image.path}")
	private String imageUploadPath;
	  //= ("images/products");
	
	private Logger logger = LoggerFactory.getLogger(ProductController.class);

	
	//CREATE 
	@PostMapping("/products")
	public ResponseEntity<ProductDto> create ( @RequestBody ProductDto productDto)
	{
		ProductDto product = productService.create(productDto);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
		
	}
	
	
	// UPDATE
	@PutMapping("/products/{productId}")
	public ResponseEntity<ProductDto> update(@PathVariable String productId , @RequestBody ProductDto productDto)
	{
		ProductDto productDto1 = productService.update(productId, productDto);
		return new ResponseEntity<>(productDto1, HttpStatus.OK);
		
	}

	

	//DELETE
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId)
	{
		productService.delete(productId);
		ApiResponseMessage message = ApiResponseMessage.builder()
				                          .message("Product deleted successfully")
				                          .status(HttpStatus.OK)
				                          .success(true)				                          
				                          .build();			
	    return new ResponseEntity<>(message, HttpStatus.OK);
	
    }
	 
	 //GET SINGLE
	 @GetMapping("/products/{productId}")
	 public ResponseEntity<ProductDto> getSingle(@PathVariable String productId)
	 {
		ProductDto singleProduct =  productService.getById(productId);
		 return new ResponseEntity<>(singleProduct, HttpStatus.OK);
	 }
	
	
	
	//GET ALL
	 @GetMapping("/allProducts")
	 public ResponseEntity<PageableResponse<ProductDto>> GetAllProduct
	    (
	    		@RequestParam (value ="pageNumber",defaultValue = "0", required = false) int pageNumber,
    	     @RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
	    	     @RequestParam(value ="sortBy", defaultValue = "title",required = false) String sortBy,
	    	     @RequestParam(value = "sortDir",defaultValue = "ASC",required = false) String sortDir		
	    )
	    {
		  PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize,sortBy,sortDir);
		 return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
	    }
	
	
	
	//GET ALL LIVE
	 @GetMapping("/live")
	 public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct
	   (
			   @RequestParam (value ="pageNumber",defaultValue = "0", required = false) int pageNumber,
	    	     @RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
	    	     @RequestParam(value ="sortBy", defaultValue = "categoryTitle",required = false) String sortBy,
	    	     @RequestParam(value = "sortDir",defaultValue = "ASC",required = false) String sortDir	   
	   )
	   {
		 PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize,sortBy,sortDir);
		 return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
	   }
	
	
    //	SEARCH BY TITLE
        @GetMapping("/search/{query}")
	 public ResponseEntity<PageableResponse<ProductDto>> searchByTitle
	  (
			  @PathVariable String query,
			  @RequestParam (value ="pageNumber",defaultValue = "0", required = false) int pageNumber,
	    	     @RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
	    	     @RequestParam(value ="sortBy", defaultValue = "categoryTitle",required = false) String sortBy,
	    	     @RequestParam(value = "sortDir",defaultValue = "ASC",required = false) String sortDir 
	   )
	  {
		 PageableResponse<ProductDto> pageableResponse = productService.getSearchByTitle(query, pageNumber, pageSize,sortBy,sortDir );
		 return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
	  }
        
        
    
        
    
     // UPLOAD PRODUCT IMAGE
        @PostMapping("/products/image/{productId}")
        public ResponseEntity<ImageResponse> uploadProductImage
          (@RequestParam ("productImage") MultipartFile file,
		          @PathVariable String productId) throws IOException
          {
        	String imageName =   fileService.uploadFile(file,imageUploadPath);
        	
        	ProductDto product = productService.getById(productId);
        	product.setProductImage(imageName);
        	productService.update(productId, product);
        	ImageResponse imageResponse = ImageResponse.builder()
                    .imageName(imageName)
                    .success(true)                
                    .status(HttpStatus.CREATED)
                    .build();
        	return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
          }
        
        
        // SERVE PRODUCT IMAGE
        @GetMapping("/products/image/{productId}")
        public void serveUserImage
		              (@PathVariable String productId, 
		                      HttpServletResponse response) throws IOException
        {
        	ProductDto product = productService.getById(productId);
        	logger.info("Product Image Name : {}",product.getProductImage());
        	InputStream resource = fileService.getResource(imageUploadPath, product.getProductImage());
        	
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(resource,response.getOutputStream());
        	
			
        }
	 
       
        
        //CREATE PRODUCT WITH CATEGORY
        @PostMapping("/categories/{categoryId}/products")
        public ResponseEntity<ProductDto> createProductWithCateogry
         (
        	@PathVariable String categoryId,
        	@RequestBody ProductDto productDto
         )
        {
          return new ResponseEntity<>(productService.createWithCategory(productDto, categoryId), HttpStatus.OK);
        }
        
        
    //UPDATE CATEGORY OF PRODUCT
        @PutMapping("/categories/{categoryId}/products/{productId}")
        public ResponseEntity<ProductDto> updateCategoryOfProduct
        (@PathVariable String categoryId, @PathVariable String productId)
        {
         ProductDto productDto =   productService.updateCategory(productId, categoryId);
        	
        	return new ResponseEntity<>(productDto, HttpStatus.OK);	
        }
        
        
        //GET PRODUCT OF CATEOGRY
		@GetMapping("/categories/{categoryId}/products")
        public ResponseEntity<PageableResponse<ProductDto>> getProductOfCateogry
        (@PathVariable String categoryId,
		@RequestParam (value ="pageNumber",defaultValue = "0", required = false) int pageNumber,
		@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
		@RequestParam(value ="sortBy", defaultValue = "categoryTitle",required = false) String sortBy,
		@RequestParam(value = "sortDir",defaultValue = "ASC",required = false) String sortDir 
		)
        {
         PageableResponse<ProductDto> productDto =   productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        	
        	return new ResponseEntity<>(productDto, HttpStatus.OK);	
        }
        
        
        
	 
}
