package com.example.boardofdirectorsServer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.helper.UserHelper;

@SpringBootTest
class BoardofdirectorsServerApplicationTests {
	
	@Autowired UserHelper user;
	@Autowired DataHelper userData;

	@Test
	void contextLoads() {
	}
	
	@Test
	void testGetUserById() {
		
		String j = user.getUserWithId("11");
		System.out.println(j);
	}

	
	@Test
	void testGetUser() {
		String j = user.getUser("shehryar", "password1");
		System.out.println(j);
	}
	
	@Test
	void testGetUserData() {
		String j = userData.getUserData("1135");
		System.out.println(j);
	}
}
