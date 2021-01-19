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
			if (!checkUserEmailAlreadyExists(user)) {
				// Account will be always InActive , Unless verified by Admin
				user.setActive(false);
				if (user.getCompanyId() != 0) {
					user.setActive(true);
				}
				userRepository.save(user);
				// Send Verification Email to Admin
				if (user.getCompanyId() != 0) {
					saveResponce = "Success:User created successfully.";

				} else {
					// in this we will user email of main admin
					sendVerificationEmailToAdmin(user.getUserId());
					saveResponce = "Success:Please wait for the approval of account by admin:You will receive email once your account is activated.";
				}
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
			if (!checkCompanyEmailAlreadyExists(company)) {
				// by default active set to false unless verified by admin
				company.setActive(false);
				companyRepository.save(company);

				saveResponce = "Success:Please wait for the approval of account by admin:You will receive email once your account is activated.";
				sendVerificationEmailToAdminForCompany(company.getCompanyId());
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
		Boolean emailExists = false;
		try {

			List<User> users = userRepository.findAll();

			for (User userEntity : users) {
				if (userEntity.getEmail() != null) {
					if (userEntity.getEmail().equals(user.getEmail())) {
						emailExists = true;
						break;
					} else {
						emailExists = false;
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
						emailExists = true;
					} else {
						emailExists = false;
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
		String url = utility.approveRequestUrl + userId + "";

		int companyId = user.getCompanyId();
		if (user.getCompanyId() == 0) {

			String content = "<a href='" + url + "'> Activate Account </a>";
			String message = "Please click the link to activate the Account for user: " + user.getName() + "<br>";
			// willl send verification email to main admin
			utility.sendEmail(message + content, "hassanarif.isb@gmail.com", "hamzariaz1994@gmail.com",
					"Account Verification for E2L");
		} else {
			queryCompany.addCriteria(Criteria.where("companyId").is(companyId));
			Company company = mongoOperation.findOne(queryCompany, Company.class);
			String content = "<a href='" + url + "'> Activate Account </a>";
			String message = "Please click the link to activate the Account for company: " + company.getName() + "<br>";

			utility.sendEmail(message + content, "hassanarif.isb@gmail.com", "hamzariaz1994@gmail.com",
					"Account Verificationf or E2L");
		}

		// utility.sendEmail(message + content, company.getEmail(),
		// "junaidp@gmail.com", "Account Verificationf or E2L");

		String json = gson.toJson(user);

		return json;

	}

	public String sendVerificationEmailToAdminForCompany(int companyId) {
		Query query = new Query();
		Query queryCompany = new Query();

		query.addCriteria(Criteria.where("companyId").is(companyId));
		Company company = mongoOperation.findOne(query, Company.class);

		String url = utility.approveRequestUrlCompany + companyId + "";

		String content = "<a href='" + url + "'> Activate Account </a>";
		String message = "Please click the link to activate the Account for company: " + company.getName() + "<br>";
		// will use email here for main admin forverification
		utility.sendEmail(message + content, "hassanarif.isb@gmail.com", "junaidp@gmail.com",
				"Account Verificationf or E2L");
		// utility.sendEmail(message + content, company.getEmail(),
		// "junaidp@gmail.com", "Account Verificationf or E2L");

		String json = gson.toJson(company);

		return json;

	}

	public String activateUser(int userId) {
		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("userId").is(userId));
			User user = mongoOperation.findOne(query, User.class);
			user.setActive(true);
			userRepository.save(user);

			String message = "Your email  (" + user.getEmail()
					+ " ) has been successfully verified by the admin :You may log in now ";
			// will user email of user for sending him verification message
			utility.sendEmail(message, user.getEmail(), "hassanarif.isb@gmail.com", "Account Successfully Verified");

		} catch (Exception ex) {
			return "Failed to activate account: " + ex;
		}
		return "Account activated";
	}

	public String activateCompany(int companyId) {
		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("companyId").is(companyId));
			Company company = mongoOperation.findOne(query, Company.class);
			company.setActive(true);
			companyRepository.save(company);

			String message = "Your email  (" + company.getEmail()
					+ " ) has been successfully verified by the admin :You may log in now ";

			utility.sendEmail(message, company.getEmail(), "hassanarif.isb@gmail.com", "Account Successfully Verified");

		} catch (Exception ex) {
			return "Failed to activate account: " + ex;
		}
		return "Account activated";
	}

	public String resetPasswordUser(int userId, String password) {
		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("userId").is(userId));
			User user = mongoOperation.findOne(query, User.class);
			user.setPassword(password);
			userRepository.save(user);

			// String message = "Your email (" + company.getEmail()
			// + " ) has been successfully verified by the admin :You may log in
			// now ";
			//
			// utility.sendEmail(message, "adnankhokhar451@gmail.com",
			// "hamzariaz1994@gmail.com",
			// "Account Successfully Verified");

		} catch (Exception ex) {
			return "Failure: Failed to activate account: " + ex;
		}
		return "Success: Password updated successfully. Please log in in again";
	}

	public String resetPasswordCompany(int companyId, String password) {
		try {
			Query query = new Query();

			query.addCriteria(Criteria.where("companyId").is(companyId));
			Company company = mongoOperation.findOne(query, Company.class);
			company.setPassword(password);
			companyRepository.save(company);

			// String message = "Your email (" + company.getEmail()
			// + " ) has been successfully verified by the admin :You may log in
			// now ";
			//
			// utility.sendEmail(message, "adnankhokhar451@gmail.com",
			// "hamzariaz1994@gmail.com",
			// "Account Successfully Verified");

		} catch (Exception ex) {
			return "Failure: failed to reset Company,s Password: " + ex;
		}
		return "Success: Password updated successfully. Please log in in again";
	}

	public String sendResetPasswordEmail(String email) {

		boolean emailExist = false;
		String response;

		User userObj = new User();
		userObj.setEmail(email);
		emailExist = checkUserEmailAlreadyExists(userObj);
		if (emailExist) {
			Query query = new Query();

			query.addCriteria(Criteria.where("email").is(email));
			User user = mongoOperation.findOne(query, User.class);

			String url = utility.resetPassword + user.getUserId() + "";
			String content = "<a href='" + url + "'> Reset Password </a>";
			String message = "Please click the link to reset your password: " + user.getName() + "<br>";

			utility.sendEmail(message + content, user.getEmail(), "hassanarif.isb@gmail.com", "Reset Account Password");
		} else {
			Company companyObj = new Company();
			companyObj.setEmail(email);
			emailExist = checkCompanyEmailAlreadyExists(companyObj);
			if (emailExist) {
				Query query = new Query();

				query.addCriteria(Criteria.where("email").is(email));
				Company company = mongoOperation.findOne(query, Company.class);

				// String url = utility.resetPassword + user.getUserId() + "";
				String url = utility.resetPasswordCompany + company.getCompanyId() + "";
				String content = "<a href='" + url + "'> Reset  Password </a>";
				String message = "Please click the link to reset your password: " + company.getName() + "<br>";

				utility.sendEmail(message + content, company.getEmail(), "hassanarif.isb@gmail.com",
						"Reset Account Password");

			}
		}

		return "Success: A link is sent to your email address for reset password.Please log in to email address ";

	}

}
