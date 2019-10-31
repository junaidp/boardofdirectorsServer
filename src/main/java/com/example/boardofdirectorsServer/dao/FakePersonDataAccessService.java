package com.example.boardofdirectorsServer.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.boardofdirectorsServer.model.Person;


@Repository("fakeDao")
public class FakePersonDataAccessService  implements PersonDao{
	
	private static List<Person> DB = new ArrayList<>();

	@Override
	public int insertPerson(UUID id, Person person) {
		DB.add((new Person(id, person.getName())));
		return 0;
	}

	@Override
	public List<Person> selectAllPeople() {
		return DB;
	}

	@Override
	public int deletePersonById(UUID id) {
		 DB.remove(id);
		return 1;
	}

	@Override
	public int updatePersonById(UUID id) {
		return 0;
	}

	
	

}
