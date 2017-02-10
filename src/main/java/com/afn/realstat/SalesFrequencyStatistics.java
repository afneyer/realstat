package com.afn.realstat;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SalesFrequencyStatistics {

	public static final Logger importLog = LoggerFactory.getLogger("import");

	@Autowired
	PropertyTransactionRepository ptRepo;

	@Autowired
	RealPropertyRepository rpRepo;

	public SalesFrequencyStatistics() {
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
				List<PropertyTransaction> transForProperty = ptRepo.findByRealPropertyAndCloseDateBefore(rp, pt1.getCloseDate(), sort);

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

}