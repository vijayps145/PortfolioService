package com.portfolio.graph;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portfolio.graph.constants.Constants;
import com.portfolio.graph.dto.EdgeDTO;
import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.service.PortfolioService;

@Component
public class NodeClientBean {
	
	@Autowired
	private PortfolioService service;

	/**
	 * Use this method to add new node in database
	 * 
	 * @param node
	 */
	public int persistNodes(Nodes node) {

		int nodeId = 0;
		try {
			nodeId = service.persistNodes(node);
		} catch (Exception e) {
			Constants.logException(e);
		}
		return nodeId;
	}

	/**
	 * To add edge between two nodes and save it in database
	 * 
	 * @param node
	 */
	public void addEdge(Nodes node) {

		try {
			service.persistNodes(node);
		} catch (Exception e) {
			Constants.logException(e);
		}
	}

	public void addEdge(String parentNode, String childNode, int edgeLevel, int quantity, int weight) {
		EdgeDTO edge = new EdgeDTO(parentNode, childNode, edgeLevel, quantity, weight);
		try {
			service.persistEdges(edge);
		} catch (Exception e) {
			Constants.logException(e);
		}
	}

	/**
	 * Get total market value for a fund
	 * 
	 * @param fundName
	 * @return
	 */
	public long getMarketValueForFund(String fundName) {

		return service.getMarketValueForFund(fundName);
	}

	/**
	 * Get total market value for a particular investor
	 * 
	 * @param investorName
	 * @return
	 */
	public long getMarketValueForInvestor(String investorName) {

		return service.getMarketValueForInvestor(investorName);
	}

	/**
	 * Get total market value for a fund after excluding certain holdings
	 * 
	 * @param fundName
	 * @param exclusionList
	 * @return
	 */
	public long getMarketValueForFundWithExclusionList(String fundName, List<String> exclusionList) {

		return service.getMarketValueForFundWithExclusionList(fundName, exclusionList);
	}

	/**
	 * Get total market value for a particular investor after excluding certain
	 * holdings
	 * 
	 * @param investorName
	 * @param exclusionList
	 * @return
	 */
	public long getMarketValueForInvestorWithExclusionList(String investorName, List<String> exclusionList) {
		return service.getMarketValueForInvestorWithExclusionList(investorName, exclusionList);
	}
}