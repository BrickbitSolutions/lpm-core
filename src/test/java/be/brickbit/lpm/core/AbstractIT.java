package be.brickbit.lpm.core;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractIT {
	@Autowired
	private EntityManager entityManager;

	protected void insert(Object... entities) throws Exception {
		for (Object entity : entities) {
			save(entity);
		}
	}

	private void save(Object object) {
		if (entityManager.contains(object)) {
			entityManager.merge(object);
		} else {
			entityManager.persist(object);
		}
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
