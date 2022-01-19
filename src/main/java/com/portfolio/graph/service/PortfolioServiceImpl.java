package com.portfolio.graph.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.graph.dao.EdgesDao;
import com.portfolio.graph.dao.NodesDao;
import com.portfolio.graph.dto.EdgeDTO;
import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.exception.InvalidPortfolioException;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	private NodesDao nodeDAO;

	@Autowired
	private EdgesDao edgeDAO;

	/**
	 * To add new node in database
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int persistNodes(Nodes node) throws InvalidPortfolioException, Exception {
		return nodeDAO.save(node);
	}

	/**
	 * To add link between two nodes and save it in database
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int persistEdges(EdgeDTO edge) throws InvalidPortfolioException, Exception {		 
		return edgeDAO.save(edge);
	}

	/**
	 * This method is to get the total market value for a fund of holdings
	 */
	@Override
	public long getMarketValueForFund(String fundName) {
		return nodeDAO.getMarketValueForFund(fundName);
	}

	/**
	 * This method is to get the total market value for an investor of holdings
	 */
	@Override
	public long getMarketValueForInvestor(String investorName) {
		return nodeDAO.getMarketValueForInvestor(investorName);
	}

	/**
	 * This method is to get the total market value for a fund after excluding list
	 * of holdings
	 */
	@Override
	public long getMarketValueForFundWithExclusionList(String fundName, List<String> exclusionList) {
		return nodeDAO.getMarketValueForFundWithExclusionList(fundName, exclusionList);
	}

	/**
	 * This method is to get the total market value for an investor after excluding
	 * list of holdings
	 */
	@Override
	public long getMarketValueForInvestorWithExclusionList(String investorName, List<String> exclusionList) {
		return nodeDAO.getMarketValueForInvestorWithExclusionList(investorName, exclusionList);
	}

}