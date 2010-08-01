package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.IPhotoAlbumRepository;
import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:test-email.xml"})
public class EmailServiceTest extends AbstractServiceTest {

	private final transient Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String ADMIN_USERNAME = "testuser";
	private static final String ADMIN_PASSWORD = "testuser";
	private static final String ADMIN_FULLNAME = "testuser";
	private static final String ADMIN_EMAIL = "ivar+admin.snaps@localhost.localdomain";
	private static final String TEST_USERNAME = "testuser";
	private static final String TEST_PASSWORD = "testuser";
	private static final String TEST_FULLNAME = "testuser";
	private static final String TEST_EMAIL = "ivar+snaps@localhost.localdomain";

	@Mock
	private JavaMailSenderImpl mailSender;

	@Mock
	private ISecurityRepository securityRepository;

	@Mock
	private ITravellerRepository travellerRepository;

	@InjectMocks
//	@Autowired
	private IEmailService emailService = new EmailService();

//	private boolean sendEmail = false;

	private final String passwordTextTemplate =
			"SnapsURL [[snapsURL]] Fullname [[fullname]] Email [[email]] Password [[password]]";
	private final String passwordTextSubject = "Snaps login assistance";
	private final String registrationNotificationTextTemplate =
			"SnapsURL [[snapsURL]] Fullname [[fullname]] Email [[email]] Username [[usernam]]";
	private final String registrationNotificationTextSubject = "Snaps registration notification";
	private final String fromAddress = "noreply@localhost";
	private final boolean sendRegistrationNotification = true;
	private final boolean sendEmails = true;
	private final String snapsURL = "http://localhost:8886/snaps";


	private Traveller generateAdminTraveller(){
		Traveller admin = new Traveller.Builder()
			.username(ADMIN_USERNAME)
			.fullname(ADMIN_FULLNAME)
			.password(ADMIN_PASSWORD)
			.email(ADMIN_EMAIL)
			.authorities(new HashSet<String>(){{ add("ROLE_USER"); add("ROLE_ADMIN"); }})
			.build();
		return admin;
	}

	private Traveller generateTraveller(){
		Traveller admin = new Traveller.Builder()
			.username(TEST_USERNAME)
			.fullname(TEST_FULLNAME)
			.password(TEST_PASSWORD)
			.email(TEST_EMAIL)
			.authorities(new HashSet<String>(){{ add("ROLE_USER");  }})
			.build();
		return admin;
	}

	private List<Traveller> generateAllTravellers(){
		List<Traveller> travellers = new ArrayList<Traveller>();

		travellers.add(generateAdminTraveller());

		return travellers;
	}

	private List<Traveller> generateNoAdminTravellers(){
		List<Traveller> travellers = new ArrayList<Traveller>();

		travellers.add(generateTraveller());

		return travellers;
	}

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		emailService.setPasswordTextTemplate(passwordTextTemplate);
		emailService.setPasswordTextSubject(passwordTextSubject);
		emailService.setRegistrationNotificationTextTemplate(registrationNotificationTextTemplate);
		emailService.setRegistrationNotificationTextSubject(registrationNotificationTextSubject);
		emailService.setFromAddress(fromAddress);
		emailService.setSendRegistrationNotification(sendRegistrationNotification);
		emailService.setSendEmails(sendEmails);
		emailService.setSnapsURL(snapsURL);
		
//		Mockito.when(travellerRepository.findAllTravellers()).thenReturn(generateAllTravellers());

	}

	@Test
	public void testEmailPassword(){
		Mockito.when(mailSender.createMimeMessage()).thenCallRealMethod();

//		if( sendEmail ){
//			Mockito.doCallRealMethod().doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());
//		}

		emailService.sendPassword(generateTraveller(),"testEmailPassword");

		Mockito.verify(mailSender, Mockito.times(1)).createMimeMessage();
		Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.<MimeMessage>anyObject());
//		Mockito.verifyNoMoreInteractions(mailSender);
		Mockito.verifyZeroInteractions(securityRepository,travellerRepository);

	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullTraveller(){
		emailService.sendPassword(null,"testEmailPasswordNullTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordMissingEmail(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.build();
		emailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidTraveller2(){
		Traveller traveller = new Traveller.Builder()
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidPassword(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,"");
	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullPassword(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,null);
	}


	@Test
	public void testNotifyNewRegistration(){
//		if( sendEmail ){
//			Mockito.doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());
//			Mockito.doCallRealMethod().doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());
//		}

		Mockito.when(travellerRepository.findAllTravellers()).thenReturn(generateAllTravellers());
		Mockito.when(mailSender.createMimeMessage()).thenCallRealMethod();

		emailService.notifyNewRegistration(generateTraveller());

		Mockito.verify(travellerRepository, Mockito.times(1)).findAllTravellers();
		Mockito.verify(mailSender, Mockito.times(1)).createMimeMessage();
		Mockito.verify(mailSender, Mockito.times(1)).send(Mockito.<MimeMessage>anyObject());
		Mockito.verifyNoMoreInteractions(travellerRepository);
		Mockito.verifyZeroInteractions(securityRepository);
	}


	@Test(expected = SnapLogicalException.class)
	public void testNotifyNewRegistrationNoAdmins(){

		Mockito.when(travellerRepository.findAllTravellers())
				.thenReturn(new ArrayList<Traveller>());

		emailService.notifyNewRegistration(generateTraveller());

	}


	@Test(expected = AssertionError.class)
	public void testNotifyNewRegistrationNullTraveller(){
		emailService.notifyNewRegistration(null);
	}


//	public void setSendEmail(boolean sendEmail) {
//		this.sendEmail = sendEmail;
//	}
}
