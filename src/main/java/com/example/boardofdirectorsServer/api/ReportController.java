package com.example.boardofdirectorsServer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.ReportHelper;
import com.example.boardofdirectorsServer.model.ReportFilterEntity;

@RequestMapping("/reports")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {

	@Autowired
	ReportHelper reportHelper;

	@GetMapping("/getReportData")
	public String getReportData(@RequestBody ReportFilterEntity reportEntity) {
		return reportHelper.getFilteredReportData(reportEntity);
	}

}
