package com.example.boardofdirectorsServer.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.boardofdirectorsServer.model.Person;


@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

	@Override
	public int insertPerson(UUID id, Person person) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Person> selectAllPeople() {
		//return List.of(new Person(UUID.randomUUID(), "FROM POSTGRES DB"));
		return null;
	}

	@Override
	public int deletePersonById(UUID id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updatePersonById(UUID id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
