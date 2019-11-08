package com.example.boardofdirectorsServer;

import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.boardofdirectorsServer.api.Calculation;
import com.example.boardofdirectorsServer.model.Entry;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class BoardofdirectorsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardofdirectorsServerApplication.class, args);
		try {
			try {
				Calculation c = new Calculation();
				Entry entry = new Entry("Lease No. 1", new Date("15/03/2017"), "Beginning", 5, 16, 16, 0, "Yearly", 100, 100.00, 0, 10, 2);
				c.entry(entry);
				
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
