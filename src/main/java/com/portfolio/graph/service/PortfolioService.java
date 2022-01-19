package com.portfolio.graph.service;

import java.util.List;

import com.portfolio.graph.dto.EdgeDTO;
import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.exception.InvalidPortfolioException;


public interface PortfolioService {

	int persistNodes(Nodes node) throws InvalidPortfolioException, Exception;
	
	int persistEdges(EdgeDTO edge) throws InvalidPortfolioException, Exception;

	long getMarketValueForFund(String fundName);

	long getMarketValueForInvestor(String investorName);

	long getMarketValueForFundWithExclusionList(String fundName, List<String> exclusionList);

	long getMarketValueForInvestorWithExclusionList(String investorName, List<String> exclusionList);
}