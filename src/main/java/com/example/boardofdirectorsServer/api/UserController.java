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

@RequestMapping("/users")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

	@Autowired
	UserHelper user;

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
		System.out.println(userTest.getName() + "," + userTest.getPassword());
		String loggedInCredentials = user.getUser(userTest.getName(), userTest.getPassword());

		if (loggedInCredentials.equals("null")) {
			return loggedInCredentials = user.getCompany(userTest.getName(), userTest.getPassword());
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

}
