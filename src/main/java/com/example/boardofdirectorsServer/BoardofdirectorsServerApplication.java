package com.example.boardofdirectorsServer;

import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.boardofdirectorsServer.api.Calculation;
import com.example.boardofdirectorsServer.api.CalculationFTA;
import com.example.boardofdirectorsServer.api.Constants;
import com.example.boardofdirectorsServer.api.TYPES;
import com.example.boardofdirectorsServer.api.TYPESFTA;
import com.example.boardofdirectorsServer.model.Entry;
import com.google.gson.Gson;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class BoardofdirectorsServerApplication {

	public static void main(String[] args) throws Exception {
	
		SpringApplication.run(BoardofdirectorsServerApplication.class, args);
			
		try {
			try {
			callJournal();
				
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private static void callJournal() throws IOException, InvalidFormatException, Exception {
		//	Calculation c = new Calculation();
		Entry entry = new Entry("Lease No. 1", new Date("01/01/2020"), "Beginning", 5, 10, 2, 2670000, "Quarterly", 0, 1000000, 10, 30, 10, 2020, 05);
		
		//	c.entry(entry);
			
			Calculation c = new Calculation();
			String map = c.entryJournal(entry, TYPES.JOURNAL_QUARTERLY, TYPES.LEASE_QUARTERLY);
			Gson gson = new Gson(); 
			String json = gson.toJson(map);
		//	System.out.println(json);
	}
	
	private static void callFTA() throws IOException, InvalidFormatException, Exception {
		//	Calculation c = new Calculation();
			Entry entry = new Entry("Lease No. 1", new Date("03/03/2016"), "Beginning", 5, 10, 2, 2670000, "Yearly", 0, 0, 10, 30, 10, 2020, 04);
		//	c.entry(entry);
			
			CalculationFTA c = new CalculationFTA();
			String map = c.entryFirstTimeAdoption(entry, TYPESFTA.RETROSPECTIVE, TYPESFTA.LEASE);
			Gson gson = new Gson(); 
			String json = gson.toJson(map);
		//	System.out.println(json);
	}

}
