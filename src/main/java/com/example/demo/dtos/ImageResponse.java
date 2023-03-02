package com.example.demo.dtos;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
       
    
public class ImageResponse {
	
    private String imageName;
	private String message;
	private boolean success;
	private HttpStatus status;
   
//	public static Object builder() {
//        return null;
//    }
//	

}