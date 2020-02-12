package com.example.boardofdirectorsServer.api;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.model.UserData;
import com.example.boardofdirectorsServer.model.UserTest;
import com.google.gson.Gson;

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
	
	@GetMapping("/getData")
	public String getData(@PathParam("userId") String userId) throws Exception
	{
		System.out.println(userId);
		 List<UserData> s = userData.getUserData(userId);
		 Gson gson = new Gson();
		return  gson.toJson(s);
		
	}
	
}
