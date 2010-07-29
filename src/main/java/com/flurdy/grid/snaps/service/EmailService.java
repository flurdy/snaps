package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

//@Service
public class EmailService extends AbstractService implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;

	private String passwordTextTemplate;
	private String passwordTextSubject;
	private String fromAddress;

	private String snapsURL;

	@Override
	public void sendPassword(Traveller traveller, String password) {

		assert traveller != null;
		assert password != null;

		if( traveller.isValid()){
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
			throw new SnapTechnicalException(SnapTechnicalException.SnapTechnicalError.INVALID_INPUT,"Traveller is invalid");
		}

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
}
