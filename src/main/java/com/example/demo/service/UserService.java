package com.example.demo.service;

import java.util.List;

import com.example.demo.dtos.PageableResponse;
import com.example.demo.dtos.UserDto;

public interface UserService {
    //CREATE
UserDto createUser( UserDto userDto);

// UPDATE
UserDto updateUser( UserDto userDto , String userId);

//DELETE
void deleteUser(String userId);



//GET ALL USER
PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize,String sortBy,String sortDir);



//get single user by id
UserDto getUserById(String userId);



//get single user by email
UserDto getUserByEmail(String email);




//search user
//List<UserDto> searchUser(String keywords);

List<UserDto> findByNameContaining(String keywords);


//other user specific details
}
