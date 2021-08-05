package com.example.boardofdirectorsServer.api;

public enum TYPES {

	LEASE_YEARLY(0), JOURNAL_YEARLY(1), LEASE_MONTHLY(2), JOURNAL_MONTHLY(3), LEASE_QUARTERLY(4), JOURNAL_QUARTERLY(
			5), RECOGNITION_YEARLY(7), RECOGNITION_MONTHLY(8), RECOGNITION_QUARTERLY(9);

	private int value;

	private TYPES(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
