package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Entry;
import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserRepository;
import com.google.gson.Gson;

@RequestMapping("calculation")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CalculationController {

	String json;

	@PostMapping
	public String calculate(@RequestBody Entry entry) throws Exception
	{

		try {
			Calculation c = new Calculation();
			json = c.entry(entry);

			return json;

		} catch (InvalidFormatException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

	}

	@PostMapping("/lease")
	public String calculateLease(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping
	public String getCalculation(){

		return json;
	}

	@Autowired
	UserRepository userRepository;

	@PostMapping("/saveUser")
	public String saveUser(@RequestBody User user)
	{

		userRepository.save(user);
		return "user saved";		
	}
	
	@GetMapping("/getUser")
	public String singIn(@RequestBody String userName, String password) throws Exception
	{
		try {
		User user = userRepository.findById("1").orElse(new User());
		
		System.out.println(user);
		Gson gson = new Gson();
		json = gson.toJson(user);
		return json;
		}catch(Exception ex)
		{
			throw ex;
		}
		
	}
	
}
