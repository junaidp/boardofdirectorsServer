package com.example.boardofdirectorsServer.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entry {

	private final int a;
	private final int b;
//	private int leaseContractNo;
//	private	Date commencementDate;	
//	private	String paymentsAt; 	
//	private	int annualDiscountRate;
//	private	int leaseTerm;	
//	private	int expectedPeriod;	
//	private	float leasePayment;	
//	private	String paymentIntervals;	
//	private	int initialDirectCost;	
//	private	float guaranteedResidualValue;	
//	private	float rightOfUseAssetAtInitialRecognition;	
//	private	int usefulLifeOfTheAsset;	
//	private	int escalation;	
//	private	int escalationAfterEvery;
	
	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public Entry(@JsonProperty("a")int a, @JsonProperty("b")int b) {
		this.a = a;
		this.b = b;
	}
	
}
