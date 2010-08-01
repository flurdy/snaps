package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService implements IAdminService {

	protected transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired ITravellerRepository travellerRepository;

	@Autowired ITravellerService travellerService;

	@Override
	public void updateTraveller(Traveller traveller) {

		if( traveller != null ){
			if( traveller.isValid() ){

				final Traveller admin = travellerService.findCurrentTraveller();
				log.info("Admin:" + admin + "| is updating: " + traveller);

				Traveller realTraveller = travellerRepository.findTraveller(traveller.getTravellerId());
				if( realTraveller != null ){

					realTraveller.setFullname(traveller.getFullname());
					realTraveller.setEmail(traveller.getEmail());

					travellerRepository.updateTraveller(realTraveller);

				} else {
					throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.TRAVELLER);
				}
			} else {
				throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_INPUT);
			}
		} else {
			throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.INVALID_INPUT,"Traveller is null");
		}
	}

	@Override
	public void deleteTraveller(long travellerId) {

		assert travellerId > 0;

		Traveller traveller = travellerRepository.findTraveller(travellerId);

		if( traveller != null ){

			Traveller admin = travellerService.findCurrentTraveller();
			log.info("Admin:" + admin + "| is deleting: " + traveller);

			travellerRepository.deleteTraveller(traveller);
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.TRAVELLER);
		}


	}

}
