package com.portfolio.graph.constants;

public enum TYPE {
	INVESTOR(1), FUNDS(2), HOLDINGS(3);

	private int value;

	TYPE(int number) {
		value = number;
	}

	public int getValue() {
		return value;
	}
}
