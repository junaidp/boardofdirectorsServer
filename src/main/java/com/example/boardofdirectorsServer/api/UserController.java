package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.UserHelper;
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
		// userEntity.setContactNumber(Integer.parseInt(userEntity.getContactNumber()));
		userEntity.setUserId(user.getAvaiablaeUserId());
		return user.saveUser(userEntity);
	}

	@PostMapping("/signIn")
	public String singIn(@RequestBody UserTest userTest) throws Exception {
		System.out.println(userTest.getName() + "," + userTest.getPassword());
		return user.getUser(userTest.getName(), userTest.getPassword());

	}

	@GetMapping("/getAllUsers")
	public String getAllUsers() {
		return user.getAllUsers();
	}

	@GetMapping("/getUserWithId")
	public String getUserWithId(@RequestParam String userId) throws Exception {
		return user.getUserWithId(userId);
	}

}
