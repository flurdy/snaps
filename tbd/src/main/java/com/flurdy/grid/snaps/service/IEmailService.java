package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;


public interface IEmailService {


	public void sendPassword(Traveller traveller, String password);

	public void notifyNewRegistration(Traveller traveller);

	public void setSendRegistrationNotification(boolean sendRegistrationNotification);

	public void setSendEmails(boolean sendEmails);

	public void setRegistrationNotificationTextTemplate(String registrationNotificationTextTemplate);

	public void setRegistrationNotificationTextSubject(String registrationNotificationTextSubject);

	public void setPasswordTextTemplate(String passwordTextTemplate);

	public void setPasswordTextSubject(String passwordTextSubject);

	public void setFromAddress(String fromAddress);

	public void setSnapsURL(String snapsURL);


}
