package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

//@Service
public class EmailService extends AbstractService implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;

	private String passwordTextTemplate;
	private String passwordTextSubject;
	private String registrationNotificationTextTemplate;
	private String registrationNotificationTextSubject;
	private String fromAddress;
	private boolean sendRegistrationNotification = true;
	private boolean sendEmails = true;


	private String snapsURL;

	@Override
	public void sendPassword(Traveller traveller, String password) {

		assert traveller != null;
		assert password != null;
		assert passwordTextSubject != null;
		assert passwordTextTemplate != null;

		if( sendEmails ){
			if( traveller.isValid()){
				if( ! isEmailAnExampleOne(traveller) ){
					if( password.trim().length() > 3){
						try {

							log.info("Sending reset password to "+traveller);

							MimeMessage message = mailSender.createMimeMessage();
							MimeMessageHelper messageHelper = new MimeMessageHelper(message,false,"UTF-8");
							messageHelper.setValidateAddresses(true);
							messageHelper.setSubject(passwordTextSubject);
							messageHelper.setFrom(fromAddress);
							messageHelper.setTo(traveller.getEmail());

							final String passwordText = passwordTextTemplate
										.replaceAll("\\[\\[snapsURL\\]\\]",snapsURL)
				//						.replaceAll("\\[\\[username\\]\\]",traveller.getSecurityDetail().getUsername())
										.replaceAll("\\[\\[fullname\\]\\]",traveller.getFullname())
										.replaceAll("\\[\\[email\\]\\]",traveller.getEmail())
										.replaceAll("\\[\\[password\\]\\]",password);

							messageHelper.setText(passwordText);

							mailSender.send(message); // Possible future refactor is to queue and async the send call

						} catch (MessagingException e) {
							throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.EMAIL,e);
						}
					} else {
						throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.INVALID_INPUT,"Password is invalid");
					}
				} else {
					log.info("Traveller has an example email so no email will be sent");
				}
			} else {
				throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.INVALID_INPUT,"Traveller is invalid");
			}
		} else {
			log.debug("Sending emails is disabled");
			throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.EMAIL_DISABLED);
		}
	}

	@Override
	public void notifyNewRegistration(Traveller traveller) {

		assert traveller != null;
		assert registrationNotificationTextSubject != null;
		assert registrationNotificationTextTemplate != null;

		if( sendRegistrationNotification && sendEmails ){
			if( traveller.isValid()){

				Set<Traveller> peopleToBeNotified = findPeopleToBeNotifiedOfRegistrations();

				if( peopleToBeNotified == null || peopleToBeNotified.isEmpty() ){
					throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_STATE,"No people can be notified!");
				}
				while( !peopleToBeNotified.isEmpty() ){ // oooh never ending looooooooop
					Set<Traveller> nextRecipients = findNextRecipients(peopleToBeNotified);
					for(Traveller recipient : nextRecipients ){
						peopleToBeNotified.remove(recipient);
					}
					sendRegistrationNotification(traveller,nextRecipients);
				}
			} else {
				throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.INVALID_INPUT,"Traveller is invalid");
			}
		} else {
			log.debug("Notification of registrations is disabled");
		}
	}

	private void sendRegistrationNotification(Traveller newRegistration,Set<Traveller> recipients) {
		log.debug("Sending registration notification");

		assert !recipients.isEmpty();

		try {

			log.debug("Sending registration notification");

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,false,"UTF-8");
			messageHelper.setValidateAddresses(true);
			messageHelper.setSubject(registrationNotificationTextSubject);
			messageHelper.setFrom(fromAddress);
			for(Traveller traveller : recipients){
				if( ! isEmailAnExampleOne(traveller)){
					messageHelper.addTo(traveller.getEmail()); // bcc?
				}else {
					log.info("Traveller has an example email so no email will be sent");
				}
			}			

			final String emailText = registrationNotificationTextTemplate
						.replaceAll("\\[\\[snapsURL\\]\\]",snapsURL)
						.replaceAll("\\[\\[username\\]\\]",newRegistration.getSecurityDetail().getUsername())
						.replaceAll("\\[\\[fullname\\]\\]",newRegistration.getFullname())
						.replaceAll("\\[\\[email\\]\\]",newRegistration.getEmail());

			messageHelper.setText(emailText);

			if( message.getAllRecipients().length>0){
				mailSender.send(message); // Possible future refactor is to queue and async the send call
			}
		} catch (MessagingException e) {
			throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.EMAIL,e);
		}

	}

	private Set<Traveller> findNextRecipients(Set<Traveller> peopleToBeNotified) {
		log.debug("Finding next recipients to be notified");
		Set<Traveller> nextRecipients = new HashSet<Traveller>();
		int batchSize = 5;
		for(Iterator<Traveller> iterator = peopleToBeNotified.iterator(); iterator.hasNext() && batchSize>0; batchSize--){
			log.debug("Batch:"+batchSize);
			nextRecipients.add(iterator.next());
		}
		return nextRecipients;
	}

	private Set<Traveller> findPeopleToBeNotifiedOfRegistrations() {
		log.debug("Looking for people to be notified");
		Set<Traveller> peopleToBeNotified = new HashSet<Traveller>();
		List<Traveller> allTravellers = travellerRepository.findAllTravellers();
		log.debug("travellers:"+allTravellers.size());
		for(Traveller traveller : allTravellers){
			log.debug("Checking:"+traveller);
			if( traveller.getSecurityDetail().hasAuthority( SecurityDetail.AuthorityRole.ROLE_ADMIN )
					|| traveller.getSecurityDetail().hasAuthority( SecurityDetail.AuthorityRole.ROLE_SUPER) ){
				peopleToBeNotified.add(traveller);
			} else

			log.debug("non admin");
		}
		return peopleToBeNotified;
	}

	private boolean isEmailAnExampleOne(Traveller traveller){
		if( traveller.getEmail().matches("@example\\....$")){
			return true;
		}
		return false;
	}


	public void setRegistrationNotificationTextTemplate(String registrationNotificationTextTemplate) {
		this.registrationNotificationTextTemplate = registrationNotificationTextTemplate;
	}

	public void setRegistrationNotificationTextSubject(String registrationNotificationTextSubject) {
		this.registrationNotificationTextSubject = registrationNotificationTextSubject;
	}

	public void setPasswordTextTemplate(String passwordTextTemplate) {
		this.passwordTextTemplate = passwordTextTemplate;
	}

	public void setPasswordTextSubject(String passwordTextSubject) {
		this.passwordTextSubject = passwordTextSubject;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public void setSnapsURL(String snapsURL) {
		this.snapsURL = snapsURL;
	}

	public void setSendRegistrationNotification(boolean sendRegistrationNotification) {
		this.sendRegistrationNotification = sendRegistrationNotification;
	}

	public void setSendEmails(boolean sendEmails) {
		this.sendEmails = sendEmails;
	}
}
