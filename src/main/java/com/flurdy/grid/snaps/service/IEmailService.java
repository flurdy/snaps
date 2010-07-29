package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;


public interface IEmailService {


	public void sendPassword(Traveller traveller, String password);

	public void notifyNewRegistration(Traveller traveller);
	
}
