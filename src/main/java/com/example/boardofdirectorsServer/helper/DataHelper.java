package com.example.boardofdirectorsServer.helper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;
import com.example.boardofdirectorsServer.repository.UserDataRepository;
import com.example.boardofdirectorsServer.repository.UserRepository;
import com.google.gson.Gson;

@Component
public class DataHelper {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserDataRepository userDataRepository;
	@Autowired
	MongoOperations mongoOperation;
	Gson gson = new Gson();

	public String saveData(UserData data) {
		try {

			User u = new User();
			u.setCity("city");
			u.setPaymentSchedule("trial");
			u.setUserId(77);
			Boolean allow = allowSave(u, data);
			String user = getUserWithId(data.getUserId() + "");
			// Boolean allowToSave = allowSave(data.getUserId());
			if (allow) {
				userDataRepository.save(data);
				return "user's data saved";
			} else {
				return "Lease limit exceed.Please change the payment schedule to Add more Leases.";
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String deleteAllData() {
		try {
			userDataRepository.deleteAll();
			return "user's data saved";
		} catch (Exception ex) {
			throw ex;
		}
	}

	public List<UserData> getUserData(int userId) {
		try {
			System.out.println("{ userId : '" + userId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("userId").is(userId));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			List<UserData> userdata = mongoOperation.find(query, UserData.class);
			System.out.println(userdata);
			if (userdata == null)
				return null;
			return userdata;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	/*
	 * public User getUser(int userId) { try {
	 * 
	 * System.out.println("{ userId : '" + userId + "'}");
	 * System.out.println("{ Mongooperation: '" + mongoOperation + "'}"); Query
	 * query = new Query();
	 * query.addCriteria(Criteria.where("userId").is(userId)); // BasicQuery
	 * query1 = new BasicQuery("{ name : '"+name+"'} , { // password:
	 * '"+password+"'}"); System.out.println("ff"); User userData = (User)
	 * mongoOperation.find(query, User.class); System.out.println(userData); if
	 * (userData == null) return null; return userData; // String json =
	 * gson.toJson(userdata); // return json; } catch (Exception ex) {
	 * System.out.println("Error is :" + ex.getMessage()); throw ex; } }
	 */

	public String getUserWithId(String userId) {
		String userJson;
		try {
			Optional<User> user = userRepository.findById(userId);
			userJson = gson.toJson(user);
		} catch (Exception ex) {
			throw ex;
		}
		return userJson;
	}

	private boolean allowSave(User user, UserData data) {
		List<UserData> userdata = getUserData(data.getUserId());
		// String user = getUserWithId(data.getUserId() + "");
		int userCounts = userdata.size();
		int trialAllowed = 1;
		int bronzeAllowed = 10;
		int silverAllowed = 30;
		int goldAllowed = 30;

		if (user.getPaymentSchedule() == "trial" && userCounts <= trialAllowed) {
			return true;
			// user.setPassword("something");

		} else if (user.getPaymentSchedule() == "bronze" && userCounts >= bronzeAllowed) {
			return true;

		} else if (user.getPaymentSchedule() == "silver" && userCounts >= silverAllowed) {
			return true;
		} else if (user.getPaymentSchedule() == "gold") {
			return true;

		}
		return false;
	}

}
