package com.portfolio.graph.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Nodes {

	private int nodeId;
	private String name;
	private int level;
	private int holdingWeight;

	public Nodes(String name, int level, int holdingWeight) {
		super();
		this.name = name;
		this.level = level;
		this.holdingWeight = holdingWeight;
	}

}
