package com.beha.controller.api;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beha.dto.EmailPasswordRequestDTO;
import com.beha.dto.UserDTO;
import com.beha.dto.UserResponseDTO;
import com.beha.service.UserService;

@RestController
@RequestMapping(path="/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@RequestMapping(path="/signIn",method=RequestMethod.POST)
	public ResponseEntity<UserResponseDTO> signIn(@RequestBody EmailPasswordRequestDTO emailPasswordRequestDTO ) {
		UserResponseDTO userResponseDTO = userService.signIn(emailPasswordRequestDTO);
		return new ResponseEntity<UserResponseDTO>(userResponseDTO ,  HttpStatus.OK);
	  }
	
	@RequestMapping(path="/add",method=RequestMethod.POST)
	public ResponseEntity<UserResponseDTO> addUser(@RequestBody UserDTO userDTO) {
		UserResponseDTO userResponseDTO = userService.addUser(userDTO);
		return new ResponseEntity<UserResponseDTO>(userResponseDTO ,  HttpStatus.OK);	     
	  }
}
