package com.example.boardofdirectorsServer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.boardofdirectorsServer.api.UserHelper;

@SpringBootTest
class BoardofdirectorsServerApplicationTests {
	
	@Autowired UserHelper user;

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
		String j = user.getUser("shehryar", "password");
		System.out.println(j);
	}
}
