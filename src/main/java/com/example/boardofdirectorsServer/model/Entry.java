package com.example.boardofdirectorsServer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entry {

	private String leaseContractNo;
	private Date commencementDate;
	private String paymentsAt;
	private float annualDiscountRate;
	private int leaseTerm;
	private int expectedPeriod;
	private double leasePayment;
	private String paymentIntervals;
	private String location;
	private String dataId;
	private String lessorName;
	private String assetCode;
	private String contractReferenceNo;
	private double initialDirectCost;
	private double guaranteedResidualValue;
	private int usefulLifeOfTheAsset;
	private float escalation;
	private int escalationAfterEvery;
	private int year;
	private int month;
	private int userId;
	private int companyId;
	private String recognitionOptions;
	private double paymentToAdd;
	private Date userSelectedDate;
	// private Boolean isModified;

	public Entry(@JsonProperty("leaseContractNo") String leaseContractNo,
			@JsonProperty("commencementDate") Date commencementDate, @JsonProperty("paymentsAt") String paymentsAt,
			@JsonProperty("annualDiscountRate") float annualDiscountRate, @JsonProperty("leaseTerm") int leaseTerm,
			@JsonProperty("expectedPeriod") int expectedPeriod, @JsonProperty("leasePayment") double leasePayment,
			@JsonProperty("paymentIntervals") String paymentIntervals,
			@JsonProperty("initialDirectCost") int initialDirectCost,
			@JsonProperty("guaranteedResidualValue") double guaranteedResidualValue,
			@JsonProperty("usefulLifeOfTheAsset") int usefulLifeOfTheAsset,
			@JsonProperty("escalation") float escalation,
			@JsonProperty("escalationAfterEvery") int escalationAfterEvery, @JsonProperty("year") int year,
			@JsonProperty("month") int month, @JsonProperty("userId") int userId,
			@JsonProperty("companyId") int companyId, @JsonProperty("recognitionOptions") String recognitionOptions,
			@JsonProperty("paymentToAdd") double paymentToAdd,
			@JsonProperty("userSelectedDate") Date userSelectedDate) {

		this.leaseContractNo = leaseContractNo;
		this.commencementDate = commencementDate;
		this.paymentsAt = paymentsAt;
		this.annualDiscountRate = annualDiscountRate / 100;

		this.leaseTerm = leaseTerm;
		this.expectedPeriod = expectedPeriod;
		this.leasePayment = leasePayment;
		this.paymentIntervals = paymentIntervals;
		this.initialDirectCost = initialDirectCost;
		this.guaranteedResidualValue = guaranteedResidualValue;
		this.usefulLifeOfTheAsset = usefulLifeOfTheAsset;
		this.escalation = escalation / 100;
		this.escalationAfterEvery = escalationAfterEvery;
		this.year = year;
		this.month = month;
		this.userId = userId;
		this.companyId = companyId;

		round(this.escalation, 2);
		round(this.annualDiscountRate, 2);
		this.recognitionOptions = recognitionOptions;
		this.paymentToAdd = paymentToAdd;
		this.userSelectedDate = userSelectedDate;

	}

	public Entry() {
		// TODO Auto-generated constructor stub
	}

	public String getLeaseContractNo() {
		return leaseContractNo;
	}

	public Date getCommencementDate() {
		return commencementDate;
	}

	public String getPaymentsAt() {
		return paymentsAt;
	}

	public float getAnnualDiscountRate() {
		return annualDiscountRate;
	}

	public int getLeaseTerm() {
		return leaseTerm;
	}

	public int getExpectedPeriod() {
		return expectedPeriod;
	}

	public double getLeasePayment() {
		return leasePayment;
	}

	public String getPaymentIntervals() {
		return paymentIntervals;
	}

	public double getInitialDirectCost() {
		return initialDirectCost;
	}

	public double getGuaranteedResidualValue() {
		return guaranteedResidualValue;
	}

	public int getUsefulLifeOfTheAsset() {
		return usefulLifeOfTheAsset;
	}

	public float getEscalation() {
		return escalation;
	}

	public int getEscalationAfterEvery() {
		return escalationAfterEvery;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getUserId() {
		return userId;
	}

	public void setLeaseContractNo(String leaseContractNo) {
		this.leaseContractNo = leaseContractNo;
	}

	public void setCommencementDate(Date commencementDate) {
		this.commencementDate = commencementDate;
	}

	public void setPaymentsAt(String paymentsAt) {
		this.paymentsAt = paymentsAt;
	}

	public void setAnnualDiscountRate(float annualDiscountRate) {
		this.annualDiscountRate = annualDiscountRate;
	}

	public void setLeaseTerm(int leaseTerm) {
		this.leaseTerm = leaseTerm;
	}

	public void setExpectedPeriod(int expectedPeriod) {
		this.expectedPeriod = expectedPeriod;
	}

	public void setLeasePayment(double leasePayment) {
		this.leasePayment = leasePayment;
	}

	public void setPaymentIntervals(String paymentIntervals) {
		this.paymentIntervals = paymentIntervals;
	}

	public void setInitialDirectCost(int initialDirectCost) {
		this.initialDirectCost = initialDirectCost;
	}

	public void setGuaranteedResidualValue(double guaranteedResidualValue) {
		this.guaranteedResidualValue = guaranteedResidualValue;
	}

	public void setUsefulLifeOfTheAsset(int usefulLifeOfTheAsset) {
		this.usefulLifeOfTheAsset = usefulLifeOfTheAsset;
	}

	public void setEscalation(float escalation) {
		this.escalation = escalation;
	}

	public void setEscalationAfterEvery(int escalationAfterEvery) {
		this.escalationAfterEvery = escalationAfterEvery;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLessorName() {
		return lessorName;
	}

	public void setLessorName(String lessorName) {
		this.lessorName = lessorName;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getContractReferenceNo() {
		return contractReferenceNo;
	}

	public void setContractReferenceNo(String contractReferenceNo) {
		this.contractReferenceNo = contractReferenceNo;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getRecognitionOptions() {
		return recognitionOptions;
	}

	public void setRecognitionOptions(String recognitionOptions) {
		this.recognitionOptions = recognitionOptions;
	}

	public double getPaymentToAdd() {
		return paymentToAdd;
	}

	public void setPaymentToAdd(double paymentToAdd) {
		this.paymentToAdd = paymentToAdd;
	}

	public Date getUserSelectedDate() {
		return userSelectedDate;
	}

	public void setUserSelectedDate(Date userSelectedDate) {
		this.userSelectedDate = userSelectedDate;
	}

	/*
	 * public Boolean getIsModified() { return isModified; }
	 * 
	 * public void setIsModified(Boolean isModified) { this.isModified =
	 * isModified; }
	 */

}
