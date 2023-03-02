package com.example.demo.dtos;

import java.util.Date;

import com.example.demo.validate.ImageNameValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class ProductDto {


	private String productId;
	
	 @Size(min = 3, max=50, message = "Invalid Product Title !!")
	 @NotBlank(message  ="Title is required, min 3 character !!")
	private String title;
	
	 @Size(min = 3, max=50, message = "Invalid product description !!")
	 @NotBlank(message  ="Description is required !!")
	private String productPrescription;
	
     // @NotBlank(message = "price must required")
	private int price;
	
	  
	private int quantity;
	
    
	private Date mfgDate;
	
	
	private Date expDate;
	
	
	private boolean live;
	
	
	private boolean stock;

	private String fileName;
	
	@ImageNameValid
	private String productImage;
	
	private CategoryDto category;
}
