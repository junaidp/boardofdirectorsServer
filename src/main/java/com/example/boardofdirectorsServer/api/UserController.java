package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.UserEntity;

@RequestMapping("User")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
	
	@Autowired
	User user;
	
	@PostMapping("/saveUser")
	public String saveUser(@RequestBody UserEntity userEntity)
	{

		return user.saveUser(userEntity);
	}
	
	@GetMapping("/getUser")
	public String singIn(@RequestBody String userName, String password) throws Exception
	{
		return user.getUser(userName, password);
		
	}
	
	@GetMapping("/getAllUsers")
	public String getAllUsers()
	{
		return user.getAllUsers();
	}

}
