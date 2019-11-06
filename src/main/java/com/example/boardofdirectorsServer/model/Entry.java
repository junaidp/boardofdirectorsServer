package com.example.boardofdirectorsServer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entry {

	private final int a;
	private final int b;
	
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
