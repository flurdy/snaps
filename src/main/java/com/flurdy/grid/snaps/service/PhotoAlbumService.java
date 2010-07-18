package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IPhotoAlbumRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.domain.Traveller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class PhotoAlbumService extends AbstractService implements IPhotoAlbumService {

	@Autowired ITravellerService travellerService;

	@Override
	public PhotoAlbum addAlbum(HolidayGroup holidayGroup, PhotoSharingProvider provider, String url) {

		final Traveller owner = travellerService.findCurrentTraveller(); 
		

		log.info("Traveller [" + owner.getSecurityDetail().getUsername()
				+ "] is adding album from provider ["+provider.name()
				+ "] to group ["+ holidayGroup.getGroupName()+"] with url: " + url);

		// validate url ?

		PhotoAlbum album = new PhotoAlbum.Builder()
				.sharingProvider(provider)
				.url(url)
				.holidayGroup(holidayGroup)
				.owner(owner)
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
