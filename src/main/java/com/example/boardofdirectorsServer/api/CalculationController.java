package com.example.boardofdirectorsServer.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String calculate(@RequestBody Entry entry) throws Exception {

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
	public String calculateLeaseYearly(@RequestBody Entry entry) throws Exception {
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry, Constants.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/lease/yearlyByDataId")
	public String calculateLeaseYearlyByDataId(@RequestParam String dataId) throws Exception {
		try {
			// int dataIdInt = Integer.parseInt(dataId);
			UserData userData = dataHelper.getUserDataByDataId(dataId);
			// if (userData == null) {
			Entry e = new Entry();
			setEntryObject(userData, e);
			// }

			Calculation c = new Calculation();
			json = c.entryLease(e, Constants.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	private void setEntryObject(UserData userData, Entry e) {
		float escalation = userData.getEscalation() / 100;
		float annuadlDiscountRate = userData.getAnnualDiscountRate() / 100;
		round(annuadlDiscountRate, 2);
		round(escalation, 2);

		e.setAnnualDiscountRate(annuadlDiscountRate);
		e.setCommencementDate(userData.getCommencementDate());
		e.setCompanyId(userData.getCompanyId());
		e.setUserId(userData.getUserId());
		e.setEscalation(escalation);
		e.setEscalationAfterEvery(userData.getEscalationAfterEvery());
		e.setExpectedPeriod(userData.getExpectedPeriod());
		e.setGuaranteedResidualValue(userData.getGuaranteedResidualValue());
		e.setInitialDirectCost(userData.getInitialDirectCost());
		e.setLeaseContractNo(userData.getLeaseContractNo());
		e.setLeasePayment(userData.getLeasePayment());
		e.setLeaseTerm(userData.getLeaseTerm());
		e.setPaymentIntervals(userData.getPaymentIntervals());
		e.setPaymentsAt(userData.getPaymentsAt());

	}

	@PostMapping("/lease/quarterly")
	public String calculateLeaseQuarterly(@RequestBody Entry entry) throws Exception {
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry, Constants.LEASE_QUARTERLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/lease/monthly")
	public String calculateLeaseMonthly(@RequestBody Entry entry) throws Exception {
		try {
			Calculation c = new Calculation();
			json = c.entryLease(entry, Constants.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/journal/yearly")
	public String calculateJournalYearly(@RequestBody Entry entryc) throws Exception {
		try {
			Calculation c = new Calculation();
			System.out.println("Values going For SUM  are:" + entryc.getCommencementDate() + "::"
					+ entryc.getPaymentsAt() + "::" + entryc.getAnnualDiscountRate() + "::" + entryc.getLeaseTerm()
					+ "::" + entryc.getExpectedPeriod() + "::" + entryc.getLeasePayment() + "::"
					+ entryc.getPaymentIntervals() + "::" + entryc.getInitialDirectCost() + "::"
					+ entryc.getGuaranteedResidualValue() + "::" + entryc.getUsefulLifeOfTheAsset() + "::"
					+ entryc.getEscalation() + "::" + entryc.getEscalationAfterEvery() + "::");

			json = c.entryJournal(entryc, TYPES.JOURNAL_YEARLY, TYPES.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/journal/yearlySum")
	public String calculateJournalYearlySum(@RequestBody Entry entry) throws Exception {
		try {
			Calculation c = new Calculation();
			List<UserData> dataList;
			///
			Gson gson = new Gson();
			if (entry.getCompanyId() == 0) {
				int userId = entry.getUserId();
				System.out.println("calling getuser data");
				dataList = dataHelper.getUserData(userId);

			} else {
				int companyId = entry.getCompanyId();
				dataList = dataHelper.getCompanyData(companyId);
			}

			System.out.println("back from data:" + dataList);
			LinkedHashMap<String, LinkedHashMap<String, String>> mapFinal = new LinkedHashMap<String, LinkedHashMap<String, String>>();

			///
			double dr = 0;
			for (UserData userData : dataList) {
				Entry entryc = new Entry();
				copyData(userData, entryc);
				entryc.setYear(entry.getYear());
				entryc.setMonth(entry.getMonth());
				System.out.println("USE DATA:" + userData.getCommencementDate() + ":" + userData.getAnnualDiscountRate()
						+ ":" + userData.getEscalation());
				System.out.println("calling entryJournal" + entryc.getAnnualDiscountRate() + ":"
						+ entryc.getCommencementDate() + ":" + entryc.getEscalation());
				System.out.println("Values going For SUM  are:" + entryc.getCommencementDate() + "::"
						+ entryc.getPaymentsAt() + "::" + entryc.getAnnualDiscountRate() + "::" + entryc.getLeaseTerm()
						+ "::" + entryc.getExpectedPeriod() + "::" + entryc.getLeasePayment() + "::"
						+ entryc.getPaymentIntervals() + "::" + entryc.getInitialDirectCost() + "::"
						+ entryc.getGuaranteedResidualValue() + "::" + entryc.getUsefulLifeOfTheAsset() + "::"
						+ entryc.getEscalation() + "::" + entryc.getEscalationAfterEvery() + "::");

				if (entryc.getPaymentIntervals().equalsIgnoreCase("Yearly")) {
					json = c.entryJournal(entryc, TYPES.JOURNAL_YEARLY, TYPES.LEASE_YEARLY);
				} else if (entryc.getPaymentIntervals().equalsIgnoreCase("Monthly")) {
					json = c.entryJournal(entryc, TYPES.JOURNAL_MONTHLY, TYPES.LEASE_MONTHLY);
				} else if (entryc.getPaymentIntervals().equalsIgnoreCase("Quarterly")) {
					json = c.entryJournal(entryc, TYPES.JOURNAL_QUARTERLY, TYPES.LEASE_QUARTERLY);
				}

				System.out.println("result is +" + json);

				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> map = gson.fromJson(json, LinkedHashMap.class);
				// map.put("commencementDate", entry.getCommencementDate()+"");
				// map.put("paymentInterval", entry.getPaymentIntervals());
				// map.put("paymentsAt", entry.getPaymentsAt());
				System.out.println("converted");
				map.put("commencementDate", entryc.getCommencementDate() + "");
				map.put("paymentsAt", entryc.getPaymentsAt());
				map.put("paymentIntervals", entryc.getPaymentIntervals());
				map.put("leaseName", userData.getLeaseName());
				map.put("lessorName", userData.getLessorName());
				map.put("referenceNo", userData.getLeaseContractNo());
				map.put("classOfAsset", userData.getClassOfAsset());
				map.put("id", userData.getId() + "");

				// map.put("payment", value)

				mapFinal.put(userData.getDataId(), map);

			}

			System.out.println("retruning" + mapFinal);

			return gson.toJson(mapFinal);
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/journal/reportFtaSum")
	public String calculateReportFtaSum(@RequestBody Entry entry) throws Exception {
		try {

			CalculationFTA c = new CalculationFTA();
			// json = c.entryFirstTimeAdoption(entry, TYPESFTA.LEASE,
			// TYPESFTA.LEASE);
			// return json;
			// Calculation c = new Calculation();
			List<UserData> dataList;
			///
			Gson gson = new Gson();
			if (entry.getCompanyId() == 0) {
				int userId = entry.getUserId();
				System.out.println("calling getuser data");
				dataList = dataHelper.getUserData(userId);

			} else {
				int companyId = entry.getCompanyId();
				dataList = dataHelper.getCompanyData(companyId);
			}

			System.out.println("back from data:" + dataList);
			LinkedHashMap<String, LinkedHashMap<String, String>> mapFinal = new LinkedHashMap<String, LinkedHashMap<String, String>>();

			///
			double dr = 0;
			for (UserData userData : dataList) {
				Entry entryc = new Entry();
				copyData(userData, entryc);
				json = c.entryFirstTimeAdoption(entryc, TYPESFTA.LEASE, TYPESFTA.LEASE);
				@SuppressWarnings("unchecked")
				LinkedHashMap<String, String> map = gson.fromJson(json, LinkedHashMap.class);
				// map.put("commencementDate", entry.getCommencementDate()+"");
				// map.put("paymentInterval", entry.getPaymentIntervals());
				// map.put("paymentsAt", entry.getPaymentsAt());
				System.out.println("converted");
				/*
				 * map.put("commencementDate", entryc.getCommencementDate() +
				 * ""); map.put("paymentsAt", entryc.getPaymentsAt());
				 * map.put("paymentIntervals", entryc.getPaymentIntervals());
				 * map.put("leaseName", userData.getLeaseName());
				 * map.put("lessorName", userData.getLessorName());
				 * map.put("referenceNo", userData.getLeaseContractNo());
				 * map.put("classOfAsset", userData.getClassOfAsset());
				 * map.put("id", userData.getId() + "");
				 */

				// map.put("payment", value)

				mapFinal.put(userData.getDataId(), map);

			}

			System.out.println("retruning" + mapFinal);

			return gson.toJson(mapFinal);
		} catch (Exception e) {
			throw e;
		}
	}

	private void copyData(UserData s, Entry t) {

		t.setAnnualDiscountRate(s.getAnnualDiscountRate() / 100);
		t.setCommencementDate(s.getCommencementDate());
		t.setEscalation(s.getEscalation() / 100);
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
		t.setContractReferenceNo(s.getLeaseContractNo());
		t.setAssetCode(s.getClassOfAsset());
		t.setLessorName(s.getLessorName());
		t.setLocation(s.getLocation());
		round(t.getEscalation(), 2);
		round(t.getAnnualDiscountRate(), 2);

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@PostMapping("/journal/quarterly")
	public String calculateJournalQuarterly(@RequestBody Entry entry) throws Exception {
		try {
			Calculation c = new Calculation();
			json = c.entryJournal(entry, TYPES.JOURNAL_QUARTERLY, TYPES.LEASE_QUARTERLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/journal/monthly")
	public String calculateJournalMonthly(@RequestBody Entry entry) throws Exception {
		try {
			Calculation c = new Calculation();
			json = c.entryJournal(entry, TYPES.JOURNAL_MONTHLY, TYPES.LEASE_MONTHLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/journal/fta")
	public String calculateJournalFirstTimeAdoption(@RequestBody Entry entry) throws Exception {
		try {
			CalculationFTA c = new CalculationFTA();
			json = c.entryFirstTimeAdoption(entry, TYPESFTA.RETROSPECTIVE, TYPESFTA.LEASE);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@PostMapping("/fta/lease")
	public String calculateFTALease(@RequestBody Entry entry) throws Exception {
		try {
			CalculationFTA c = new CalculationFTA();
			json = c.entryFirstTimeAdoption(entry, TYPESFTA.LEASE, TYPESFTA.LEASE);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping
	public String getCalculation() {

		return json;
	}

	@PostMapping("/journal/test")
	public String calculateJournaltest(@RequestBody Entry entryc) throws Exception {
		try {
			Calculation c = new Calculation();
			System.out.println("Values going For SUM  are:" + entryc.getCommencementDate() + "::"
					+ entryc.getPaymentsAt() + "::" + entryc.getAnnualDiscountRate() + "::" + entryc.getLeaseTerm()
					+ "::" + entryc.getExpectedPeriod() + "::" + entryc.getLeasePayment() + "::"
					+ entryc.getPaymentIntervals() + "::" + entryc.getInitialDirectCost() + "::"
					+ entryc.getGuaranteedResidualValue() + "::" + entryc.getUsefulLifeOfTheAsset() + "::"
					+ entryc.getEscalation() + "::" + entryc.getEscalationAfterEvery() + "::");

			json = c.entryTEST(entryc, TYPES.JOURNAL_YEARLY, TYPES.LEASE_YEARLY);
			return json;
		} catch (Exception e) {
			throw e;
		}
	}

}
