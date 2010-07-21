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

		// todo: validate url ?

		if( holidayGroup.isMember(owner)){

			PhotoAlbum album = new PhotoAlbum.Builder()
					.sharingProvider(provider)
					.url(url)
					.holidayGroup(holidayGroup)
					.owner(owner)
					.build();

			photoAlbumRepository.addAlbum(album);

			return album;

		} else {
			log.warn("Traveller is NOT a member of this group");
			throw new RuntimeException("Traveller was NOT a member of this holiday group");
		}

	}

	@Override
	public PhotoAlbum findPhotoAlbum(long albumId, long holidayId){

		final HolidayGroup holidayGroup = holidayGroupRepository.findHolidayGroup( holidayId );
		final Traveller traveller = travellerService.findCurrentTraveller();
		if( holidayGroup.isMember(traveller)){
			final PhotoAlbum photoAlbum = photoAlbumRepository.findAlbum(albumId);
			if( photoAlbum.getHolidayGroup().equals(holidayGroup)){
				log.debug("Traveller is a member of this group");
				return photoAlbum;
			} else {
				log.warn("Album is NOT for this group");
				throw new RuntimeException("Photo album was NOT a for this holiday group");
			}
		} else {
			log.warn("Traveller is NOT a member of this group");
			throw new RuntimeException("Traveller was NOT a member of this holiday group");
		}

	}


}
