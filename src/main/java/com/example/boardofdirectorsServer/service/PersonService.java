package com.example.boardofdirectorsServer.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.boardofdirectorsServer.dao.PersonDao;
import com.example.boardofdirectorsServer.model.Person;


@Service
public class PersonService {

	private final PersonDao personDao;
	
	@Autowired
	public PersonService(@Qualifier("fakeDao")PersonDao personDao)
	{
		this.personDao = personDao;
	}
	
	public int addPerson(Person person)
	{
		return personDao.insertPerson(person);
	}
	
	public List<Person> getAllPeople(){
		return personDao.selectAllPeople();
	}
	
	public int deletePerson(UUID id)
	{
		return personDao.deletePersonById(id);
	}
}
