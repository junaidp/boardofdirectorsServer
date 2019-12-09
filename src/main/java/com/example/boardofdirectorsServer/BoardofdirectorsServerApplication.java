package com.example.boardofdirectorsServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class BoardofdirectorsServerApplication {

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(BoardofdirectorsServerApplication.class, args);
		/*
		try {
			try {
			//	Calculation c = new Calculation();
				Entry entry = new Entry("Lease No. 1", new Date("05/10/2019"), "Ending", 7, 60, 60, 250000, "Monthly", 0, 200000, 10, 15, 3, 2019);
			//	c.entry(entry);
				
				Calculation c = new Calculation();
				String map = c.entryJournal(entry);
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
	*/
	}

}
