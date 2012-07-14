package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.SecurityDetail;
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
import org.springframework.security.authentication.encoding.PasswordEncoder;

import java.util.HashSet;
import java.util.Iterator;

public class SecurityServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Mock
	private IEmailService emailService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ITravellerService travellerService;

	@InjectMocks
	@Autowired
	private ISecurityRepository securityRepository;

	@InjectMocks
	@Autowired
	private ISecurityService securityService;

	@InjectMocks
	@Autowired
	private ITravellerService realTravellerService;

	private static final String TRAVELLER_USERNAME = "travelling testuser";
	private static final String TRAVELLER_PASSWORD = "travelling testuser";
	private static final String TRAVELLER_FULLNAME = "travelling testuser";
	private static final String TRAVELLER_EMAIL = "travelling.testuser@example.com";


	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}


	private Long registerDefaultTraveller(){
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.<Object>anyObject())).thenReturn(ENCODED_PASSWORD);
		Traveller traveller = generateDefaultTraveller();
		securityService.registerTraveller(traveller);
		long defaultTravellerId = traveller.getTravellerId();
		Assert.assertTrue( defaultTravellerId > 0 );
//		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
//		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.<Object>anyObject());
//		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);
		return defaultTravellerId;
	}

	private Long registerAdminTraveller(){
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.<Object>anyObject())).thenReturn(ENCODED_PASSWORD);
		Traveller traveller = generateAdminTraveller();
		securityService.registerTraveller(traveller);
		long travellerId = traveller.getTravellerId();
		Assert.assertTrue( travellerId > 0 );
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.<Object>anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder,travellerService);
		return travellerId;
	}

	private Long registerAdminTravellerAndEscalateToAdmin(){

		long adminId = registerAdminTraveller();
//		securityService.addAuthority(ADMIN_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );		
		SecurityDetail securityDetail = securityService.findLogin(ADMIN_USERNAME);
		securityDetail.addAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN);
		securityRepository.updateSecurityDetail(securityDetail);

		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);
		return adminId;
	}

	@Test
	public void testFindLogin(){

		long defaultTravellerId = registerDefaultTraveller();

		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);

		SecurityDetail securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getUsername(), DEFAULT_USERNAME );

		Mockito.verifyZeroInteractions(emailService,passwordEncoder,travellerService);

	}



	@Test
	public void testRegisterTraveller(){
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.<Object>anyObject())).thenReturn(ENCODED_PASSWORD);

		Traveller traveller =  new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		securityService.registerTraveller(traveller);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getUsername(), TRAVELLER_USERNAME );

		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder,travellerService);
	}


	@Test
	public void testAddAuthority(){

		long adminId = registerAdminTravellerAndEscalateToAdmin();

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(adminId));

		registerDefaultTraveller();

		Mockito.verify(emailService, Mockito.times(2)).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder, Mockito.times(2)).encodePassword(Mockito.anyString(),Mockito.<Object>anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);

		SecurityDetail securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		securityService.addAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 2 );

		Iterator<SecurityDetail.AuthorityRole> iterator = securityDetail.getAuthorities().iterator();
		SecurityDetail.AuthorityRole role1 = iterator.next();
		SecurityDetail.AuthorityRole role2 = iterator.next();
		Assert.assertTrue( ( role1 == SecurityDetail.AuthorityRole.ROLE_USER && role2 == SecurityDetail.AuthorityRole.ROLE_ADMIN )
			|| ( role2 == SecurityDetail.AuthorityRole.ROLE_USER && role1 == SecurityDetail.AuthorityRole.ROLE_ADMIN ) );


		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);

	}


	@Test
	public void testRemoveAuthority(){

		long adminId = registerAdminTravellerAndEscalateToAdmin();

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(adminId));

		SecurityDetail securityDetail = securityService.findLogin(ADMIN_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 2 );

		Iterator<SecurityDetail.AuthorityRole> iterator = securityDetail.getAuthorities().iterator();
		SecurityDetail.AuthorityRole role1 = iterator.next();
		SecurityDetail.AuthorityRole role2 = iterator.next();
		Assert.assertTrue( ( role1 == SecurityDetail.AuthorityRole.ROLE_USER && role2 == SecurityDetail.AuthorityRole.ROLE_ADMIN )
			|| ( role2 == SecurityDetail.AuthorityRole.ROLE_USER && role1 == SecurityDetail.AuthorityRole.ROLE_ADMIN ) );


		securityService.removeAuthority(ADMIN_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		securityDetail = securityService.findLogin(ADMIN_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}



	@Test
	public void testEnableSecurityDetail(){


		registerDefaultTraveller();

		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);

		securityService.disableSecurityDetail(DEFAULT_USERNAME);

		SecurityDetail securityDetail = securityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		securityService.enableSecurityDetail(DEFAULT_USERNAME);

		securityDetail = securityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );


		Mockito.verifyZeroInteractions(emailService,passwordEncoder,travellerService);
	}


	@Test
	public void testDisableSecurityDetail(){

		registerDefaultTraveller();

		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);

		SecurityDetail securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );

		securityService.disableSecurityDetail(DEFAULT_USERNAME);

		securityDetail = securityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		Mockito.verifyZeroInteractions(emailService,passwordEncoder,travellerService);
	}




	@Test
	public void  testChangePassword(){

		registerDefaultTraveller();

		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(emailService);

		SecurityDetail securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals(securityDetail.getPassword(),
				passwordEncoder.encodePassword( TRAVELLER_PASSWORD, DEFAULT_USERNAME ) );

		securityService.changeSecurityDetailPassword(DEFAULT_USERNAME, "new password");

		securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals(securityDetail.getPassword(),
				passwordEncoder.encodePassword( "new password", DEFAULT_USERNAME ) );

		Mockito.verify(passwordEncoder, Mockito.times(4)).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(passwordEncoder);
		Mockito.verifyZeroInteractions(emailService,travellerService);

	}



	@Test
	public void testResetPasswordByUsername(){

		registerDefaultTraveller();		

		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(emailService);
		
		SecurityDetail securityDetail = securityRepository.findSecurityDetail(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getPassword(), ENCODED_PASSWORD );

		securityService.resetPassword(DEFAULT_USERNAME);

		securityDetail = securityRepository.findSecurityDetail(DEFAULT_USERNAME);
		Assert.assertEquals( securityDetail.getPassword(), ENCODED_PASSWORD );


		Mockito.verify(passwordEncoder, Mockito.times(2)).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verify(emailService,  Mockito.times(1)).sendPassword(Mockito.<Traveller>anyObject(),Mockito.anyString());
		Mockito.verifyNoMoreInteractions(passwordEncoder,emailService,travellerService);
	}




}
