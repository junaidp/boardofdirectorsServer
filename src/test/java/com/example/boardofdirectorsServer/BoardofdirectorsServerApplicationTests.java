package com.example.boardofdirectorsServer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.boardofdirectorsServer.api.User;

@SpringBootTest
class BoardofdirectorsServerApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testGetUser() {
		User user = new User();
		user.getUser("hello", "test");
	}

}
