package com.example.boardofdirectorsServer.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;
import com.example.boardofdirectorsServer.repository.UserDataRepository;
import com.google.gson.Gson;

@Component
public class DataHelper {

	@Autowired
	UserDataRepository userDataRepository ;
	@Autowired MongoOperations mongoOperation;
	Gson gson = new Gson();

	public String saveData(UserData data)
	{
		try {
			userDataRepository.save(data);
		return "user's data saved";	
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}

	public String getUser(String name, String password)
	{
		try {

			System.out.println("{ name : '"+name+"'}");
			System.out.println("{ password : '"+password+"'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
			//	BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , { password: '"+password+"'}");
			System.out.println("ff");
			User user = mongoOperation.findOne(query, User.class);
			System.out.println(user);

			String json = gson.toJson(user);

			return json;
		}catch(Exception ex)
		{
			System.out.println("Error is :"+ ex.getMessage());
			throw ex;
		}
	}


}
