/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.dao;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HolidayGroupRepository implements IHolidayGroupRepository {

	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
    protected EntityManager entityManager;

	@Override
	public void addHolidayGroup(HolidayGroup holidayGroup) {
		assert holidayGroup != null;
		entityManager.persist(holidayGroup);		
	}

	@Override
	public HolidayGroup findHolidayGroup(Long groupId) {
		assert groupId > 0;
		return entityManager.find(HolidayGroup.class, groupId);
	}

	@Override
	public Collection<HolidayGroup> findHolidayGroups(String groupName) {

		// TODO convert to name query
		Query query = entityManager.createQuery(
				"select hg from HolidayGroup hg where hg.groupName like '%"+ groupName +"%'" );
		@SuppressWarnings("unchecked")
		List<HolidayGroup> results = query.getResultList();
		return results;

	}


}
