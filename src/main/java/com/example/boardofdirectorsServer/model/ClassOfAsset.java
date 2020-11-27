
package com.example.boardofdirectorsServer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ClassOfAsset {

	@Id
	private String id;
	private String classOfAsset;

	public String getClassOfAsset() {
		return classOfAsset;
	}

	public void setClassOfAsset(String classOfAsset) {
		this.classOfAsset = classOfAsset;
	}

}
