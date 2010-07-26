package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			log.info("not member");
			throw new SnapAccessDeniedException(SnapAccessDeniedException.SnapAccessError.NOT_MEMBER);
		}

	}

	@Override
	public PhotoAlbum findPhotoAlbum(long albumId, long holidayId){

		final HolidayGroup holidayGroup = holidayGroupRepository.findHolidayGroup( holidayId );
		if( holidayGroup != null ){
			final Traveller traveller = travellerService.findCurrentTraveller();
			if( holidayGroup.isMember(traveller)){
				final PhotoAlbum photoAlbum = photoAlbumRepository.findAlbum(albumId);
				if( photoAlbum != null ){
					if( photoAlbum.getHolidayGroup().equals(holidayGroup) ){
						return photoAlbum;
					} else {
						log.info("Photo album was NOT for this holiday group");
						throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.PHOTO_ALBUM);
					}
				} else {
					throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.PHOTO_ALBUM);					
				}
			} else {
				log.info("not member");
				throw new SnapAccessDeniedException(SnapAccessDeniedException.SnapAccessError.NOT_MEMBER);
			}
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.HOLIDAY);
		}

	}


}
