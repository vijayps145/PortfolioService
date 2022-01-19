# PortfolioService
 
PortfolioService is a project to build a three level graph between Investor, Fund and Holding. 
In this project, nodes at level 1, 2 and 3 refers to investor, fund and holdings respectively.
For leaf node which is level 3 node, there is a weight attribute which is non-zero for level 3 and can be zero for other levels.
In addition to weight attribute, edge between level 2 and level 3 has quantity which signifies the holding quantity for the fund.

Portfolio Service offers following features:
1. User can add node at any level on this graph and add edge/link between those nodes.
2. Get market value for investor based on the investor id.
3. Get market value for fund based on the fund id.
4. Get market value for investor/fund after excluding list of holdings.


To run this project, 
1. Clone this repository on you machine and then import this as a maven project in eclipse or any IDE.
2. Select the pom.xml file, right click and run as maven install to download the required maven dependencies.
3. Project uses lombok dependency, which needs to be integrated with IDE in order to successsfully compile the project.
4. Go to the com.portfolio.test package and then right click PortfolioTestCases class file and go to the "Run Configurations".
5. Select "Test Runner" as "JUnit4" and hit the run button to run the test cases.