package com.example.demo.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.PageableResponse;
import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.helper.Helper;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.UserService;

@Service

public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	 @Value("${user.image.path}")
	private String imagepath;
	   //= ("images/users");
	
	
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	

	@Override
	public UserDto createUser(UserDto userDto) {
		//dto to entity
		User user = mapper.map(userDto, User.class);			    
	  //generate unique id in string format
	  		String userId = UUID.randomUUID().toString();
	  	    user.setUserId(userId);	  	
		    User savedUser = userRepository.save(user);
            //entity to dto	   
		return mapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		
		User user = userRepository.findById(userId).orElseThrow(( ) -> new ResourceNotFoundException("User not found by thi s Id"));		
		user.setName(userDto.getName());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());
		//save data		
		User updatedUser = userRepository.save(user);
		return mapper.map(updatedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String userId) {
	  
		User user = userRepository.findById(userId).orElseThrow(( ) -> new ResourceNotFoundException("User not found by thi s Id"));		
		//delete user		
		userRepository.delete(user);		
		//delete user Image		
		// fullPath is images/users/abc.png
		
        String fullPath = imagepath+user.getImageName();		
		Path path = Paths.get(fullPath);		
		try {
			Files.delete(path);
		} catch (NoSuchFileException ex) {
			logger.info("user image not found in folder");
			ex.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy, String sortDir) {
		
		//Sort sort = Sort.by(sortBy); 
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending()) ; 
		//this page num default starts from 0
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
             /*
			  * //pageable will not return List direct
			      it will return obj of page
				  from which we can get content as List<>;
			  */
        Page<User> page = userRepository.findAll(pageable);
		 PageableResponse<UserDto> response = Helper.getPageableResponse(page , UserDto.class);
					return response;		          
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId).orElseThrow(( ) -> new ResourceNotFoundException("User not found by thi s Id"));
		return mapper.map(user, UserDto.class);
	}

	//very imp topic
	@Override
	public UserDto getUserByEmail(String email) {
	
	User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not with given email ID and password"));
		return mapper.map(user, UserDto.class);
	}


	@Override
	public List<UserDto> findByNameContaining(String keywords) {
		List<User> users=   userRepository.findByNameContaining(keywords);     


		List<UserDto> dtoList = users.stream()
                                .map(user -> entityToDto(user))
                                .collect(Collectors.toList());
			return dtoList;
	}
		
	
	private UserDto entityToDto(User savedUser) {

		return mapper.map(savedUser,UserDto.class);
	}

}

