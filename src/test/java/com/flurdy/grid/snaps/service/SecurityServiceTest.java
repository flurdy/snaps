package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import java.util.Iterator;

@Transactional
public class SecurityServiceTest extends AbstractServiceTest {

	@InjectMocks
	private ISecurityService securityService = new SecurityService();

	@Mock
	private ISecurityRepository securityRepository;

	@Mock
	private IEmailService emailService;

	@Mock
	private ITravellerRepository travellerRepository;

	@Mock
	private ITravellerService travellerService;


	@Mock
	private PasswordEncoder passwordEncoder;

	private static final String TRAVELLER_USERNAME = "travelling testuser";
	private static final String TRAVELLER_PASSWORD = "travelling testuser";
	private static final String TRAVELLER_FULLNAME = "travelling testuser";
	private static final String TRAVELLER_EMAIL = "travelling.testuser@example.com";


	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.anyObject())).thenReturn("ENCRYPTED");
	}

	private Traveller generateMockTraveller()  {
		return new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL)
					.enabled(true)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); }})
					.build();
	}

	private SecurityDetail generateMockEmptySecurityDetail()  {
		return new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.enabled(true)
					.authorities(new HashSet<String>())
					.build();
	}

	private SecurityDetail generateMockSecurityDetail()  {
		return new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.enabled(true)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); }})
					.build();
	}

	private SecurityDetail generateMockDisabledSecurityDetail()  {
		return new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.enabled(false)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); }})
					.build();
	}

	private SecurityDetail generateMockAdminSecurityDetail() {
		return new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.enabled(true)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); add("ROLE_ADMIN"); }})
					.build();
	}

	private SecurityDetail generateMockSuperSecurityDetail() {
		return new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.enabled(true)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); add("ROLE_SUPER"); }})
					.build();
	}

	protected Traveller generateAdminTraveller(){
		return new Traveller.Builder()
			.username(ADMIN_USERNAME)
			.fullname(ADMIN_FULLNAME)
			.password(ADMIN_PASSWORD)
			.authorities(new HashSet<String>(){{ add("ROLE_USER"); add("ROLE_ADMIN"); }})
			.email(ADMIN_EMAIL).build();
	}

	protected Traveller generateSuperTraveller(){
		return new Traveller.Builder()
			.username(ADMIN_USERNAME)
			.fullname(ADMIN_FULLNAME)
			.password(ADMIN_PASSWORD)
			.authorities(new HashSet<String>(){{ add("ROLE_USER"); add("ROLE_SUPER"); }})
			.email(ADMIN_EMAIL).build();
	}

	@Test
	public void testFindLogin(){

		SecurityDetail mockSecurityDetail = new SecurityDetail.Builder()
				.username(DEFAULT_USERNAME).password(DEFAULT_PASSWORD).enabled(true).build();

		Mockito.when(securityRepository.findSecurityDetail(DEFAULT_USERNAME)).thenReturn(mockSecurityDetail);

		SecurityDetail securityDetail = securityService.findLogin(DEFAULT_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getUsername(), DEFAULT_USERNAME );
		Mockito.verify(securityRepository).findSecurityDetail(DEFAULT_USERNAME);
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository);

	}

	@Test
	public void testFindNonExistentLogin(){
		Mockito.when(securityRepository.findSecurityDetail("asdasdas")).thenReturn(null);

		SecurityDetail securityDetail = securityService.findLogin("asdasdas");

		Assert.assertNull( securityDetail );
		Mockito.verify(securityRepository).findSecurityDetail("asdasdas");
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository);
	}

	@Test
	public void testFindNullLogin(){
		SecurityDetail securityDetail = securityService.findLogin(null);
		Assert.assertNull( securityDetail );
		Mockito.verifyZeroInteractions(securityRepository,travellerRepository);
	}



	@Test
	public void testRegisterTraveller(){

		final SecurityDetail mockSecurityDetail = generateMockSecurityDetail();

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(null).thenReturn(mockSecurityDetail);
//		Mockito.doNothing().when(travellerRepository).addTraveller(Mockito.<Traveller>anyObject());
		Traveller traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		securityService.registerTraveller(traveller);

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getUsername(), TRAVELLER_FULLNAME );

		Mockito.verify(travellerRepository).addTraveller(Mockito.<Traveller>anyObject());
		Mockito.verify(securityRepository, Mockito.times(2)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(travellerRepository,securityRepository,emailService,passwordEncoder);
	}


	@Test(expected = SnapLogicalException.class)
	public void testRegisterTravellerTwice(){

		final SecurityDetail mockSecurityDetail = generateMockSecurityDetail();

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(null).thenReturn(mockSecurityDetail);

		Traveller traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		securityService.registerTraveller(traveller);

		traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		securityService.registerTraveller(traveller);
	}





	@Test(expected = SnapLogicalException.class)
	public void testRegisterExistingTravellerUsername(){

		final SecurityDetail mockSecurityDetail = generateMockSecurityDetail();
//		Mockito.reset(securityRepository);
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(mockSecurityDetail);

		Traveller traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		securityService.registerTraveller(traveller);

	}




	@Test
	public void testAddAuthority(){

		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateAdminTraveller());
		
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockAdminSecurityDetail());
//		Mockito.doNothing().when(securityRepository).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 2 );

		Iterator<SecurityDetail.AuthorityRole> iterator = securityDetail.getAuthorities().iterator();
		SecurityDetail.AuthorityRole role1 = iterator.next();
		SecurityDetail.AuthorityRole role2 = iterator.next();
		Assert.assertTrue( ( role1 == SecurityDetail.AuthorityRole.ROLE_USER && role2 == SecurityDetail.AuthorityRole.ROLE_ADMIN )
			|| ( role2 == SecurityDetail.AuthorityRole.ROLE_USER && role1 == SecurityDetail.AuthorityRole.ROLE_ADMIN ) );



