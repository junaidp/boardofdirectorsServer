package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserTest;

@RequestMapping("/users")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
	

	@Autowired
	UserHelper user;
	
	@PostMapping("/saveUser")
	public String saveUser(@RequestBody User userEntity)
	{

		return user.saveUser(userEntity);
	}
	
	@GetMapping("/getUser")
	public String singIn(@RequestBody UserTest users) throws Exception
	{
		System.out.println(users.getName() +","+ users);
		UserHelper user = new UserHelper();
		return user.getUser(users.getName(), users.getPassword());
		
	}
	
	@GetMapping("/getAllUsers")
	public String getAllUsers()
	{
		return user.getAllUsers();
	}
	
	@GetMapping("/getUserWithId")
	public String getUserWithId(@RequestBody String userId) throws Exception
	{
		return user.getUserWithId(userId);
	}

}
