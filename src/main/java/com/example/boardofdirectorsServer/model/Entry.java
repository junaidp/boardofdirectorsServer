package com.example.boardofdirectorsServer.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entry {

	private String leaseContractNo;
	private	Date commencementDate;	
	private	String paymentsAt; 	
	private	int annualDiscountRate;
	private	int leaseTerm;	
	private	int expectedPeriod;	
	private	double leasePayment;	
	private	String paymentIntervals;	
	private	int initialDirectCost;	
	private	double guaranteedResidualValue;	
	private	int usefulLifeOfTheAsset;	
	private	int escalation;	
	private	int escalationAfterEvery;

	public Entry(@JsonProperty("leaseContractNo")String leaseContractNo, @JsonProperty("commencementDate")Date commencementDate,
			@JsonProperty("paymentsAt")String paymentsAt, @JsonProperty("annualDiscountRate")int annualDiscountRate,@JsonProperty("leaseTerm")int leaseTerm,
			@JsonProperty("expectedPeriod")int expectedPeriod,@JsonProperty("leasePayment")double leasePayment,@JsonProperty("paymentIntervals")String paymentIntervals,
			@JsonProperty("initialDirectCost")int initialDirectCost,@JsonProperty("guaranteedResidualValue")double guaranteedResidualValue,
			@JsonProperty("usefulLifeOfTheAsset")int usefulLifeOfTheAsset,
			@JsonProperty("escalation")int escalation,	@JsonProperty("escalationAfterEvery")int escalationAfterEvery) {

		this.leaseContractNo = leaseContractNo;
		this.commencementDate = commencementDate;
		this.paymentsAt = paymentsAt;
		this.annualDiscountRate = annualDiscountRate;
		this.leaseTerm = leaseTerm;
		this.expectedPeriod = expectedPeriod;
		this.leasePayment = leasePayment;
		this.paymentIntervals = paymentIntervals;
		this.initialDirectCost = initialDirectCost;
		this.guaranteedResidualValue = guaranteedResidualValue;
		this.usefulLifeOfTheAsset = usefulLifeOfTheAsset;
		this.escalation = escalation;
		this.escalationAfterEvery = escalationAfterEvery;

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

	public int getAnnualDiscountRate() {
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

	public int getEscalation() {
		return escalation;
	}

	public int getEscalationAfterEvery() {
		return escalationAfterEvery;
	}

}
