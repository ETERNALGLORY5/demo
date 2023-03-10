package com.example.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import com.example.demo.dtos.ImageResponse;
import com.example.demo.dtos.PageableResponse;
import com.example.demo.dtos.UserDto;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
 
	@Autowired
	private FileService fileService;

	@Value("${user.image.path}")
	private String imageUploadPath;
	   //= ("images/users");
	
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	
	// CREATE
	/*
	 * we use ResponseEntity bcoz with data we also share the response, we use UserDto
	 *  to send the data and it's name will be createUser. since we require data so we
	 *  use UserDto with name userDto with @RequestBody.
	 *  
	 *  the method we require to create user is held with UserService so @Autowired it.
	 *  
	 *  
	 */
	
	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
	{
		UserDto user = userService.createUser(userDto);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
		//
	}
	
	//UPDATE
	/*
	 * we get data and userId which we have to update.
	 * we return what we update. means UserDto.
	 * @PathVariable is used to fetch the value of {userId} into the variable "Sting
	 * userId" .
	 * the
	 * we get the new data in form of JSON in @RequestBody
	 */
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser
	     (    @RequestBody UserDto userDto,
	    		 @PathVariable String userId
	    		 )
	{
		UserDto updatedUserDto = userService.updateUser(userDto, userId);
		return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
	}
	
	
	
	// DELETE
	/*we should not take String data type in ResponseEntity, will handle it later.
	 * since normal string is not the JSON data.
	 * since java method variable String userId and path variable {userId} are same 
	 * so we don't provide variable for PathVariable.
	 * 
	 */
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)
	{
		userService.deleteUser(userId);
		ApiResponseMessage message =  ApiResponseMessage
				.builder()
				.message("User Is deleted successfully ")
				.success(true)
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<ApiResponseMessage>(message ,HttpStatus.OK);
	}
	
	
	
	//GETALL
	/*we use list bcoz we get whole list of data
	 * 
	 */
	
	@GetMapping		
	public ResponseEntity<PageableResponse<UserDto>> getAllUser(
		@RequestParam (value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
		@RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,

		@RequestParam (value = "sortBy",defaultValue = "name",required = false) String sortBy,
		@RequestParam(value = "sortDir",defaultValue = "ASC",required = false) String sortDir
	)
	{
		return new ResponseEntity<>(userService.getAllUser(pageNumber, pageSize,sortBy,sortDir),HttpStatus.OK);
	}
			
	
	
	//GET SINGLE USER BY ID
	/*
	 * 
	 */
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId)
	{
		return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
	}
	
	
	
	
	// GET SINGLE USER BY EMAIL
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
	{
		return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
	}
	
	
	//SEARCH USER
	/*
	 * if String and URL request value are same we don't required to provide value with
	 * @Pathvariable
	 * 
	 * when we search we get list of userDto so we use List<> .
	 */
	
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords)
	{
		return new ResponseEntity<>(userService.findByNameContaining(keywords),HttpStatus.OK);
	}


	//UPLOAD USER IMAGE
	
    @PostMapping("/imageCover/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage
	  //we write the name of key of file in postman we passed in RequestParameter
	                (@RequestParam("userImage") MultipartFile image,
					      @PathVariable String userId) throws IOException
    	{

		String imageName =   fileService.uploadFile(image,imageUploadPath);

		//save Image on database

		 UserDto user = userService.getUserById((userId));
		 user.setImageName(imageName);
		  userService.updateUser(user, userId);
        // UserDto userDto = userService.updateUser(user, userId);
	ImageResponse imageResponse = ImageResponse.builder()
			                      .imageName(imageName)
			                      .success(true)			                  
			                      .status(HttpStatus.CREATED)
			                      .build();	
			  return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
		
	}




	// SERVE USER IMAGE
    
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException
    {
    	UserDto user = userService.getUserById(userId);
    	logger.info("User Image Name : {}",user.getImageName());
    	InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
    	response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    	StreamUtils.copy(resource,response.getOutputStream());
    }
    
    
    
    
}
