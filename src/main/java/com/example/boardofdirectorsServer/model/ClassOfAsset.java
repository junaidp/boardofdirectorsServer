
package com.example.boardofdirectorsServer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ClassOfAsset {

	@Id
	private String id;
	private String classOfAsset;
	private int companyId;
	private int userId;

	public String getClassOfAsset() {
		return classOfAsset;
	}

	public void setClassOfAsset(String classOfAsset) {
		this.classOfAsset = classOfAsset;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
