package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.PhotoAlbum;
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

}
