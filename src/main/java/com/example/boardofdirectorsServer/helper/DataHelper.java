package com.example.boardofdirectorsServer.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


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

	public List<UserData> getUserData(String userId)
	{
		try {
			
			System.out.println("{ userId : '"+userId+"'}");
			System.out.println("{ Mongooperation: '"+mongoOperation+"'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(userId));
			//	BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , { password: '"+password+"'}");
			System.out.println("ff");
			List<UserData> userdata = mongoOperation.find(query, UserData.class);
			System.out.println(userdata);
			if(userdata == null)
				return null;
			return userdata;
			//String json = gson.toJson(userdata);
			//return json;
		}catch(Exception ex)
		{
			System.out.println("Error is :"+ ex.getMessage());
			throw ex;
		}
	}


}
