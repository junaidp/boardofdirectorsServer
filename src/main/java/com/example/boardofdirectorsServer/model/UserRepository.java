package com.example.boardofdirectorsServer.model;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface UserRepository extends MongoRepository<UserEntity, String>{
	
	@Query("{'name : ?0'}")
	UserEntity findUserByName(String name);

}
