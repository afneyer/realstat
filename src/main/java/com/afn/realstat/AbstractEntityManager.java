package com.afn.realstat;

import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AbstractEntityManager<T extends AbstractEntity> {

	public static final Logger log = LoggerFactory.getLogger("app");

	AbstractEntityRepository<T> repo;

	public void performActionOnEntities(Function<T, Boolean> action) {

		int batchSize = 1000;
		int maxBatches = 10000;
		this.performActionOnEntities(action, batchSize, maxBatches);

	}

	public void performActionOnEntities(Function<T, Boolean> action, int batchSize, int maxBatches) {

		// set the paging parameters (batch size)
		int i = 0;
		PageRequest pr = new PageRequest(i, batchSize);
		Page<T> entities = repo.findAll(pr);

		// iterate over all pages
		while (entities.hasContent() && i < maxBatches) {

			List<T> list = entities.getContent();
			performActionOnBatch(action, list);

			i++;
			pr = new PageRequest(i, batchSize);
			entities = repo.findAll(pr);
		}
	}

	@Transactional
	private void performActionOnBatch(Function<T, Boolean> action, List<T> list) {
		for (T e : list) {
			Boolean result = action.apply(e);
			if (!result) {
				log.error("Cannot apply function = " + action.toString() + " to entity " + e);
			}
		}
		System.out.println("-First Entity:" + list.get(0) + "  -Last Entity:" + list.get(list.size()-1) + "  -BatchSize:" + list.size());
		repo.save(list);
		repo.flush();

	}

}
