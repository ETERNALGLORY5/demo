package com.example.demo.dtos;

import com.example.demo.validate.ImageNameValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {

	
	
	private String categoryId;
	
	 @Size(min = 3, max=50, message = "Invalid Category Title !!")
	 @NotBlank(message  ="Title is required, min 3 character !!")
	private String categoryTitle;
	
	 @Size(min = 3, max=50, message = "Invalid description !!")
	 @NotBlank(message  ="Description is required !!")
	private String description;
	
	 
	 @ImageNameValid
	  @NotBlank(message  ="cover image is required !!")
	 private String coverImage;
}