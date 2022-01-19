package com.portfolio.graph.dao;

import org.springframework.stereotype.Repository;

import com.portfolio.graph.dto.EdgeDTO;
import com.portfolio.graph.exception.InvalidPortfolioException;

@Repository
public interface EdgesDao {

	int save(EdgeDTO edgeDto) throws InvalidPortfolioException, Exception;
	
}