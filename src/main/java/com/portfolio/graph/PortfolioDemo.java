package com.portfolio.graph;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.portfolio.graph.entity.Nodes;

public class PortfolioDemo {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		NodeClientBean client = context.getBean(NodeClientBean.class);
		
		Nodes node = new Nodes();
		node.setName("I1");
		node.setLevel(1);		
		System.out.println("Node I1 saved successfully with nodeId = " + client.persistNodes(node));
		
	}
}