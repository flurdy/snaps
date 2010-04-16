package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.PhotoSharingProvider;

public interface IPhotoSharingProviderRepository {

	public PhotoSharingProvider findProvider(String providerName);

}
