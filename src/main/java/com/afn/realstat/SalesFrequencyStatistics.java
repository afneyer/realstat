package com.afn.realstat;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SalesFrequencyStatistics {

	public static final Logger importLog = LoggerFactory.getLogger("app");

	private String defaultFilePath;

	@Autowired
	PropertyTransactionRepository ptRepo;

	@Autowired
	RealPropertyRepository rpRepo;

	@Autowired
	DataSource afnDataSource;

	public SalesFrequencyStatistics() {
		// TODO retrieve base directory
		defaultFilePath = "C:\\afndev\\apps\\realstat\\logs\\analytics\\";
	}

	@Transactional
	public void timeBetweenSales2016(String fileName) {

		String header = "Apn, Street, City, Zip, Recent MLS, Recent Date, Earlier MLS, Earlier Date, Earlier Years";
		CsvFileWriter cfw = new CsvFileWriter(fileName, header);

		// find all propertyTransactions for 2016
		Date d1 = AfnDateUtil.of(2016, 1, 1);
		Date d2 = AfnDateUtil.of(2017, 1, 1);
		List<PropertyTransaction> pt2016 = ptRepo.findByCloseDateBetween(d1, d2);

		for (PropertyTransaction pt1 : pt2016) {
			RealProperty rp = pt1.getRealProperty();

			if (rp != null) {
				// find all transactions for the property
				Sort sort = new Sort(Direction.DESC, "closeDate");
				List<PropertyTransaction> transForProperty = ptRepo.findByRealPropertyAndCloseDateBefore(rp,
						pt1.getCloseDate(), sort);

				// assertTrue(transForProperty.size() > 0);

				// verify the first transaction is the property itself
				// assertTrue(transForProperty.get(0) == pt1);

				if (transForProperty.size() >= 1) {
					PropertyTransaction pt2 = transForProperty.get(0);
					d1 = pt2.getCloseDate();
					d2 = pt1.getCloseDate();
					// find years between closing dates
					long years = AfnDateUtil.yearsBetween(d1, d2);

					String line = new String();
					line += rp.getApn() + ",";
					line += rp.getPropertyAddress() + ",";
					line += rp.getPropertyCity() + ",";
					line += rp.getPropertyZip() + ",";
					line += pt1.getMlsNo() + ",";
					line += pt1.getCloseDate() + ",";
					line += pt2.getMlsNo() + ",";
					line += pt2.getCloseDate() + ",";
					line += years;

					cfw.appendLine(line);
				}
			}
		}
		cfw.close();

	}

	public void salesByYearCityAndZip() {

		String query = "select year(closeDate) as closeYear, city, substring(zip,1,5) as zipCode, count(id) \n"
				+ "from property_transaction \n" + "where closeDate is not null \n"
				+ "group by closeYear, city, zipCode \n";

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFileName());

	}

	public void doubleEndedTransactionsByAgent() {

		String query = "select sum(case when(pt.listingAgent_id = pt.sellingAgent_id) then 1 else 0 end) as doubleEnded, \n"
				+ "(100*sum(case when(pt.listingAgent_id = pt.sellingAgent_id) then 1 else 0 end)/count(pt.id)) as percentDoubleEnded, count(pt.id) as allTrans, a.agentName \n"
				+ "from property_transaction pt, agent a \n"
				+ "where pt.sellingAgent_id = a.id or pt.listingAgent_id = a.id \n" + "group by a.agentName \n"
				+ "having count(pt.id) > 20 \n" + "order by percentDoubleEnded desc \n";
		
		CsvFileWriter.writeQueryResult(afnDataSource, query, getFileName());
	}
	
	public void checkPropertyLinking() {

		String query = "select count(id) as totalCnt, count(realProperty_id) as linkedCnt, \n"
				+ "100*count(realProperty_id)/count(id) as linkedPercent, substring(zip,1,5) as zipCode, city \n"
				+ "from property_transaction where substring(zip,1,5) in ('94610', '94611', '94618') group by zipCode, city;";

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFileName());
	}

	public void checkAgentLinking() {

		String query = "select status, \n"
				+ "count(ListingAgentName), count(SellingAgent1Name), count(CoListAgentName), count(CoSellAgentName), \n"
				+ "count(ListingAgentLicenseID), count(SellingAgent1LicenseId), count(CoListAgentLicenseId), count(CoSellAgentLicenseId), \n"
				+ "count(ListAgentBreNum), count(SoldAgentBreNum), count(CoListAgentBreNum), count(CoSellAgentBreNum), \n"
				+ "count(listingAgent_id), count(sellingAgent_id), count(listingAgent2_id), count(sellingAgent2_id) \n"
				+ "from property_transaction group by status \n";
		
		System.out.print(query);

		CsvFileWriter.writeQueryResult(afnDataSource, query, getFileName());
	}

	private String getFileName() {

		String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
		String fileName = functionName + ".csv";
		String fullFileName = defaultFilePath + fileName;
		return fullFileName;

	}

}
