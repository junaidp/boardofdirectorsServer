package com.example.boardofdirectorsServer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTest {
	
	
	private String name;
	private String password;
	
	public UserTest(@JsonProperty("name")String name, @JsonProperty("password")String password)
	{
		this.name = name;
		this.password = password;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
