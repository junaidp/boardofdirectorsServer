package com.example.boardofdirectorsServer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.boardofdirectorsServer.model.ClassOfAsset;
import com.example.boardofdirectorsServer.model.User;

public interface ClassOfAssetRepository extends MongoRepository<ClassOfAsset, String> {

	@Query("{ 'name' : ?0'}")
	User findUserByName(String name);

}
