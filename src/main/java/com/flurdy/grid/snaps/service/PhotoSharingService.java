package com.flurdy.grid.snaps.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PhotoSharingService implements IPhotoSharingProviderService {
	
	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

//	@Autowired IPhotoSharingProviderRepository sharingProviderRepository;
//
//	@Override
//	public PhotoSharingProvider findProvider(String providerName) {
//
//		log.debug("Finding provider: "+ providerName );
//
////		PhotoSharingProvider provider = sharingProviderRepository.findProvider(providerName);
//
//		return provider;
//	}

}
