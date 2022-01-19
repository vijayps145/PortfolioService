package com.portfolio.graph.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.exception.InvalidPortfolioException;

@Repository
public interface NodesDao {

    int save(Nodes node) throws InvalidPortfolioException, Exception;

	long getMarketValueForFund(String fundName);

	long getMarketValueForInvestor(String investorName);

	long getMarketValueForFundWithExclusionList(String fundName, List<String> exclusionList);

	long getMarketValueForInvestorWithExclusionList(String investorName, List<String> exclusionList);
}