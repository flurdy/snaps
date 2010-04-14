package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import java.util.List;
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

//		traveller = findTraveller(traveller.getTravellerId());

		assert traveller != null;
		assert traveller.getTravellerId() != null;
		assert traveller.getTravellerId() > 0;

//		SecurityDetail securityDetail = entityManager.getReference(SecurityDetail.class, traveller.getSecurityDetail().getUsername());

//		Traveller realTraveller = entityManager.find(Traveller.class, traveller.getTravellerId());

//		traveller = entityManager.getReference(Traveller.class, traveller.getTravellerId());

//		log.debug("REmoving:"+traveller);
//		entityManager.remove(securityDetail);
//		entityManager.merge(traveller);
		traveller = entityManager.getReference(Traveller.class, traveller.getTravellerId());
		entityManager.remove(traveller);

		entityManager.flush();
	}
}
