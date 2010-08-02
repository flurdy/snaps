package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
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
	public Long addHolidayGroup(HolidayGroup holidayGroup) {
		assert holidayGroup != null;
		entityManager.persist(holidayGroup);
		return holidayGroup.getGroupId();
	}

	@Override
	public HolidayGroup findHolidayGroup(Long groupId) {
		assert groupId > 0;
		Query query = entityManager.createNamedQuery("holidayGroup.findById");
		query.setParameter("groupId", groupId);
		try {
			return (HolidayGroup) query.getSingleResult();
		} catch (NoResultException e) {
//			query = entityManager.createNamedQuery("holidayGroup.findById");
//			query.setParameter("groupId", groupId);
//			log.debug("hgggg:"+((HolidayGroup) query.getSingleResult()));
			return null;
//		} catch (NonUniqueResultException ex) {
//			throw new TechnicalException(TechnicalError.DATA_ERROR,ex);
		}
	}

	@Override
	public Collection<HolidayGroup> findHolidayGroups(String groupName) {

		Query query = entityManager.createQuery(
				"select hg from HolidayGroup hg where hg.groupName like '%"+ groupName +"%' order by hg.groupId" );
		@SuppressWarnings("unchecked")
		List<HolidayGroup> results = query.getResultList();
		return results;

	}

	@Override
	public void updateHolidayGroup(HolidayGroup holidayGroup) {
		assert holidayGroup != null;
		entityManager.merge(holidayGroup);	
	}

	@Override
	public Collection<HolidayGroup> findAllHolidayGroups() {
		Query query = entityManager.createQuery(
				"select hg from HolidayGroup hg order by hg.groupId" );
		@SuppressWarnings("unchecked")
		List<HolidayGroup> results = query.getResultList();
		return results;
	}

	@Override
	public void deleteHolidayGroup(HolidayGroup holidayGroup) {

		assert holidayGroup != null;
		Query query = entityManager.createNamedQuery("holidayGroup.findById");
		query.setParameter("groupId", holidayGroup.getGroupId() );
		holidayGroup = (HolidayGroup) query.getSingleResult();

		if( holidayGroup.getMembers() != null ){
			for( HolidayMember holidayMember : holidayGroup.getMembers()){
			}
		}

		if( holidayGroup.getPhotoAlbums() != null ){
			for( PhotoAlbum photoAlbum: holidayGroup.getPhotoAlbums()){
			}
		}

		entityManager.remove(holidayGroup);

		entityManager.flush();

	}


}
