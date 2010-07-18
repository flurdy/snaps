/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.Traveller;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravellerService extends AbstractService implements ITravellerService {

	@Autowired ISecurityService securityService;

	@Override
	public List<Traveller> findTravellers() {
		return travellerRepository.findAllTravellers();
	}

	@Override
	public Traveller findTraveller(long travellerId) {
		if ( travellerId  > 0) {
			return travellerRepository.findTraveller(travellerId);
		} else {
			return null;
		}
	}

	@Override
	public Traveller findCurrentTraveller() {
		final String username = securityService.findLoggedInUsername();
		return travellerRepository.findTraveller(username);
	}


}
