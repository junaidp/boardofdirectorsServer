package com.example.boardofdirectorsServer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserRepository;
import com.google.gson.Gson;


public class UserHelper {
	
	@Autowired
	UserRepository userRepository ;
	Gson gson = new Gson();
	
	public String saveUser(User user)
	{
		userRepository.save(user);
		return "user saved";	
	}
	
	public String getUser(String userName, String password)
	{
		try {
			//	User user = userRepository.findById("1").orElse(new User());
				System.out.println();
				User user = userRepository.findUserByName(userName);
				
				System.out.println(user);
				
				String json = gson.toJson(user);
				System.out.println(user.getName());
				System.out.println(user.getUserId());
				System.out.println(user.getCreationDate());
				
				return json;
				}catch(Exception ex)
				{
					System.out.println("Error is :"+ ex.getMessage());
					throw ex;
				}
	}

	public String getAllUsers() {
		
		List<User> users = userRepository.findAll();
		String jsonUsers = gson.toJson(users);
		return jsonUsers;
	}

}
