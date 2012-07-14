package com.flurdy.grid.snaps.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

public abstract class AbstractRepository {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	protected EntityManager entityManager;

}
