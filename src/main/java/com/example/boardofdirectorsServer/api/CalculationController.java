package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Entry;

@RequestMapping("calculation")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CalculationController {

	String json;

	@PostMapping
	public String calculate(@RequestBody Entry entry) throws Exception
	{

		try {
			Calculation c = new Calculation();
			json = c.entry(entry);

			return json;

		} catch (InvalidFormatException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

	}

	@PostMapping("/lease")
	public String calculateLease(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping
	public String getCalculation(){

		return json;
	}

}
