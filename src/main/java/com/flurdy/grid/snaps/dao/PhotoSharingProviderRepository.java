package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PhotoSharingProviderRepository extends AbstractRepository implements  IPhotoSharingProviderRepository {

	@Override
	public PhotoSharingProvider findProvider(String providerName) {

        assert providerName != null;
        assert providerName.length()>0;

		Query query = entityManager.createNamedQuery("sharingProvider.findByName");
		query.setParameter("providerName", providerName);
		try{
			return (PhotoSharingProvider) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
//		} catch (NonUniqueResultException ex) {
//			throw new TechnicalException(TechnicalError.DATA_ERROR,ex);
		}	

	}

}