//		Mockito.verify(travellerRepository).addTraveller(Mockito.<Traveller>anyObject());
		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
//		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
//		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.anyObject())
		 Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();;
		Mockito.verifyNoMoreInteractions(securityRepository,travellerService);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);

	}


	@Test // (expected = SnapLogicalException.class) // Idempotent
	public void testAddAuthorityTwice(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateAdminTraveller())
				.thenReturn(generateAdminTraveller());
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockAdminSecurityDetail());

		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );


		Mockito.verify(securityRepository, Mockito.times(2)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(2)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerService, Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(securityRepository,travellerService);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}

	@Test(expected = AssertionError.class)
	public void testAddNullAuthority(){
		securityService.addAuthority(TRAVELLER_USERNAME, null );
	}


	@Test // (expected = SnapLogicalException.class) //  Idempotent
	public void testAddExistingAuthority(){
		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateAdminTraveller());

		final SecurityDetail mockSecurityDetail = generateMockSecurityDetail();
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(mockSecurityDetail);
		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );

		Mockito.verify(securityRepository, Mockito.times(1)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(securityRepository,travellerService);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}


	@Test(expected = AssertionError.class)
	public void testAddAuthorityToNullTraveller(){
		securityService.addAuthority(null, SecurityDetail.AuthorityRole.ROLE_ADMIN );
	}




	@Test(expected = SnapNotFoundException.class)
	public void testAddAuthorityToNonExistantTraveller(){
		Mockito.when(travellerService.findCurrentTraveller())				
				.thenReturn(generateAdminTraveller());
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(null);
		securityService.addAuthority("abrah", SecurityDetail.AuthorityRole.ROLE_ADMIN );
	}



	@Test(expected = SnapLogicalException.class)
	public void testAddAuthorityWithoutAuthorization(){

		final SecurityDetail mockAdminSecurityDetail = generateMockAdminSecurityDetail();
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(mockAdminSecurityDetail);
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

	}



	@Test
	public void testAddSuperAuthorityAsSuper(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateSuperTraveller());
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSuperSecurityDetail());

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertTrue( !securityDetail.hasAuthority( SecurityDetail.AuthorityRole.ROLE_SUPER ) );

		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_SUPER );

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 2 );
		Assert.assertTrue( securityDetail.hasAuthority( SecurityDetail.AuthorityRole.ROLE_SUPER ) );

		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService,securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}


	@Test(expected = SnapLogicalException.class)
	public void testAddSuperAuthorityAsNonSuper(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateAdminTraveller());
		securityService.addAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_SUPER );

	}



	@Test
	public void testRemoveAuthority(){
		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateAdminTraveller());

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockEmptySecurityDetail());

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		securityService.removeAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.getAuthorities() == null || securityDetail.getAuthorities().isEmpty() );

		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService,securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}




	@Test // (expected = SnapLogicalException.class) //  idempotent
	public void testRemoveAuthorityTwice(){
		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateAdminTraveller())
				.thenReturn(generateAdminTraveller());
		final SecurityDetail mockSecurityDetail = generateMockSecurityDetail();
		final SecurityDetail mockEmptySecurityDetail = generateMockEmptySecurityDetail();
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(mockSecurityDetail)
				.thenReturn(mockEmptySecurityDetail);

		securityService.removeAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );
		securityService.removeAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );

		Mockito.verify(securityRepository, Mockito.times(2)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(2)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerService, Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService,securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}

	@Test(expected = AssertionError.class)
	public void testRemoveNullAuthority(){
		securityService.removeAuthority(TRAVELLER_USERNAME, null );
	}

	@Test // (expected = SnapLogicalException.class) // idempotent
	public void testRemoveNonExistentAuthority(){
		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateAdminTraveller());
		final SecurityDetail mockSecurityDetail = generateMockSecurityDetail();
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(mockSecurityDetail);

		securityService.removeAuthority(TRAVELLER_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		Mockito.verify(securityRepository, Mockito.times(1)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService,securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}


	@Test(expected = AssertionError.class)
	public void testRemoveAuthorityFromNullTraveller(){
		securityService.removeAuthority(null, SecurityDetail.AuthorityRole.ROLE_USER );
	}


	@Test(expected = SnapLogicalException.class)
	public void testRemoveAuthorityFromNonExistentTraveller(){
		
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(null);

		securityService.removeAuthority("abrah", SecurityDetail.AuthorityRole.ROLE_USER );
	}




	@Test
	public void testEnableSecurityDetail(){

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockDisabledSecurityDetail())
				.thenReturn(generateMockDisabledSecurityDetail())
				.thenReturn(generateMockSecurityDetail());

		securityService.disableSecurityDetail(TRAVELLER_USERNAME);

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		securityService.enableSecurityDetail(TRAVELLER_USERNAME);

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );

		Mockito.verify(securityRepository, Mockito.times(4)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(2)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}



	@Test // Idempotent
	public void testEnablingAlreadyEnabledSecurityDetail(){

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail());

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );

		securityService.enableSecurityDetail(TRAVELLER_USERNAME);

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );


		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}


	@Test(expected = SnapLogicalException.class)
	public void testEnableNonExistentSecurityDetail(){

		Mockito.when(securityRepository.findSecurityDetail("asdasdsa"))
				.thenReturn(null)
				.thenReturn(null)
				.thenReturn(null);


		SecurityDetail securityDetail = securityService.findLogin("asdasdsa");
		Assert.assertNull( securityDetail );

		securityService.enableSecurityDetail("asdasdsa");

		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail("asdasdsa");
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}


	@Test(expected = AssertionError.class)
	public void testEnableNullSecurityDetail(){

		securityService.enableSecurityDetail(null);

	}


	@Test
	public void testDisableSecurityDetail(){

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockDisabledSecurityDetail());

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );

		securityService.disableSecurityDetail(TRAVELLER_USERNAME);

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}



	@Test
	public void testDisablingAlreadyDisabledSecurityDetail(){

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockDisabledSecurityDetail())
				.thenReturn(generateMockDisabledSecurityDetail())
				.thenReturn(generateMockDisabledSecurityDetail());

		securityService.disableSecurityDetail(TRAVELLER_USERNAME);

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		securityService.disableSecurityDetail(TRAVELLER_USERNAME);

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		Mockito.verify(securityRepository, Mockito.times(4)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(2)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verifyNoMoreInteractions(securityRepository);
		Mockito.verifyZeroInteractions(travellerRepository,emailService,passwordEncoder);
	}



	@Test(expected = SnapLogicalException.class)
	public void testDisableNonExistentSecurityDetail(){

		Mockito.when(securityRepository.findSecurityDetail("asdasdsa"))
				.thenReturn(null)
				.thenReturn(null)
				.thenReturn(null);

		SecurityDetail securityDetail = securityService.findLogin("asdasdsa");
		Assert.assertNull( securityDetail );

		securityService.disableSecurityDetail("asdasdsa");

	}


	@Test(expected = AssertionError.class)
	public void testDisableNullSecurityDetail(){

		securityService.disableSecurityDetail(null);

	}




	@Test
	public void  testChangePassword(){

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail());
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.anyObject()))
				.thenReturn(TRAVELLER_PASSWORD)
				.thenReturn(TRAVELLER_PASSWORD)
				.thenReturn(TRAVELLER_PASSWORD);

		SecurityDetail securityDetail = securityService.findLogin(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals(securityDetail.getPassword(),
				passwordEncoder.encodePassword( TRAVELLER_PASSWORD, TRAVELLER_USERNAME ) );

		securityService.changeSecurityDetailPassword(TRAVELLER_USERNAME, "new password");

		securityDetail = securityService.findLogin(TRAVELLER_USERNAME);
		
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals(securityDetail.getPassword(),
				passwordEncoder.encodePassword( "new password", TRAVELLER_USERNAME ) );

		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(passwordEncoder, Mockito.times(3)).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verifyNoMoreInteractions(securityRepository,passwordEncoder);
		Mockito.verifyZeroInteractions(travellerRepository,emailService);

	}


	@Test(expected = AssertionError.class)
	public void  testChangeNullPassword(){

		securityService.changeSecurityDetailPassword(TRAVELLER_USERNAME, null);

	}



	@Test(expected = SnapInvalidClientInputException.class)
	public void  testChangeInvalidPassword(){

		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail());

		securityService.changeSecurityDetailPassword(TRAVELLER_USERNAME, "");

	}



	@Test
	public void testResetPasswordByEmail(){

		Mockito.when(securityRepository.findSecurityDetailByEmail(TRAVELLER_EMAIL))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password("RANDOM")
					.enabled(true)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); }})
					.build());
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_EMAIL))
				.thenReturn(null);
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.anyObject()))
				.thenReturn(TRAVELLER_PASSWORD)
				.thenReturn("RANDOM");
		Mockito.when(travellerRepository.findTraveller(TRAVELLER_USERNAME)).thenReturn(generateMockTraveller());

		final String originalPassword = passwordEncoder.encodePassword(TRAVELLER_PASSWORD,TRAVELLER_USERNAME);

		SecurityDetail securityDetail = securityRepository.findSecurityDetailByEmail(TRAVELLER_EMAIL);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getPassword(), originalPassword );

		securityService.resetPassword(TRAVELLER_EMAIL);

		securityDetail = securityRepository.findSecurityDetailByEmail(TRAVELLER_EMAIL);

		Assert.assertEquals( securityDetail.getPassword(), "RANDOM" );

		Mockito.verify(securityRepository, Mockito.times(1)).findSecurityDetail(TRAVELLER_EMAIL);
