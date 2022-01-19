package com.portfolio.graph.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EdgeDTO {

	private String parentNodeName;
	private String childNodeName;
	private int edgeLevel;
	private int holdingQuantity;
	private int holdingWeight;

}
