package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.UserHelper;
import com.example.boardofdirectorsServer.model.Company;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserTest;
import com.google.gson.Gson;

@RequestMapping("/users")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

	@Autowired
	UserHelper user;

	Gson gson = new Gson();

	@PostMapping("/saveUser")
	public String saveUser(@RequestBody User userEntity)

	{
		// userEntity.setContactNumber(Integer.parseInt(userEntity.getContactNumber()));
		userEntity.setUserId(user.getAvaiablaeUserId());
		if (userEntity.getUserType().equals("individual")) {

			return user.saveUser(userEntity);
		} else {
			Company company = new Company();
			company.setName(userEntity.getName());
			company.setCompanyId(user.getAvaiablaeCompanyId());
			company.setEmail(userEntity.getEmail());
			company.setCity(userEntity.getCity());
			company.setCompanyAddress(userEntity.getCompanyAddress());
			company.setContactNumber(userEntity.getContactNumber());
			company.setCreationDate(userEntity.getCreationDate());
			company.setCurrency(userEntity.getCurrency());
			company.setPassword(userEntity.getPassword());
			company.setPaymentSchedule(userEntity.getPaymentSchedule());
			company.setUserType(userEntity.getUserType());

			return user.saveCompany(company);
		}

	}

	@PostMapping("/signIn")
	public String singIn(@RequestBody UserTest userTest) throws Exception {
		String loggedInCredentials;
		System.out.println(userTest.getName() + "," + userTest.getPassword());
		User loggedInUser = user.getUser(userTest.getName(), userTest.getPassword());
		// if (loggedInUser != null) {
		// if (loggedInUser.isActive() == false) {
		// return loggedInCredentials = "Activation Alert: You are not yet
		// verified by the admin";
		// }
		// }

		loggedInCredentials = gson.toJson(loggedInUser);

		if (loggedInCredentials.equals("null")) {
			Company loggedInCompany = user.getCompany(userTest.getName(), userTest.getPassword());
			// if (loggedInCompany != null) {
			// if (loggedInCompany.isActive() == false) {
			// return loggedInCredentials = "Activation Alert: You are not yet
			// verified by the admin";
			// }
			// }
			return loggedInCredentials = gson.toJson(loggedInCompany);
		} else {
			return loggedInCredentials;
		}

	}

	@GetMapping("/getAllUsers")
	public String getAllUsers() {
		return user.getAllUsers();
	}

	@GetMapping("/getCompanyUsers")
	public String getCompanyUsers(@RequestParam String companyId) throws Exception {
		return user.getCompanyUsers(companyId);
	}

	@GetMapping("/getUserWithId")
	public String getUserWithId(@RequestParam String userId) throws Exception {
		return user.getUserWithId(userId);
	}

	@GetMapping("/deleteAllUsers")
	public String deleteAllUsers() throws Exception {
		return user.deleteAll();
	}

	@GetMapping("/deleteAllCompanies")
	public String deleteAllCompanies() throws Exception {
		return user.deleteAllCompanies();
	}

	// Not to be called from controller..
	// This will be from Inside SignUp Method (after user account Created with
	// status:notActive)
	@GetMapping("/sendVerificationEmailToAdmin")
	public String sendVerificationEmailToAdmin(@RequestParam int userId) throws Exception {
		return user.sendVerificationEmailToAdmin(userId);
	}

	@PostMapping("/activateUser")
	public String activateUser(@RequestParam int userId) throws Exception {
		return user.activateUser(userId);
	}

	@PostMapping("/activateCompany")
	public String activateCompany(@RequestParam int companyId) throws Exception {
		return user.activateCompany(companyId);
	}

	@GetMapping("/resetPasswordEmail")
	public String resetPasswordEmail(@RequestParam String email) throws Exception {
		return user.sendResetPasswordEmail(email);
	}

	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody User userEntity) throws Exception {
		// return user.activateUser(userId);
		String s;
		return user.resetPasswordUser(userEntity.getUserId(), userEntity.getPassword());
	}

	@PostMapping("/resetPasswordCompany")
	public String resetPasswordCompany(@RequestBody Company companyEntity) throws Exception {
		// return user.activateUser(userId);
		String s;
		return user.resetPasswordCompany(companyEntity.getCompanyId(), companyEntity.getPassword());
	}

}
