package com.example.boardofdirectorsServer.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.boardofdirectorsServer.model.User;


public interface UserRepository extends MongoRepository<User, String>{
	
	@Query("{ 'name' : ?0'}")
	User findUserByName(String name);


}
