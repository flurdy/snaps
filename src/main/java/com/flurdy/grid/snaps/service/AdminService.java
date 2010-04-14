package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.Traveller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService implements IAdminService {

	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired ITravellerRepository travellerRepository;

	@Override
	public void updateTraveller(Traveller traveller) {

		// TODO validate traveller

		Traveller realTraveller = travellerRepository.findTraveller(traveller.getTravellerId());

		realTraveller.setFullname(traveller.getFullname());
		realTraveller.setEmail(traveller.getEmail());

		travellerRepository.updateTraveller(realTraveller);

	}

	@Override
	public void deleteTraveller(long travellerId) {

		Traveller traveller = travellerRepository.findTraveller(travellerId);

		log.info("Deleting traveller"+ traveller);

		travellerRepository.deleteTraveller(traveller);


	}

}
