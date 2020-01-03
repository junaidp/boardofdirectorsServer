package com.example.boardofdirectorsServer;

import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.boardofdirectorsServer.api.Calculation;
import com.example.boardofdirectorsServer.api.Constants;
import com.example.boardofdirectorsServer.api.TYPES;
import com.example.boardofdirectorsServer.model.Entry;
import com.google.gson.Gson;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class BoardofdirectorsServerApplication {

	public static void main(String[] args) throws Exception {
	
		SpringApplication.run(BoardofdirectorsServerApplication.class, args);
			
		try {
			try {
			//	Calculation c = new Calculation();
				Entry entry = new Entry("Lease No. 1", new Date("10/05/2020"), "Beginning", 5, 2, 2, 2670000, "Yearly", 0, 1000000, 10, 30, 10, 2021, 01);
			//	c.entry(entry);
				
				Calculation c = new Calculation();
				String map = c.entryJournal(entry, TYPES.JOURNAL_YEARLY, TYPES.LEASE_YEARLY);
				Gson gson = new Gson(); 
				String json = gson.toJson(map);
			//	System.out.println(json);
				
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