//		Mockito.verify(securityRepository, Mockito.times(1)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetailByEmail(TRAVELLER_EMAIL);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerRepository, Mockito.times(1)).findTraveller(TRAVELLER_USERNAME);
		Mockito.verify(passwordEncoder, Mockito.times(2)).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verify(emailService,  Mockito.times(1)).sendPassword(Mockito.<Traveller>anyObject(),Mockito.anyString());
		Mockito.verifyNoMoreInteractions(travellerRepository,securityRepository,passwordEncoder,emailService);

	}




	@Test
	public void testResetPasswordByUsername(){

//		Mockito.when(securityRepository.findSecurityDetailByEmail(TRAVELLER_USERNAME))
//				.thenReturn(null);
		Mockito.when(securityRepository.findSecurityDetail(TRAVELLER_USERNAME))
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(generateMockSecurityDetail())
				.thenReturn(new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password("RANDOM")
					.enabled(true)
					.authorities(new HashSet<String>(){{ add("ROLE_USER"); }})
					.build());
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.anyObject()))
				.thenReturn(TRAVELLER_PASSWORD)
				.thenReturn("RANDOM");
		Mockito.when(travellerRepository.findTraveller(TRAVELLER_USERNAME)).thenReturn(generateMockTraveller());

		final String originalPassword = passwordEncoder.encodePassword(TRAVELLER_PASSWORD, TRAVELLER_USERNAME);
		SecurityDetail securityDetail = securityRepository.findSecurityDetail(TRAVELLER_USERNAME);

		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getPassword(), originalPassword );

		securityService.resetPassword(TRAVELLER_USERNAME);

		securityDetail = securityRepository.findSecurityDetail(TRAVELLER_USERNAME);
		Assert.assertEquals( securityDetail.getPassword(), "RANDOM" );


