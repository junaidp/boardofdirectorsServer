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
import com.example.boardofdirectorsServer.model.User;
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
	
	
	
	//@Test
	void testDeleteAllData() {
		 userData.deleteAllData();
		System.out.println("AllData deleted");
	}
	
	//@Test
	void testDeleteAllUsers() {
		 user.deleteAll();
		System.out.println("All users deleted");
	}
	
	//@Test
	void saveUser()
	{
		System.out.println("saveUser testcase running");
		User user1 = new User();
		user1.setCreationDate(new Date());
		user1.setName("testcaseuser");
		user1.setPassword("password");
		user1.setUserId(user.getAvaiablaeUserId());
		user.saveUser(user1);
		
		
	}
//	@Test
	void testGetUserData() {
		List<UserData> j = userData.getUserData(1145);
		System.out.println("UserData for 1145"+ j);
	}
	
	//	@Test
	void testGetJournalSum(){
		Entry entry = new Entry("Lease No. 1", new Date("01/04/2020"), "Beginning", 3, 10, 10, 2670000, "Yearly", 0, 
				1000000, 10, 30, 10, 2022, 01, 29);
		
		try {
			c.calculateJournalYearlySum(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	
	
}
