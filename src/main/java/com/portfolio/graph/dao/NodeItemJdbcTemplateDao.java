package com.portfolio.graph.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.portfolio.graph.constants.Constants;
import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.exception.InvalidPortfolioException;
import com.portfolio.graph.mapper.MarketValueAction;

@Component
@AllArgsConstructor
public class NodeItemJdbcTemplateDao implements NodesDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     *  This method is to save the newly added node in database.
     * @throws InvalidPortfolioException 
     */
    @Override
    public int save(Nodes nodeItem) throws InvalidPortfolioException {
       
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(Constants.INSERT_NODE_QUERY,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nodeItem.getName());
                ps.setInt(2, nodeItem.getLevel());
                ps.setInt(3, nodeItem.getHoldingWeight());
                return ps;
            }
        }, holder);
        Number key = holder.getKey();
        if (key != null) {
            return key.intValue();
        }
        throw new InvalidPortfolioException("No generated primary key returned.");
    }	

	/**
	 * This method find the total market value for the given fund after processing
	 * the result from database.
	 */
	@Override
	public long getMarketValueForFund(String fundName) {

		List<MarketValueAction> objList = fetchMarketValueForFund(fundName);

		int sum = 0;

		for (MarketValueAction obj : objList) {
			sum = sum + obj.getHoldingQuantity() * obj.getHoldingWeight();
		}
		return sum;
	}



	/**
	 * This method find the total market value for the given fund after
	 * processing the result from database and then exclude the certain holdings
	 * based on exclusionList
	 */
	@Override
	public long getMarketValueForFundWithExclusionList(String fundName, List<String> exclusionList) {
		List<MarketValueAction> objList = fetchMarketValueForFund(fundName);

		int sum = 0;

		// remove exclusion list of holding entries from the objList
		objList.removeIf(obj -> exclusionList.contains(obj.getHoldingName()));

		for (MarketValueAction obj : objList) {
			sum = sum + obj.getHoldingQuantity() * obj.getHoldingWeight();
		}
		return sum;
	}

	/**
	 * This method find the total market value for the given investor after
	 * processing the result from database.
	 */	
	@Override
	public long getMarketValueForInvestor(String investorName) {

		List<MarketValueAction> objList = fetchMarketValueForInvestor(investorName);

		int sum = 0;

		for (MarketValueAction obj : objList) {
			sum = sum + obj.getHoldingQuantity() * obj.getHoldingWeight();
		}
		
		return sum;
	}
	
	/**
	 * This method find the total market value for the given investor after
	 * processing the result from database and then exclude the certain holdings
	 * based on exclusionList
	 */
	@Override
	public long getMarketValueForInvestorWithExclusionList(String investorName, List<String> exclusionList) {
		List<MarketValueAction> objList = fetchMarketValueForInvestor(investorName);
		
		int sum = 0;
		
        // remove exclusion list of holding entries from the objList 
		objList.removeIf(obj -> exclusionList.contains(obj.getHoldingName()));

		for (MarketValueAction obj : objList) {
			sum = sum + obj.getHoldingQuantity() * obj.getHoldingWeight();
		}
		return sum;
	}
	
	/**
	 * This database call fetch the holding weight, holding quantity and holding
	 * name based on the investorName and we are storing that information in the list.
	 * 
	 * @param investorName
	 * @return
	 */
	private List<MarketValueAction> fetchMarketValueForInvestor(String investorName) {

		List<MarketValueAction> objList = new ArrayList<>();

		try {
			objList = jdbcTemplate.query(Constants.MARKET_VALUE_BY_INVESTOR, (rs, rowNum) ->

			new MarketValueAction(rs.getInt("holdingweight"), rs.getInt("holding_quantity"), rs.getString("name")),
					new Object[] { investorName }
			);

		} catch (Exception e) {
			Constants.logException(e);
		}
		return objList;
	}
	
	
	/**
	 * This database call fetch the holding weight, holding quantity and holding
	 * name based on the fund name and we are storing that information in the list.
	 * 
	 * @param fundName
	 * @return
	 */
	private List<MarketValueAction> fetchMarketValueForFund(String fundName) {

		List<MarketValueAction> objectList = new ArrayList<>();

		try {
			objectList = jdbcTemplate.query(Constants.MARKET_VALUE_BY_FUND, (rs, rowNum) ->

			new MarketValueAction(rs.getInt("holdingweight"), rs.getInt("holding_quantity"), rs.getString("name")),
					new Object[] { fundName }
			);

		} catch (Exception e) {
			Constants.logException(e);
		}
		return objectList;
	}
}