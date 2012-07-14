package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.service.IEmailService;
import com.flurdy.grid.snaps.service.ISecurityService;
import com.flurdy.grid.snaps.service.ITravellerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class TravellerServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Mock
	private IEmailService emailService;

	@InjectMocks
	@Autowired
	private ITravellerRepository travellerRepository;

	@InjectMocks
	@Autowired
	private ISecurityService securityService;

	@InjectMocks
	@Autowired
	private ITravellerService travellerService;

	private long defaultTravellerId;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);		
		registerDefaultTraveller();
	}

	private void registerDefaultTraveller(){
		Traveller traveller = generateDefaultTraveller();
		securityService.registerTraveller(traveller);
		defaultTravellerId = traveller.getTravellerId();
		Assert.assertTrue( defaultTravellerId > 0 );
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(emailService);
	}

	@Test
	public void testFindTraveller(){

		Assert.assertTrue( defaultTravellerId > 0 );

		Traveller traveller = travellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );

		Mockito.verifyZeroInteractions(emailService);
	}



	@Test
	public void testFindAllTravellers(){

		List<Traveller> travellers = travellerService.findTravellers();

		Assert.assertTrue( travellers.size() == 1 );
		Mockito.verifyZeroInteractions(emailService);
	}
}
