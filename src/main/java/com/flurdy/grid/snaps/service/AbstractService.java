package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.IPhotoAlbumRepository;
import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {


	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected ISecurityRepository securityRepository;

	@Autowired
	protected IPhotoAlbumRepository photoAlbumRepository;

	@Autowired
	protected ITravellerRepository travellerRepository;

	@Autowired
	protected IHolidayGroupRepository holidayGroupRepository;


}
