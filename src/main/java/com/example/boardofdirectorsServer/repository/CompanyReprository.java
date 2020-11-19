package com.example.boardofdirectorsServer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.boardofdirectorsServer.model.Company;
import com.example.boardofdirectorsServer.model.User;

public interface CompanyReprository extends MongoRepository<Company, String> {

	@Query("{ 'name' : ?0'}")
	User findUserByName(String name);

}
