package com.example.boardofdirectorsServer.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserData {

	@Id
	private String dataId;
	private String userId;
	private String leaseContractNo;
	private String classAsset;
	private String commencementDate;
	private String paymentsAt;
	private String annualDiscountRate;
	private String leaseTerm;
	private String expectedPeriod;
	private String leasePayment;
	private String paymentIntervals;
	private String initialDirectCost;
	private String guaranteedResidualValue;
	private String usefulLifeOfTheAsset;
	private String escalation;
	private String escalationAfterEvery;
	
	
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLeaseContractNo() {
		return leaseContractNo;
	}
	public void setLeaseContractNo(String leaseContractNo) {
		this.leaseContractNo = leaseContractNo;
	}
	public String getClassAsset() {
		return classAsset;
	}
	public void setClassAsset(String classAsset) {
		this.classAsset = classAsset;
	}
	public String getCommencementDate() {
		return commencementDate;
	}
	public void setCommencementDate(String commencementDate) {
		this.commencementDate = commencementDate;
	}
	public String getPaymentsAt() {
		return paymentsAt;
	}
	public void setPaymentsAt(String paymentsAt) {
		this.paymentsAt = paymentsAt;
	}
	public String getAnnualDiscountRate() {
		return annualDiscountRate;
	}
	public void setAnnualDiscountRate(String annualDiscountRate) {
		this.annualDiscountRate = annualDiscountRate;
	}
	public String getLeaseTerm() {
		return leaseTerm;
	}
	public void setLeaseTerm(String leaseTerm) {
		this.leaseTerm = leaseTerm;
	}
	public String getExpectedPeriod() {
		return expectedPeriod;
	}
	public void setExpectedPeriod(String expectedPeriod) {
		this.expectedPeriod = expectedPeriod;
	}
	public String getLeasePayment() {
		return leasePayment;
	}
	public void setLeasePayment(String leasePayment) {
		this.leasePayment = leasePayment;
	}
	public String getPaymentIntervals() {
		return paymentIntervals;
	}
	public void setPaymentIntervals(String paymentIntervals) {
		this.paymentIntervals = paymentIntervals;
	}
	public String getInitialDirectCost() {
		return initialDirectCost;
	}
	public void setInitialDirectCost(String initialDirectCost) {
		this.initialDirectCost = initialDirectCost;
	}
	public String getGuaranteedResidualValue() {
		return guaranteedResidualValue;
	}
	public void setGuaranteedResidualValue(String guaranteedResidualValue) {
		this.guaranteedResidualValue = guaranteedResidualValue;
	}
	public String getUsefulLifeOfTheAsset() {
		return usefulLifeOfTheAsset;
	}
	public void setUsefulLifeOfTheAsset(String usefulLifeOfTheAsset) {
		this.usefulLifeOfTheAsset = usefulLifeOfTheAsset;
	}
	public String getEscalation() {
		return escalation;
	}
	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}
	public String getEscalationAfterEvery() {
		return escalationAfterEvery;
	}
	public void setEscalationAfterEvery(String escalationAfterEvery) {
		this.escalationAfterEvery = escalationAfterEvery;
	}
	


	
}
