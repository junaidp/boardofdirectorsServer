package com.example.boardofdirectorsServer.helper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.repository.UserRepository;
import com.google.gson.Gson;

@Component
public class UserHelper {

	@Autowired
	UserRepository userRepository;
	@Autowired
	MongoOperations mongoOperation;
	Gson gson = new Gson();

	public String saveUser(User user) {
		try {
			// setPaymentSchedule(user);
			System.out.println("Saving user" + user);
			userRepository.save(user);
			return "user saved";
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String getUser(String name, String password) {
		try {

			System.out.println("{ name : '" + name + "'}");
			System.out.println("{ password : '" + password + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			User user = mongoOperation.findOne(query, User.class);
			System.out.println(user);

			String json = gson.toJson(user);

			return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public String getAllUsers() {
		System.out.println("in get all users");
		String jsonUsers = null;
		try {
			List<User> users = userRepository.findAll();
			jsonUsers = gson.toJson(users);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return jsonUsers;
	}

	public String getUserWithPrimaryId(String userId) {
		String userJson;
		try {
			Optional<User> user = userRepository.findById(userId);
			userJson = gson.toJson(user);
		} catch (Exception ex) {
			throw ex;
		}
		return userJson;
	}

	public String getUserWithId(String userId) {
		String userJson;
		try {
			Query query = new Query();
			int id = Integer.parseInt(userId);
			query.addCriteria(Criteria.where("userId").is(id));
			User user = mongoOperation.findOne(query, User.class);
			userJson = gson.toJson(user);
		} catch (Exception ex) {
			throw ex;
		}
		return userJson;
	}

	public int getAvaiablaeUserId() {
		Long total = userRepository.count();
		int count = total.intValue();
		return count + 1;

	}

	public String deleteAll() {
		try {
			userRepository.deleteAll();
			return "user's deleted";
		} catch (Exception ex) {
			throw ex;
		}
	}

}
