package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TravellerRepository extends AbstractRepository implements ITravellerRepository {

	@Override
	public void addTraveller(Traveller traveller) {

		assert traveller != null;

//		SecurityDetail securityDetail = traveller.getSecurityDetail();

//		entityManager.persist(sec);

		entityManager.persist(traveller);

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Traveller> findAllTravellers() {
		return entityManager.createNamedQuery("traveller.findAll").getResultList();
	}

	@Override
	public Traveller findTraveller(long travellerId) {

		assert travellerId > 0;

		Query query = entityManager.createNamedQuery("traveller.findById");
		query.setParameter("travellerId", travellerId);
		try {
			return (Traveller) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
//		} catch (NonUniqueResultException ex) {
//			throw new TechnicalException(TechnicalError.DATA_ERROR,ex);
		}
	}

	@Override
	public void updateTraveller(Traveller traveller) {

		assert traveller != null;
		assert traveller.getTravellerId() != null && traveller.getTravellerId() > 0;
		entityManager.merge(traveller);
		entityManager.flush();
	}

	@Override
	public void deleteTraveller(Traveller traveller) {
		assert traveller != null;
		assert traveller.getTravellerId() != null;
		assert traveller.getTravellerId() > 0;

		Query query = entityManager.createNamedQuery("traveller.findFullById");
		query.setParameter("travellerId", traveller.getTravellerId());
		traveller = (Traveller) query.getSingleResult();

		assert traveller != null;
		assert traveller.getTravellerId() != null;
		assert traveller.getTravellerId() > 0;

		if( traveller.getPhotoAlbums() != null ){
			for(PhotoAlbum photoAlbum : traveller.getPhotoAlbums() ){
				entityManager.remove(photoAlbum);
			}
		}
		traveller.setPhotoAlbums(null);

		if( traveller.getHolidayMemberships() != null ){
			for(HolidayMember holidayMember : traveller.getHolidayMemberships() ){
				entityManager.remove(holidayMember);
			}
		}
		traveller.setHolidayMemberships(null);

		SecurityDetail securityDetail = entityManager.getReference(SecurityDetail.class, traveller.getSecurityDetail().getUsername());

		traveller.setSecurityDetail(null);

		entityManager.remove( securityDetail );

		entityManager.merge(traveller);

		entityManager.remove(traveller);
		
		entityManager.flush();


	}

	@Override
	public Traveller findTraveller(String username) {
		assert username != null;

		Query query = entityManager.createNamedQuery("traveller.findByUsername");
		query.setParameter("username", username);
		try {
			return (Traveller) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
//		} catch (NonUniqueResultException ex) {
//			throw new TechnicalException(TechnicalError.DATA_ERROR,ex);
		}
	}
}




