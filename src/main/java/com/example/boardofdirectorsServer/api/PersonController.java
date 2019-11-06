package com.example.boardofdirectorsServer.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Person;
import com.example.boardofdirectorsServer.service.PersonService;


@RequestMapping("api/v1/person")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonController {
	
	private final PersonService personService;
	
	@Autowired
	public PersonController(PersonService personService)
	{
		this.personService = personService;
	}
	
	@PostMapping
	@CrossOrigin(origins = "*", maxAge = 3600)
	public String addPerson(@RequestBody Person person){
		personService.addPerson(person);
		return "Hello";
	}
	
	@GetMapping
	@CrossOrigin(origins = "*", maxAge = 3600)
	public List<Person> getAllPeople()
	{
		return personService.getAllPeople();
	}
	
	@DeleteMapping(path ="{id}")
	@CrossOrigin(origins = "*")
	public void deletePersonById(@PathVariable("id")UUID id){
		personService.deletePerson(id);
		
	}

}
