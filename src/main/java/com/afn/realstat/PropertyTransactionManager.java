package com.afn.realstat;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class PropertyTransactionManager {

	public static final Logger importLog = LoggerFactory.getLogger("import");

	@Autowired
	PropertyTransactionRepository ptRepo;

	@Autowired
	RealPropertyRepository rpRepo;

	@Autowired
	AgentRepository agtRepo;

	@PersistenceContext
	EntityManager em;

	@PersistenceUnit
	EntityManagerFactory emf;

	@Autowired
	PlatformTransactionManager ptm;

	public PropertyTransactionManager() {
	}

	public List<PropertyTransaction> getAllPropertyTransactionsIterable(int offset, int max) {
		final TypedQuery<PropertyTransaction> tq = em.createQuery("SELECT pt FROM PropertyTransaction pt",
				PropertyTransaction.class);
		tq.setFirstResult(offset);
		tq.setMaxResults(max);
		List<PropertyTransaction> result = tq.getResultList();

		return result;
	}

	// @Transactional
	public void iterateAll() {
		int offset = 0;
		int batchSize = 100;

		List<PropertyTransaction> listPt;

		listPt = getAllPropertyTransactionsIterable(offset, batchSize);
		while (listPt.size() > 0) {

			// tm.getTransaction(null); TODO
			// DefaultTransactionDefinition dtd = new
			// DefaultTransactionDefinition();
			// dtd.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
			// TransactionStatus ts = ptm.getTransaction(dtd);
			performEntityAction(listPt);

			// Print this out
			// int i = 0; TODO
			// for (PropertyTransaction pt : listPt) {
			// String state = pt.getState();
			// importLog.info("Offset = " + offset + " listIndex = " + i + "
			// state = " + state + " pt = " + pt.toString() );
			// i++;
			// }
			//
			ptRepo.save(listPt);
			// ts.flush();
			// ptm.commit(ts);

			// em.flush();
			// em.clear();
			// em.getTransaction().commit();
			offset += batchSize;
			listPt = getAllPropertyTransactionsIterable(offset, batchSize);
		}
	}

	public void performEntityAction(List<PropertyTransaction> entityList) {

		int i = 0;
		for (PropertyTransaction pt : entityList) {

			// DebugUtils.transactionRequired("PropertyTransactionManager.performEntityAction");
			importLog.info("index =" + i + " iterating over PT=" + pt.toString());
			linkPropertyTransactionToRealProperty(pt);
			linkPropertyTransactionToAgents(pt);
			// repository.save(pt)
			i++;
		}

	}

	@Transactional
	public void linkPropertyTransactionToRealProperty(PropertyTransaction pt) {

		RealProperty rp = null;
		String apnClean = RealStatUtil.cleanApn(pt.getApn());
		List<RealProperty> list = rpRepo.findByApnClean(apnClean);

		if (list.size() == 1) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.info("Property Transaction Linked By APN= " + pt.toString());
			return;
		}

		if (list.size() == 0) {
			// no corresponding property found
			importLog.warn("No property found by APN=" + pt.getApn() + " apnClean =" + apnClean
					+ " for propertyTransaction = " + pt.toString());
			this.linkPropertyTransactionToRealPropertyByAddress(pt);
			return;
		}

		for (RealProperty rp1 : list) {
			importLog.warn("Multiple properties by APN! pt = " + pt.toString() + "rp = " + rp1.toString());
			this.linkPropertyTransactionToRealPropertyByAddress(pt);
			return;
		}

	}

	@Transactional
	public void linkPropertyTransactionToRealPropertyByAddress(PropertyTransaction pt) {

		RealProperty rp = null;
		Address adrClean = new Address(pt.getAddress(), pt.getUnit(), pt.getCity(), pt.getZip());
		String adrCleanStr = adrClean.getCleanAddress();
		List<RealProperty> list = rpRepo.findByAddressClean(adrCleanStr);

		if (list.size() == 1) {
			rp = list.get(0);
			pt.setRealProperty(rp);
			importLog.warn("Property Transaction Linked By Address= " + pt.toString());
			return;
		}

		if (list.size() == 0) {
			// no corresponding property found
			importLog.warn("No property found by Adress");
			importLog.warn("raw Address=" + adrClean.getRawAddress());
			importLog.warn("clean Address" + adrClean.getCleanAddress());
			importLog.warn("prop trans=" + pt.toString());
			return;
		}

		for (RealProperty rp1 : list) {
			importLog.warn("Multiple properties by Adress! pt = " + pt.toString() + "rp = " + rp1.toString());
			return;
		}

	}

	@Transactional
	public void linkPropertyTransactionToAgents(PropertyTransaction pt) {

		// Link Listing Agent
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getListingAgentLicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getListingAgentName();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setListingAgent(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}

		// Link Selling Agent
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getSellingAgent1LicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getSellingAgent1Name();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setSellingAgent(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}
		// Link Listing Agent1
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getCoListAgentLicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getCoListAgentName();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setListingAgent2(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}
		// Link Selling Agent1
		{
			Function<PropertyTransaction, String> licenseGetter = pt1 -> {
				return pt1.getCoSellAgentLicenseId();
			};
			Function<PropertyTransaction, String> agentNameGetter = pt1 -> {
				return pt1.getCoSellAgentName();
			};
			BiFunction<PropertyTransaction, Agent, String> agentSetter = (pt1, agt) -> {
				pt.setSellingAgent2(agt);
				return null;
			};
			linkPropertyTransactionToAgent(pt, licenseGetter, agentNameGetter, agentSetter);
		}
	}

	@Transactional
	public void linkPropertyTransactionToListingAgent(PropertyTransaction pt) {
		Agent agt = null;
		String lic = pt.getListingAgentLicenseId();

		List<Agent> list = agtRepo.findByLicense(lic);
		if (list.size() == 1) {
			agt = list.get(0);
			pt.setListingAgent(agt);
			importLog.info("Property Transaction Linked to Listing Agent by LicenseId= " + pt.toString());
			return;
		}

		linkPropertyTransactionToListingAgentByAddress(pt);

		/*
		 * if (list.size() == 0) { // no corresponding property found
		 * importLog.warn("No property found by APN=" + pt.getApn() +
		 * " apnClean =" + apnClean + " for propertyTransaction = " +
		 * pt.toString() );
		 * this.linkPropertyTransactionToRealPropertyByAddress(pt); return; }
		 * 
		 * for (RealProperty rp1 : list) {
		 * importLog.warn("Multiple properties by APN! pt = " + pt.toString() +
		 * "rp = " + rp1.toString() );
		 * this.linkPropertyTransactionToRealPropertyByAddress(pt); return; }
		 */

	}

	private void linkPropertyTransactionToListingAgentByAddress(PropertyTransaction pt) {
		Agent agt = null;
		String nameRaw = pt.getListingAgentName();
		PersName name = new PersName(nameRaw);
		if (name.getFirstName() != null && name.getLastName() != null) {

			List<Agent> list = agtRepo.findByFirstNameAndLastName(name.getFirstName(), name.getLastName());
			if (list.size() == 1) {
				agt = list.get(0);
				pt.setListingAgent(agt);
				importLog.info("Property Transaction Linked to Listing Agent by Name= " + name.getFirstName() + " "
						+ name.getLastName());
				return;
			}
		}

		importLog.info("Could not link Property Transaction to listing agent:" + "license="
				+ pt.getListingAgentLicenseId() + "  name=" + name.getFirstName() + " " + name.getLastName());

	}

	@Transactional
	public void linkPropertyTransactionToAgent(PropertyTransaction pt, Function<PropertyTransaction, String> licGetter,
			Function<PropertyTransaction, String> agentNameGetter,
			BiFunction<PropertyTransaction, Agent, String> agentSetter) {
		Agent agt = null;
		String lic = licGetter.apply(pt);

		List<Agent> list = agtRepo.findByLicense(lic);
		if (list.size() == 1) {
			agt = list.get(0);
			agentSetter.apply(pt, agt); // pt.setListingAgent(agt);
			importLog.info("Property Transaction Linked by LicenseId= " + pt.toString() + "  Using: ", licGetter.toString());
			return;
		}

		linkPropertyTransactionToAgentByAddress(pt, agentNameGetter, agentSetter);

		/*
		 * if (list.size() == 0) { // no corresponding property found
		 * importLog.warn("No property found by APN=" + pt.getApn() +
		 * " apnClean =" + apnClean + " for propertyTransaction = " +
		 * pt.toString() );
		 * this.linkPropertyTransactionToRealPropertyByAddress(pt); return; }
		 * 
		 * for (RealProperty rp1 : list) {
		 * importLog.warn("Multiple properties by APN! pt = " + pt.toString() +
		 * "rp = " + rp1.toString() );
		 * this.linkPropertyTransactionToRealPropertyByAddress(pt); return; }
		 */

	}

	private void linkPropertyTransactionToAgentByAddress(PropertyTransaction pt,
			Function<PropertyTransaction, String> agentNameGetter,
			BiFunction<PropertyTransaction, Agent, String> agentSetter) {
		Agent agt = null;
		String nameRaw = agentNameGetter.apply(pt);
		PersName name = new PersName(nameRaw);
		if (name.getFirstName() != null && name.getLastName() != null) {

			List<Agent> list = agtRepo.findByFirstNameAndLastName(name.getFirstName(), name.getLastName());
			if (list.size() == 1) {
				agt = list.get(0);
				agentSetter.apply(pt, agt);
				importLog.info("Property Transaction Linked to Agent by Name= " + name.getFirstName() + " "
						+ name.getLastName() + "  Using: ", agentNameGetter.toString());
				return;
			}
		}

		importLog.info("Could not link Property Transaction to listing agent:" + "license="
				+ pt.getListingAgentLicenseId() + "  name=" + name.getFirstName() + " " + name.getLastName());

	}

	private void linkPropertyTransactionToSellingAgentByAddress(PropertyTransaction pt) {
		Agent agt = null;
		String nameRaw = pt.getSellingAgent1Name();
		PersName name = new PersName(nameRaw);
		if (name.getFirstName() != null && name.getLastName() != null) {

			List<Agent> list = agtRepo.findByFirstNameAndLastName(name.getFirstName(), name.getLastName());
			if (list.size() == 1) {
				agt = list.get(0);
				pt.setSellingAgent(agt);
				importLog.info("Property Transaction Linked to Selling Agent by Name= " + name.getFirstName() + " "
						+ name.getLastName());
				return;
			}
		}

		importLog.info("Could not link Property Transaction to listing agent:" + "license="
				+ pt.getListingAgentLicenseId() + "  name=" + name.getFirstName() + " " + name.getLastName());

	}

	// TODO (iterate using pagable)
	/*
	 * final int pageLimit = 300; int pageNumber = 0; Page<T> page =
	 * repository.findAll(new PageRequest(pageNumber, pageLimit)); while
	 * (page.hasNextPage()) { processPageContent(page.getContent()); page =
	 * repository.findAll(new PageRequest(++pageNumber, pageLimit)); } //
	 * process last page processPageContent(page.getContent());
	 * 
	 * or
	 * 
	 * do{ page = repository.findAll(new PageRequest(pageNumber, pageLimit));
	 * pageNumber++;
	 * 
	 * }while (!page.isLastPage());
	 * 
	 */
}
