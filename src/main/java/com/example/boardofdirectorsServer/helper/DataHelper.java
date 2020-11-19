package com.example.boardofdirectorsServer.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.boardofdirectorsServer.model.Company;
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

			if (data.getUserId() == 0) {
				return "Failure: Only users can add Leases";
			}
			User user = getUserWithId(data.getUserId() + "");
			Boolean allow = allowSave(user, data);
			if (allow) {
				userDataRepository.save(data);
				return "Success :user's data saved";
			} else {
				return " Failure: Lease limit exceed.Please change the payment schedule to Add more Leases.";
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

	public List<UserData> getCompanyData(int companyId) {
		try {
			System.out.println("{ companyId : '" + companyId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("companyId").is(companyId));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			List<UserData> companyData = mongoOperation.find(query, UserData.class);
			System.out.println(companyData);
			if (companyData == null)
				return null;
			return companyData;
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

	public User getUserWithId(String userId) {
		String userJson;
		User user;
		try {
			Query query = new Query();
			int id = Integer.parseInt(userId);
			query.addCriteria(Criteria.where("userId").is(id));
			user = mongoOperation.findOne(query, User.class);
			userJson = gson.toJson(user);
		} catch (Exception ex) {
			throw ex;
		}
		return user;
	}

	public Company getCompanyWithId(String userId) {
		String userJson;
		Company company;
		try {
			Query query = new Query();
			int id = Integer.parseInt(userId);
			query.addCriteria(Criteria.where("companyId").is(id));
			company = mongoOperation.findOne(query, Company.class);
			userJson = gson.toJson(company);
		} catch (Exception ex) {
			throw ex;
		}
		return company;
	}

	private boolean allowSave(User user, UserData data) {
		List<UserData> userdata;
		if (user.getCompanyId() == 0) {
			userdata = getUserData(user.getUserId());
		} else {
			userdata = getCompanyData(user.getCompanyId());
		}

		// String user = getUserWithId(data.getUserId() + "");
		int userCounts = userdata.size();
		int trialAllowed = 1;
		int bronzeAllowed = 10;
		int silverAllowed = 30;
		int goldAllowed = 100;

		if (user.getPaymentSchedule().equals("trial") && userCounts < trialAllowed) {
			return true;

		} else if (user.getPaymentSchedule().equals("bronze") && userCounts < bronzeAllowed) {
			return true;

		} else if (user.getPaymentSchedule().equals("silver") && userCounts < silverAllowed) {
			return true;
		} else if (user.getPaymentSchedule().equals("gold")) {
			return true;

		}
		return false;
	}

}
