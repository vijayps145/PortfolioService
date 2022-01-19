package com.portfolio.graph.constants;

public interface Constants {
	
	String MARKET_VALUE_BY_FUND =  "select e.holding_quantity , n2.holdingweight, n2.name from nodes n1, nodes n2 , edges e where n1.nodeId = e.parent_node_id "
			+ "and n2.nodeId = e.child_node_id and n1.name = ?";
	
	String MARKET_VALUE_BY_INVESTOR  = "select e2.holding_quantity , n3.holdingweight, n3.name from nodes n1, nodes n2 , nodes n3 , edges e1, edges e2 "
															+ " where n1.nodeId = e1.parent_node_id "
															+ " and e1.child_node_id = n2.nodeId"
															+ " and n2.nodeId = e2.parent_node_id"
															+ " and e2.child_node_id = n3.nodeId"
															+ " and n1.name = ?";
	
	String ADD_EDGE_QUERY = "insert into Edges (parent_node_id, child_node_id,edge_level, holding_quantity) values (?, ?, ?, ?)";
	
	String GET_NODE_BY_NAME_QUERY = "select nodeid from nodes where name = ?";
	
	String GET_EDGE_QUERY = "select edge_id from Edges where parent_node_id = ? and child_node_id = ?";
	
	String INSERT_NODE_QUERY = "insert into Nodes (name, level,holdingWeight) values (?, ?, ?)";
	
	int RECORD_COUNT = 1;
	
	int NOT_VALID = -1;
	
	
	 static void logException(Exception e) {
	        System.out.println("-- exception --");
	        System.err.println("Exception: "+e.getClass().getName());
	        System.err.println("Message: "+ e.getMessage());
	        System.out.println("---------");
	    }
}
