package com.portfolio.graph.mapper;

public class MarketValueAction {

	private int holdingWeight;
	private int holdingQuantity;
	private String holdingName;

	public MarketValueAction() {

	}

	public MarketValueAction(int holdingWeight, int holdingQuantity, String holdingName) {
		super();
		this.holdingWeight = holdingWeight;
		this.holdingQuantity = holdingQuantity;
		this.holdingName = holdingName;
	}

	public int getHoldingWeight() {
		return holdingWeight;
	}

	public void setHoldingWeight(int holdingWeight) {
		this.holdingWeight = holdingWeight;
	}

	public int getHoldingQuantity() {
		return holdingQuantity;
	}

	public void setHoldingQuantity(int holdingQuantity) {
		this.holdingQuantity = holdingQuantity;
	}

	public String getHoldingName() {
		return holdingName;
	}

	public void setHoldingName(String holdingName) {
		this.holdingName = holdingName;
	}

}
