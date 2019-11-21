package com.example.boardofdirectorsServer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.boardofdirectorsServer.api.UserHelper;

@SpringBootTest
class BoardofdirectorsServerApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testGetUser() {
		UserHelper  user = new UserHelper();
		String j = user.getUserWithId("11");
		System.out.println(j);
	}

}
