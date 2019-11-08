package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Entry;

@RequestMapping("calculation")
@RestController
public class CalculationController {
	
	public  LinkedHashMap<String, LinkedHashMap<String, String>> mapAnswer ;
	
	@PostMapping
	public LinkedHashMap<String, LinkedHashMap<String, String>> calculate(@RequestBody Entry entry)
	{
		
		try {
			Calculation c = new Calculation();
			LinkedHashMap map = c.entry(entry);
			mapAnswer = map;
			return map;
			
		} catch (InvalidFormatException e) {
			 e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  null;
		
	}
	
	@GetMapping
	public LinkedHashMap<String, LinkedHashMap<String, String>> getCalculation(){
		if(mapAnswer != null)
		return mapAnswer;
		return null;
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
