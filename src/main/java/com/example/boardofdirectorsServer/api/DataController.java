package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.model.UserData;
import com.example.boardofdirectorsServer.model.UserTest;

@RequestMapping("/data")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataController {
	

	@Autowired
	DataHelper userData;
	
	@PostMapping("/saveData")
	public String saveUser(@RequestBody UserData userEntity)
	{
		return userData.saveData(userEntity);
	}
	
	@PostMapping("/getData")
	public String getData(@RequestBody String userId) throws Exception
	{
		System.out.println(userId);
		return userData.getUserData(userId);
		
	}
	
}