package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IPhotoSharingProviderRepository;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoSharingService implements IPhotoSharingProviderService {
	
	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired IPhotoSharingProviderRepository sharingProviderRepository;

	@Override
	public PhotoSharingProvider findProvider(String providerName) {

		log.debug("Finding provider: "+ providerName );

		PhotoSharingProvider provider = sharingProviderRepository.findProvider(providerName);

		return provider;		
	}

}
