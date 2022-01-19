package com.portfolio.graph.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.portfolio.graph.constants.Constants;
import com.portfolio.graph.constants.TYPE;
import com.portfolio.graph.dto.EdgeDTO;
import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.exception.InvalidPortfolioException;
import com.portfolio.graph.service.PortfolioService;

@Component
public class EdgeItemJdbcTemplateDao implements EdgesDao {
	
	@Autowired
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PortfolioService service;

	@PostConstruct
	private void postConstruct() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * This method returns the id for the given node if node exists, otherwise
	 * return -1
	 * 
	 * @param nodeName
	 * @return
	 * @throws InvalidPortfolioException 
	 */
	public int getNodeId(String nodeName) throws InvalidPortfolioException {

		List<Integer> idList = jdbcTemplate.query(Constants.GET_NODE_BY_NAME_QUERY,
					(rs, rowNum) -> rs.getInt("nodeId"), new Object[] { nodeName });
		
		if (idList.size() == Constants.RECORD_COUNT) {
			return idList.get(0);
		} else if (idList.size() > Constants.RECORD_COUNT) {
			throw new InvalidPortfolioException("Found more than one records for the given node = (" + nodeName+") ");
		}
		
		return Constants.NOT_VALID;
	}

	/**
	 * This method is to get the parent node id if exist in database, otherwise
	 * create new entry for parent node in database and then return the id.
	 * 
	 * @param edgeItem
	 * @return
	 * @throws InvalidPortfolioException
	 */
	private int getParentNodeId(EdgeDTO edgeItem) throws InvalidPortfolioException {

		int parentId = 0;
		parentId = getNodeId(edgeItem.getParentNodeName());

		if (parentId == Constants.NOT_VALID) {
			Nodes parentNode = new Nodes();
			// if edgeLevel is 2, then parent level is 1, else parent level wise be the same
			// passed in the edge method
			int edgeLevel = edgeItem.getEdgeLevel() == TYPE.FUNDS.getValue() ? TYPE.INVESTOR.getValue() : TYPE.FUNDS.getValue();
			createNode(parentNode, edgeItem.getParentNodeName(), edgeLevel);
			try {
				parentId = service.persistNodes(parentNode);
			} catch (Exception e) {

				throw new InvalidPortfolioException("Exception occur while inserting parent node ("
						+ edgeItem.getParentNodeName() + ") in database");
			}
		}

		return parentId;
	}

	/**
	 * This method is to get the parent node id if exist in database, otherwise
	 * create new entry for child node in database and then return the id.
	 * @param edgeItem
	 * @return
	 * @throws InvalidPortfolioException
	 */
	private int getChildNodeId(EdgeDTO edgeItem) throws InvalidPortfolioException {
		int childId = 0;
		childId = getNodeId(edgeItem.getChildNodeName());

		if (childId == Constants.NOT_VALID) {
			Nodes childNode = new Nodes();
			// if edgeLevel is 2, then child level is 2, else child level will be the same
			// passed in the edge method
			int edgeLevel = edgeItem.getEdgeLevel() == TYPE.FUNDS.getValue() ? TYPE.FUNDS.getValue() : TYPE.HOLDINGS.getValue();
			createNode(childNode, edgeItem.getChildNodeName(), edgeLevel);
			childNode.setHoldingWeight(edgeItem.getHoldingWeight());
			try {
				childId = service.persistNodes(childNode);
			} catch (Exception e) {

				throw new InvalidPortfolioException(
						"Exception occur while inserting child node (" + edgeItem.getChildNodeName() + ") in database");
			}
		}

		return childId;

	}

	private void createNode(Nodes node, String nodeName, int level) {
		node.setName(nodeName);
		node.setLevel(level);
	}

	@Override
	public int save(EdgeDTO edgeItem) throws InvalidPortfolioException, Exception {

		if (edgeItem.getEdgeLevel() < TYPE.FUNDS.getValue() || edgeItem.getEdgeLevel() > TYPE.HOLDINGS.getValue()) {
			throw new InvalidPortfolioException("Edge level for parent node  (" + edgeItem.getParentNodeName() +" and child node (" + edgeItem.getChildNodeName() + ")  must be between 2 to 3 ");
		}
		final int parentNodeId = getParentNodeId(edgeItem);
		final int childNodeId = getChildNodeId(edgeItem);
		
		if(checkIfEdgeExists(parentNodeId,childNodeId)) {
			throw new InvalidPortfolioException("Edge with parent_node_id = " +parentNodeId +" and child_node_id = " + childNodeId + " already exists");
		}

		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(Constants.ADD_EDGE_QUERY,
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, parentNodeId);
				ps.setInt(2, childNodeId);
				ps.setInt(3, edgeItem.getEdgeLevel());
				ps.setInt(4, edgeItem.getHoldingQuantity());
				return ps;
			}
		}, holder);
		Number key = holder.getKey();
		if (key != null) {
			return key.intValue();
		}
		throw new InvalidPortfolioException("Unable to get the generated primary key.");
	}
	
	/**
	 * Check if the edge between the given parent and child node already exists 
	 * @param parentNodeId
	 * @param childNodeId
	 * @return
	 * @throws InvalidPortfolioException 
	 */
	public boolean checkIfEdgeExists(int parentNodeId, int childNodeId) throws InvalidPortfolioException {
		
		List<Integer> idList  = jdbcTemplate.query(Constants.GET_EDGE_QUERY,
					(rs, rowNum) -> rs.getInt("edge_id"), new Object[] { parentNodeId, childNodeId });
		
		if(idList.size() == Constants.RECORD_COUNT) {
			return true;
		} else if (idList.size() > Constants.RECORD_COUNT) {
			throw new InvalidPortfolioException("Found more than one records with the given parent_node_id = ("
					+ parentNodeId + ") and child_node_id = (" + childNodeId + ") ");
		}
		return false;		
	}
}