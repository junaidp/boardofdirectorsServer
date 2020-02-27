package com.example.boardofdirectorsServer.model;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserData {

	@Id
	private String dataId;
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
	
	
	public String getLeaseContractNo() {
		return leaseContractNo;
	}
	public void setLeaseContractNo(String leaseContractNo) {
		this.leaseContractNo = leaseContractNo;
	}
	public Date getCommencementDate() {
		return commencementDate;
	}
	public void setCommencementDate(Date commencementDate) {
		this.commencementDate = commencementDate;
	}
	public String getPaymentsAt() {
		return paymentsAt;
	}
	public void setPaymentsAt(String paymentsAt) {
		this.paymentsAt = paymentsAt;
	}
	public float getAnnualDiscountRate() {
		return annualDiscountRate;
	}
	public void setAnnualDiscountRate(float annualDiscountRate) {
		this.annualDiscountRate = annualDiscountRate;
	}
	public int getLeaseTerm() {
		return leaseTerm;
	}
	public void setLeaseTerm(int leaseTerm) {
		this.leaseTerm = leaseTerm;
	}
	public int getExpectedPeriod() {
		return expectedPeriod;
	}
	public void setExpectedPeriod(int expectedPeriod) {
		this.expectedPeriod = expectedPeriod;
	}
	public double getLeasePayment() {
		return leasePayment;
	}
	public void setLeasePayment(double leasePayment) {
		this.leasePayment = leasePayment;
	}
	public String getPaymentIntervals() {
		return paymentIntervals;
	}
	public void setPaymentIntervals(String paymentIntervals) {
		this.paymentIntervals = paymentIntervals;
	}
	public int getInitialDirectCost() {
		return initialDirectCost;
	}
	public void setInitialDirectCost(int initialDirectCost) {
		this.initialDirectCost = initialDirectCost;
	}
	public double getGuaranteedResidualValue() {
		return guaranteedResidualValue;
	}
	public void setGuaranteedResidualValue(double guaranteedResidualValue) {
		this.guaranteedResidualValue = guaranteedResidualValue;
	}
	public int getUsefulLifeOfTheAsset() {
		return usefulLifeOfTheAsset;
	}
	public void setUsefulLifeOfTheAsset(int usefulLifeOfTheAsset) {
		this.usefulLifeOfTheAsset = usefulLifeOfTheAsset;
	}
	public float getEscalation() {
		return escalation;
	}
	public void setEscalation(float escalation) {
		this.escalation = escalation;
	}
	public int getEscalationAfterEvery() {
		return escalationAfterEvery;
	}
	public void setEscalationAfterEvery(int escalationAfterEvery) {
		this.escalationAfterEvery = escalationAfterEvery;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	
	
	
}
