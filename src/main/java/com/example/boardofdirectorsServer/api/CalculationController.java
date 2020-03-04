package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boardofdirectorsServer.helper.DataHelper;
import com.example.boardofdirectorsServer.model.Entry;
import com.example.boardofdirectorsServer.model.UserData;
import com.google.gson.Gson;

@RequestMapping("calculation")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CalculationController {

	@Autowired
	DataHelper dataHelper;
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

	@PostMapping("/lease/yearly")
	public String calculateLeaseYearly(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry, Constants.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/lease/quarterly")
	public String calculateLeaseQuarterly(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry, Constants.LEASE_QUARTERLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/lease/monthly")
	public String calculateLeaseMonthly(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry, Constants.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/journal/yearly")
	public String calculateJournalYearly(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryJournal(entry, TYPES.JOURNAL_YEARLY, TYPES.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/journal/yearlySum")
	public String calculateJournalYearlySum(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			
			///
			Gson gson = new Gson();
			int userId = entry.getUserId();
			System.out.println("calling getuser data");
			List<UserData> dataList = dataHelper.getUserData(userId);
			System.out.println("back from data:"+ dataList);
			LinkedHashMap<String, LinkedHashMap<String, String>> mapFinal = new LinkedHashMap<String, LinkedHashMap<String, String>>();
			
			///
			double dr = 0;
			for(UserData userData: dataList)
			{
				Entry entryc = new Entry();
				copyData(userData, entryc);
				entryc.setYear(entry.getYear());
				entryc.setMonth(entry.getMonth());
				System.out.println("USE DATA:" + userData.getCommencementDate()+":"+userData.getAnnualDiscountRate()+":"+userData.getEscalation());
				System.out.println("calling entryJournal"+ entryc.getAnnualDiscountRate()+":"+ entryc.getCommencementDate()+":"+ entryc.getEscalation());
				json = c.entryJournal(entryc, TYPES.JOURNAL_YEARLY, TYPES.LEASE_YEARLY);
				System.out.println("converting");
				
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> map = gson.fromJson(json, LinkedHashMap.class);
			//	map.put("commencementDate", entry.getCommencementDate()+"");
			//	map.put("paymentInterval", entry.getPaymentIntervals());
			//	map.put("paymentsAt", entry.getPaymentsAt());
				System.out.println("converted");
				map.put("commencementDate", entryc.getCommencementDate()+"");
				map.put("paymentsAt", entryc.getPaymentsAt());
				map.put("paymentIntervals", entryc.getPaymentIntervals());
				mapFinal.put(userData.getDataId(), map);
				
			}
			
			System.out.println("retruning"+ mapFinal);
			
			return gson.toJson(mapFinal);
		} catch (Exception e) {
			throw e;
		}
	}

	
	private void copyData(UserData s, Entry t) {
		
		t.setAnnualDiscountRate(s.getAnnualDiscountRate());
		t.setCommencementDate(s.getCommencementDate());
		t.setEscalation(s.getEscalation());
		t.setEscalationAfterEvery(s.getEscalationAfterEvery());
		t.setExpectedPeriod(s.getExpectedPeriod());
		t.setGuaranteedResidualValue(s.getGuaranteedResidualValue());
		t.setInitialDirectCost(s.getInitialDirectCost());
		t.setLeaseContractNo(s.getLeaseContractNo());
		t.setLeasePayment(s.getLeasePayment());
		t.setLeaseTerm(s.getLeaseTerm());
		t.setPaymentIntervals(s.getPaymentIntervals());
		t.setPaymentsAt(s.getPaymentsAt());
		t.setUsefulLifeOfTheAsset(s.getUsefulLifeOfTheAsset());
		t.setUserId(s.getUserId());
		
		
	}

	@PostMapping("/journal/quarterly")
	public String calculateJournalQuarterly(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryJournal(entry, TYPES.JOURNAL_QUARTERLY, TYPES.LEASE_QUARTERLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/journal/monthly")
	public String calculateJournalMonthly(@RequestBody Entry entry) throws Exception
	{
		try {
			Calculation c = new Calculation();
			json = c.entryJournal(entry, TYPES.JOURNAL_MONTHLY, TYPES.LEASE_MONTHLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/journal/fta")
	public String calculateJournalFirstTimeAdoption(@RequestBody Entry entry) throws Exception
	{
		try {
			CalculationFTA c = new CalculationFTA();
			json = c.entryFirstTimeAdoption(entry, TYPESFTA.RETROSPECTIVE, TYPESFTA.LEASE);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/fta/lease")
	public String calculateFTALease(@RequestBody Entry entry) throws Exception
	{
		try {
			CalculationFTA c = new CalculationFTA();
			json = c.entryFirstTimeAdoption(entry, TYPESFTA.LEASE, TYPESFTA.LEASE);
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
