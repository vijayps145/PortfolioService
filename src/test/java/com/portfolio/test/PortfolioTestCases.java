package com.portfolio.test;

import com.portfolio.graph.AppConfig;
import com.portfolio.graph.dao.EdgesDao;
import com.portfolio.graph.dao.NodesDao;
import com.portfolio.graph.dto.EdgeDTO;
import com.portfolio.graph.entity.Nodes;
import com.portfolio.graph.exception.InvalidPortfolioException;
import com.portfolio.graph.service.PortfolioServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PortfolioTestCases  {

	@InjectMocks
	PortfolioServiceImpl service;

	@Mock
	private NodesDao nodeDAO;

	@Mock
	private EdgesDao edgeDAO;

	List<String> exclusionList =  Arrays.asList("H1");

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testSaveNewNode()  {
		Nodes node = new Nodes();
		node.setName("I1");
		node.setLevel(1);
		try {
			doReturn(1).when(nodeDAO).save(node);
			assertEquals(1, service.persistNodes(node));
		} catch (Exception e) {
			fail("should not have thrown exception");
		}
	}


	@Test
	public void testAddEdge() {
		EdgeDTO edge = new EdgeDTO("I1","F1",2,0,0);
		try {
			doReturn(1).when(edgeDAO).save(edge);
			assertEquals(1, service.persistEdges(edge));
		} catch (Exception e) {
			fail("should not have thrown exception");
		}
	}


	@Test
	public void testMarketValueForFund() throws InvalidPortfolioException, Exception {
		doReturn(3000L).when(nodeDAO).getMarketValueForFund("F1");
		//setupData();
		assertEquals(3000L, service.getMarketValueForFund("F1"));
	}

	@Test
	public void testMarketValueForFund2() throws InvalidPortfolioException, Exception {
		// Mock setup
		doReturn(3000L).when(nodeDAO).getMarketValueForFund("F1");

		// Call method under test
		long marketValue = service.getMarketValueForFund("F1");

		// Verify interaction with mock
		verify(nodeDAO, times(1)).getMarketValueForFund("F1");

		assertEquals(3000L, marketValue);
	}

	@Test
	public void testMarketValueForInvestor() throws InvalidPortfolioException, Exception {
		doReturn(3750L).when(nodeDAO).getMarketValueForInvestor("I1");
		setupData();
		assertEquals(3750L, service.getMarketValueForInvestor("I1"));
	}


	@Test
	public void testMarketValueForInvestorByExclusionHoldings() throws InvalidPortfolioException, Exception {
		doReturn(2750L).when(nodeDAO).getMarketValueForInvestorWithExclusionList("I1",exclusionList);
		setupData();
		assertEquals(2750L, service.getMarketValueForInvestorWithExclusionList("I1",exclusionList));
	}


	@Test
	public void testMarketValueForFundByExclusionHoldings() throws InvalidPortfolioException, Exception {
		doReturn(2000L).when(nodeDAO).getMarketValueForFundWithExclusionList("F1",exclusionList);
		setupData();
		assertEquals(2000L, service.getMarketValueForFundWithExclusionList("F1",exclusionList));
	}

	@Test(expected = InvalidPortfolioException.class)
	public void testDuplicateEdgeInsertion() throws InvalidPortfolioException, Exception {

		service.persistEdges(new EdgeDTO("I1", "F1", 2, 0, 0));
		service.persistEdges(new EdgeDTO("I1", "F1", 2, 0, 0));

	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testInvalidLevelValueForNode() throws InvalidPortfolioException, Exception {
		Nodes node = new Nodes();
		node.setName("I1");
		node.setLevel(4);
		service.persistNodes(node);
	}


	@Test(expected = InvalidPortfolioException.class)
	public void testInvalidLevelValueForEdge() throws InvalidPortfolioException, Exception {
		service.persistEdges(new EdgeDTO("I1", "F1", 5, 0, 0));
	}

	/**
	 * This method is to setUp data to be used by test cases
	 * @throws InvalidPortfolioException
	 * @throws Exception
	 */
	public void setupData() throws InvalidPortfolioException, Exception {
		service.persistNodes(new Nodes("I1", 1 , 0));
		service.persistNodes(new Nodes("F1", 2 , 0));
		service.persistNodes(new Nodes("F2", 2 , 0));
		service.persistNodes(new Nodes("H2", 3 , 15));
		service.persistNodes(new Nodes("H3", 3 , 20));


		service.persistEdges(new EdgeDTO("I1", "F1", 2, 0 , 0));
		service.persistEdges(new EdgeDTO("I1", "F2", 2, 0, 0));
		service.persistEdges(new EdgeDTO("F1", "H1", 3, 100, 10));
		service.persistEdges(new EdgeDTO("F2", "H2", 3, 50, 15));
		service.persistEdges(new EdgeDTO("F1", "H3", 3, 100, 15));
	}

}
