package com.example.boardofdirectorsServer.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.model.Entry;

@RequestMapping
@RestController
public class CalculationController {
	
	@PostMapping
	public String calculate(@RequestBody Entry entry)
	{
		return "Ans : "+ entry.getA() + entry.getB();
	}
}
