package com.example.boardofdirectorsServer.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserData {

	@Id
	private String dataId;
	private String leaseContractNo;
	private Date commencementDate;
	private String paymentsAt;
	private String leaseName;
	private String lessorName;
	private String lesseeName;
	private String location;
	private String classOfAsset;
	private float annualDiscountRate;
	private int leaseTerm;
	private int expectedPeriod;
	private double leasePayment;
	private String paymentIntervals;
	private String contractCurrency;
	private String assetCode;
	private int initialDirectCost;
	private double guaranteedResidualValue;
	private int usefulLifeOfTheAsset;
	private float escalation;
	private int escalationAfterEvery;
	private int year;
	private int month;
	private int userId;
	private int companyId;
	private String id;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String answer5;
	private String answer6;
	private String answer7;
	private String conclusion;
	private String fileName;
	private String assetDescription;
	private Boolean isModified;

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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getLeaseName() {
		return leaseName;
	}

	public void setLeaseName(String leaseName) {
		this.leaseName = leaseName;
	}

	public String getLessorName() {
		return lessorName;
	}

	public void setLessorName(String lessorName) {
		this.lessorName = lessorName;
	}

	public String getLesseeName() {
		return lesseeName;
	}

	public void setLesseeName(String lesseeName) {
		this.lesseeName = lesseeName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClassOfAsset() {
		return classOfAsset;
	}

	public void setClassOfAsset(String classOfAsset) {
		this.classOfAsset = classOfAsset;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getAnswer5() {
		return answer5;
	}

	public void setAnswer5(String answer5) {
		this.answer5 = answer5;
	}

	public String getAnswer6() {
		return answer6;
	}

	public void setAnswer6(String answer6) {
		this.answer6 = answer6;
	}

	public String getAnswer7() {
		return answer7;
	}

	public void setAnswer7(String answer7) {
		this.answer7 = answer7;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getContractCurrency() {
		return contractCurrency;
	}

	public void setContractCurrency(String contractCurrency) {
		this.contractCurrency = contractCurrency;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAssetDescription() {
		return assetDescription;
	}

	public void setAssetDescription(String assetDescription) {
		this.assetDescription = assetDescription;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public Boolean getIsModified() {
		return isModified;
	}

	public void setIsModified(Boolean isModified) {
		this.isModified = isModified;
	}

}
