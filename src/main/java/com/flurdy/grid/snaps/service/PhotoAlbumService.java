package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IPhotoAlbumRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoAlbumService implements IPhotoAlbumService {

	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired IPhotoAlbumRepository photoAlbumRepository;

	@Override
	public PhotoAlbum addAlbum(HolidayGroup holidayGroup, PhotoSharingProvider provider, String url) {

		log.debug("Adding album from provider ["+provider.name() + "] to group ["+ holidayGroup.getGroupName()+"] with url: " + url);

		// validate url ?

		PhotoAlbum album = new PhotoAlbum.Builder()
				.sharingProvider(provider)
				.url(url)
				.build();

		photoAlbumRepository.addAlbum(album);

		return album;
	}

	@Override
	public PhotoAlbum findPhotoAlbum(long albumId){

		log.debug("find album:"+albumId);

		return photoAlbumRepository.findAlbum(albumId);

	}


}
