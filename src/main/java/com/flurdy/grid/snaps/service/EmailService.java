package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.stereotype.Service;

@Service
public class EmailService extends AbstractService implements IEmailService {


	@Override
	public void sendPassword(Traveller traveller, String password) {


		// throw new UnsupportedOperationException("Not yet implemented");
		log.error("Not yet implemented");


	}
}
