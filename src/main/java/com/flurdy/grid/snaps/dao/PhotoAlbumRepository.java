package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.Traveller;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PhotoAlbumRepository extends AbstractRepository implements IPhotoAlbumRepository {

	@Override
	public void addAlbum(PhotoAlbum album) {

		assert album != null;
		entityManager.persist(album);

	}




	@Override
	public PhotoAlbum findAlbum(long albumId) {

		assert albumId > 0;

		Query query = entityManager.createNamedQuery("photoAlbum.findById");
		query.setParameter("albumId", albumId);
		try {
			return (PhotoAlbum) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
//		} catch (NonUniqueResultException ex) {
//			throw new TechnicalException(TechnicalError.DATA_ERROR,ex);
		}
	}

}
