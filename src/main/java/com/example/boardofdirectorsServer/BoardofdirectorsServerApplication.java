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
		/*try {
			try {
				Calculation c = new Calculation();
				Entry entry = new Entry("Lease No. 1", new Date("03/15/2017"), "Beginning", 5, 2, 16, 100000, "Yearly", 0, 0.00, 10, 10, 2);
				c.entry(entry);
				
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
