package com.example.boardofdirectorsServer.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.boardofdirectorsServer.model.User;
import com.example.boardofdirectorsServer.model.UserData;


public interface UserDataRepository extends MongoRepository<UserData, String>{
	
	@Query("{ 'name' : ?0'}")
	User findUserByName(String name);


}
