package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.PhotoAlbum;

public interface IPhotoAlbumRepository {

	public void addAlbum(PhotoAlbum album);

	public PhotoAlbum findAlbum(long albumId);

	public void deleteAlbum(long albumId);
}
