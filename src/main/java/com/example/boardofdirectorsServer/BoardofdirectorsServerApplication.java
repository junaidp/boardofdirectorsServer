package com.example.boardofdirectorsServer;

import java.io.IOException;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.boardofdirectorsServer.api.CalculationFTA;
import com.example.boardofdirectorsServer.api.TYPESFTA;
import com.example.boardofdirectorsServer.model.Entry;
import com.google.gson.Gson;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class BoardofdirectorsServerApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication.run(BoardofdirectorsServerApplication.class, args);

	}

	private static void callFTA() throws IOException, InvalidFormatException, Exception {
		// Calculation c = new Calculation();
		Entry entry = new Entry("Lease No. 1", new Date("03/15/2016"), "Beginning", 5, 20, 20, 100000, "Yearly", 0, 0,
				25, 10, 2, 2020, 04, 1, 0, null, 0, null);
		// c.entry(entry);

		CalculationFTA c = new CalculationFTA();
		String map = c.entryFirstTimeAdoption(entry, TYPESFTA.RETROSPECTIVE, TYPESFTA.LEASE);
		Gson gson = new Gson();
		String json = gson.toJson(map);
		// System.out.println(json);
	}

}
