package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IPhotoAlbumService {

	@PreAuthorize("hasRole('ROLE_USER')")
	public PhotoAlbum addAlbum(HolidayGroup holidayGroup, PhotoSharingProvider provider, String url);

	public PhotoAlbum findPhotoAlbum(long albumId);

}
