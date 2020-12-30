package com.example.boardofdirectorsServer.helper;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.example.boardofdirectorsServer.model.Company;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.repository.CompanyReprository;
import com.example.boardofdirectorsServer.repository.UserRepository;
import com.google.gson.Gson;

@Component
public class UserHelper {

	@Autowired
	UserRepository userRepository;
	@Autowired
	CompanyReprository companyRepository;
	@Autowired
	MongoOperations mongoOperation;
	@Autowired
	Utility utility;

	Gson gson = new Gson();

	public String saveUser(User user) {
		String saveResponce;
		try {
			// setPaymentSchedule(user);
			System.out.println("Saving user" + user);
			if (checkUserEmailAlreadyExists(user)) {
				// Account will be always InActive , Unless verified by Admin
				user.setActive(false);
				userRepository.save(user);
				// Send Verification Email to Admin
				sendVerificationEmailToAdmin(user.getUserId());
				saveResponce = "Success:Please wait for the approval of account by admin:You will receive email once your account is activated.";
			} else {
				saveResponce = "Failure: Sorry User with this email Already Exists!";

			}
			return saveResponce;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public String saveCompany(Company company) {
		String saveResponce;
		try {
			// setPaymentSchedule(user);
			System.out.println(company);
			if (checkCompanyEmailAlreadyExists(company)) {
				companyRepository.save(company);

				saveResponce = "Success: Company Saved Successfully";
			} else {
				saveResponce = "Failure: Sorry Company with this email Already Exists!";

			}

			return saveResponce;
		} catch (Exception ex) {
			throw ex;
		}
	}

	public User getUser(String name, String password) {
		try {

			System.out.println("{ name : '" + name + "'}");
			System.out.println("{ password : '" + password + "'}");
			Query query = new Query();

			if (name == "All") {
				query.addCriteria(Criteria.where("email").is(name));
			}
			if (password == "All") {
				query.addCriteria(Criteria.where("password").is(password));
			}
			query.addCriteria(Criteria.where("email").is(name).and("password").is(password));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			User user = mongoOperation.findOne(query, User.class);
			System.out.println(user);

			String json = gson.toJson(user);

			return user;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
	}

	public Company getCompany(String name, String password) {
		try {

			System.out.println("{ name : '" + name + "'}");
			System.out.println("{ password : '" + password + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("email").is(name).and("password").is(password));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			Company company = mongoOperation.findOne(query, Company.class);
			System.out.println(company);

			String json = gson.toJson(company);

			return company;
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

	public String getCompanyUsers(String companyId) {
		try {
			String jsonUsers = null;
			int comId = Integer.parseInt(companyId);
			System.out.println("{ companyId : '" + companyId + "'}");
			System.out.println("{ Mongooperation: '" + mongoOperation + "'}");
			Query query = new Query();
			query.addCriteria(Criteria.where("companyId").is(comId));
			// BasicQuery query1 = new BasicQuery("{ name : '"+name+"'} , {
			// password: '"+password+"'}");
			System.out.println("ff");
			List<User> companyUsers = mongoOperation.find(query, User.class);
			jsonUsers = gson.toJson(companyUsers);
			System.out.println(companyUsers);
			if (companyUsers == null)
				return null;
			return jsonUsers;
			// String json = gson.toJson(userdata);
			// return json;
		} catch (Exception ex) {
			System.out.println("Error is :" + ex.getMessage());
			throw ex;
		}
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

	public int getAvaiablaeCompanyId() {
		Long total = companyRepository.count();
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

	public String deleteAllCompanies() {
		try {
			companyRepository.deleteAll();
			return "companies deleted";
		} catch (Exception ex) {
			throw ex;
		}
	}

	public Boolean checkUserEmailAlreadyExists(User user) {
		System.out.println("in Checkuseremailexist");
		Boolean emailExists = true;
		try {

			List<User> users = userRepository.findAll();

			for (User userEntity : users) {
				if (userEntity.getEmail() != null) {
					if (userEntity.getEmail().equals(user.getEmail())) {
						emailExists = false;
						break;
					} else {
						emailExists = true;
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return emailExists;
	}

	public Boolean checkCompanyEmailAlreadyExists(Company company) {
		System.out.println("in Checkuseremailexist");
		Boolean emailExists = false;
		try {

			List<Company> companies = companyRepository.findAll();

			for (Company companyEntity : companies) {
				if (companyEntity.getEmail() != null) {
					if (companyEntity.getEmail().equals(company.getEmail())) {
						emailExists = false;
					} else {
						emailExists = true;
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return emailExists;
	}

	public String sendVerificationEmailToAdmin(int userId) {
		Query query = new Query();
		Query queryCompany = new Query();

		query.addCriteria(Criteria.where("userId").is(userId));
		User user = mongoOperation.findOne(query, User.class);

		// queryCompany.addCriteria(Criteria.where("companyId").is(user.getCompanyId()));
		// Company company = mongoOperation.findOne(queryCompany,
		// Company.class);
		// company.getEmail()
		// System.out.println("Sending Verification Email to: " +
		// company.getEmail() + "For User : " + user.getName());

		// String url = utility.mainUrl + "/users/activateUser?userId=" + userId
		// + "";
		String url = utility.approveRequestUrl + userId + "";

		String content = "<a href='" + url + "'> Activate Account </a>";
		String message = "Please click the link to activate the Account for user: " + user.getName() + "<br>";

		utility.sendEmail(message + content, "adnankhokhar451@gmail.com", "junaidp@gmail.com",
				"Account Verificationf or E2L");
		// utility.sendEmail(message + content, company.getEmail(),
		// "junaidp@gmail.com", "Account Verificationf or E2L");

		String json = gson.toJson(user);

		return json;

	}

	public String activateUser(int userId) {
		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("userId").is(userId));
			User user = mongoOperation.findOne(query, User.class);
			user.setActive(true);
			userRepository.save(user);
		} catch (Exception ex) {
			return "Failed to activate account: " + ex;
		}
		return "Account activated";
	}

}
