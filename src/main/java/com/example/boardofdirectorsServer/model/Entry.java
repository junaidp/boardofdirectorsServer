package com.example.boardofdirectorsServer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entry {

	private String leaseContractNo;
	private	Date commencementDate;	
	private	String paymentsAt; 	
	private	float annualDiscountRate;
	private	int leaseTerm;	
	private	int expectedPeriod;	
	private	double leasePayment;	
	private	String paymentIntervals;	
	private	int initialDirectCost;	
	private	double guaranteedResidualValue;	
	private	int usefulLifeOfTheAsset;	
	private	float escalation;	
	private	int escalationAfterEvery;
	private int year;
	private int month;
	private int userId;

	public Entry(@JsonProperty("leaseContractNo")String leaseContractNo, @JsonProperty("commencementDate")Date commencementDate,
			@JsonProperty("paymentsAt")String paymentsAt, @JsonProperty("annualDiscountRate")float annualDiscountRate,@JsonProperty("leaseTerm")int leaseTerm,
			@JsonProperty("expectedPeriod")int expectedPeriod,@JsonProperty("leasePayment")double leasePayment,@JsonProperty("paymentIntervals")String paymentIntervals,
			@JsonProperty("initialDirectCost")int initialDirectCost,@JsonProperty("guaranteedResidualValue")double guaranteedResidualValue,
			@JsonProperty("usefulLifeOfTheAsset")int usefulLifeOfTheAsset,
			@JsonProperty("escalation")float escalation,	@JsonProperty("escalationAfterEvery")int escalationAfterEvery, 
			@JsonProperty("year")int year, @JsonProperty("month")int month, @JsonProperty("userId")int userId) {

		this.leaseContractNo = leaseContractNo;
		this.commencementDate = commencementDate;
		this.paymentsAt = paymentsAt;
		this.annualDiscountRate = annualDiscountRate/100;
		
		this.leaseTerm = leaseTerm;
		this.expectedPeriod = expectedPeriod;
		this.leasePayment = leasePayment;
		this.paymentIntervals = paymentIntervals;
		this.initialDirectCost = initialDirectCost;
		this.guaranteedResidualValue = guaranteedResidualValue;
		this.usefulLifeOfTheAsset = usefulLifeOfTheAsset;
		this.escalation = escalation/100;
		this.escalationAfterEvery = escalationAfterEvery;
		this.year = year;
		this.month = month;
		
		round(this.escalation, 2);
		round(this.annualDiscountRate, 2);
		
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

	public int getInitialDirectCost() {
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
	    if (places < 0) throw new IllegalArgumentException();

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


}