//		Mockito.verify(securityRepository, Mockito.times(1)).findSecurityDetailByEmail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(3)).findSecurityDetail(TRAVELLER_USERNAME);
		Mockito.verify(securityRepository, Mockito.times(1)).updateSecurityDetail(Mockito.<SecurityDetail>anyObject());
		Mockito.verify(travellerRepository, Mockito.times(1)).findTraveller(TRAVELLER_USERNAME);
		Mockito.verify(passwordEncoder, Mockito.times(2)).encodePassword(Mockito.anyString(),Mockito.anyObject());
		Mockito.verify(emailService,  Mockito.times(1)).sendPassword(Mockito.<Traveller>anyObject(),Mockito.anyString());
		Mockito.verifyNoMoreInteractions(travellerRepository,securityRepository,passwordEncoder,emailService);
	}



	@Test(expected = SnapNotFoundException.class)
	public void testResetPasswordByNonExistentEmail(){

		Mockito.when(securityRepository.findSecurityDetailByEmail("avava@asass.com"))
				.thenReturn(null);
		Mockito.when(securityRepository.findSecurityDetail("avava@asass.com"))
				.thenReturn(null);

		securityService.resetPassword("avava@asass.com");
	}




	@Test(expected = SnapNotFoundException.class)
	public void testResetPasswordByNonexistentUsername(){
		Mockito.when(securityRepository.findSecurityDetailByEmail("avava@asass.com"))
				.thenReturn(null);
		Mockito.when(securityRepository.findSecurityDetail("avava@asass.com"))
				.thenReturn(null);
		securityService.resetPassword("avava");
	}


	@Test(expected = SnapLogicalException.class)
	public void testResetPasswordWhereUsernameAndEmailMatch(){

		Mockito.when(securityRepository.findSecurityDetailByEmail("avava@asass.com"))
				.thenReturn(new SecurityDetail.Builder()
					.username(TRAVELLER_USERNAME)
					.password(TRAVELLER_PASSWORD)
					.enabled(true)
					.authorities(new HashSet<String>())
					.build());
		Mockito.when(securityRepository.findSecurityDetail("avava@asass.com"))
				.thenReturn(new SecurityDetail.Builder()
					.username("testusermatch@example.org")
					.password(TRAVELLER_PASSWORD)
					.enabled(true)
					.authorities(new HashSet<String>())
					.build());

		securityService.resetPassword("testusermatch@example.org");
	}




//	public void enforceAristocracy();
	

}
