package com.example.boardofdirectorsServer;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.boardofdirectorsServer.api.CalculationController;
import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.helper.UserHelper;
import com.example.boardofdirectorsServer.model.Entry;
import com.example.boardofdirectorsServer.model.UserData;

@SpringBootTest
class BoardofdirectorsServerApplicationTests {
	
	@Autowired UserHelper user;
	@Autowired DataHelper userData;
	@Autowired CalculationController c;
	

	//@Test
	void contextLoads() {
	}
	
	//@Test
	void testGetUserById() {
		
		String j = user.getUserWithId("11");
		System.out.println(j);
	}

	
	//@Test
	void testGetUser() {
		String j = user.getUser("shehryar", "password1");
		System.out.println(j);
	}
	
	@Test
	void testGetUserData() {
		List<UserData> j = userData.getUserData("1144");
		System.out.println("UserData for 1144"+ j);
	}
	
	@Test
	void testGetJournalSum(){
		Entry entry = new Entry("Lease No. 1", new Date("01/01/2020"), "Beginning", 5, 40, 2, 2670000, "Quarterly", 0, 1000000, 10, 30, 10, 2022, 01, 1133);
		
		try {
			c.calculateJournalYearlySum(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	
}
