package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Entry;

@RequestMapping("calculation")
@RestController
public class CalculationController {
	
	@PostMapping
	public String calculate(@RequestBody Entry entry)
	{
		int ans =entry.getA() + entry.getB();
		return "Ans : "+ ans;
	}
	
//	@Autowired
//	EntryRepository entryRepository;
//	
//	@PostMapping("/saveEntry")
//	public String saveEntry(@RequestBody EntryEntity entry)
//	{
//		int ans =entry.getA() - entry.getB();
//		entryRepository.save(entry);
//		return "Ans : "+ ans;		
//	}
}
