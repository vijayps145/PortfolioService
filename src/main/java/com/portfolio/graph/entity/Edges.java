package com.portfolio.graph.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Edges {

	private int edgeId;
	private int parentNodeId;
	private int childNodeId;
	private int edgeLevel;
	private int holdingQuantity;

}
