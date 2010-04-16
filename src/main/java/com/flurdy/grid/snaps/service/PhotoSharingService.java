package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.PhotoSharingProvider;

public class PhotoSharingService implements IPhotoSharingProviderService {

	@Override
	public PhotoSharingProvider findProvider(String providerName) {

		PhotoSharingProvider provider = photoSharingRepository.findProvider(providerName);


	}

}
