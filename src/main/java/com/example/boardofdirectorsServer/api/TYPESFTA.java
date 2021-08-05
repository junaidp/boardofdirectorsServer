package com.example.boardofdirectorsServer.api;

public enum TYPESFTA {

	LEASE(0), CUMMULATIVE(1), RETROSPECTIVE(2), SCHEDULELEASEREPORT(0), LEASELIABILITYREPORT(0);

	private int value;

	private TYPESFTA(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
