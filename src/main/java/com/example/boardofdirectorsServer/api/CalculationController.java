package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Entry;

@RequestMapping("calculation")
@RestController
public class CalculationController {
	
	@PostMapping
	public HashMap<String, HashMap<String, String>> calculate(@RequestBody Entry entry)
	{
		int ans =entry.getA() + entry.getB();
		Calculation c;
		try {
			c = new Calculation();
			return c.calculate();
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  null;
		
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
