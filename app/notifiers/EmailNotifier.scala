package notifiers

import com.typesafe.plugin._
import models._
import play.api.Play.current
import play.Logger
import play.api.{Mode, Play}
import play.core.Router


object EmailNotifier {

  private def noSmtpHostDefinedException = throw new NullPointerException("No SMTP host defined")
  private val alertRecipient = Play.configuration.getString("mail.alerts").getOrElse("snaps@example.org")
  private val emailFrom = Play.configuration.getString("mail.alerts").getOrElse("snaps@example.com")

  private def hostname = Play.configuration.getString("net.hostname").getOrElse("localhost")

  private def footer = {
    "\nSent by Snaps, event photo snaps.\nHost: " + hostname
  }

  private def sendOrMockAlert(notification: (String, String)) {
    if (Play.mode == Mode.Prod) {
      Play.configuration.getString("smtp.host") match {
        case None => noSmtpHostDefinedException
        case Some("mock") => mockNotification(notification._1)
        case _ => sendNotification(alertRecipient,notification._1, notification._2)
      }
    } else {
      mockNotification(notification._1)
    }
  }

  private def sendOrMockNotification(email:String,notification: (String, String)) {
    if (Play.mode == Mode.Prod) {
      Play.configuration.getString("smtp.host") match {
        case None => noSmtpHostDefinedException
        case Some("mock") => mockNotification(notification._1)
        case _ => sendNotification(email,notification._1, notification._2)
      }
    } else {
      mockNotification(notification._1)
    }
  }


  private def sendNotification(recipient:String,subject: String, bodyText: String) {
    val mail = use[MailerPlugin].email
    mail.setSubject("SNAPS: " + subject)
    mail.addFrom(emailFrom)
    mail.addRecipient(recipient)
    mail.send(bodyText+footer)
    Logger.info("Notification sent: " + subject)
  }


  private def mockNotification(subject: String) {
    Logger.info("Notification (mock): " + subject)
  }


  def registrationAlert(participant: Participant) {
    sendOrMockAlert(registrationText(participant))
  }

  def deleteParticipantAlert(participant: Participant) {
    sendOrMockAlert(deleteParticipantText(participant))
  }

  def createEventAlert(participant: Participant, eventName: String) {
    sendOrMockAlert(createEventText(participant, eventName))
  }


  def addAlbumNotification(participant: Participant, event: Event, album: Album) {
    val (subject:String,body:String) = addAlbumText(participant, event.eventName)
    sendEventNotification(event,subject,body)
  }

  private def sendEventNotification(event:Event,subject:String,body:String) {
    event.organiser.map { organiser =>
      sendOrMockNotification(organiser.email,(subject,body))
    }
    for(eventParticipant<-event.findParticipants){
      sendOrMockNotification(eventParticipant.email,(subject,body))
    }
  }

  def deleteEventAlert(participant: Participant, event: Event) {
    sendOrMockAlert(deleteEventText(participant, event.eventName))
  }

  def deleteEventNotification(participant: Participant, event: Event) {
    val (subject:String,body:String) = deleteEventText(participant, event.eventName)
    sendEventNotification(event,subject,body)
  }

  private def registrationText(participant: Participant) = {
    ("New registration", "Participant " + participant.username + " has registered with Snaps")
  }

  private def deleteParticipantText(participant: Participant) = {
    ("Participant deleted", "Participant " + participant.username + " has been deleted with Snaps")

  }

  private def createEventText(participant: Participant, eventName: String) = {
    ("Event created", "Event " + eventName + " created by  " + participant.username)
  }

  private def deleteEventText(participant: Participant, eventName: String) = {
    ("Event deleted", "Event " + eventName + " deleted by  " + participant.username)
  }

  private def addAlbumText(participant: Participant, eventName: String) = {
    ("Album added", "Album added to Event " + eventName + " by  " + participant.username)
  }


  def newPasswordText(participant: Participant, newPassword: String): (String, String) = {
    ("Password reset","Your new password is : " + newPassword)
  }

  def sendNewPassword(participant: Participant, newPassword: String) {
    sendOrMockNotification(participant.email,newPasswordText(participant, newPassword))
  }

  private def joinRequestText(event: Event, participant: Participant): (String, String) = {
    ("Event join request", Participant + "" + participant.username + " has asked to joinRequestText _event " + event.eventName)
  }

  def sendJoinRequestNotification(event: Event, participant: Participant) {
    val (subject:String,body:String) = joinRequestText(event,participant)
    event.organiser.map { organiser =>
      sendOrMockNotification(organiser.email,(subject,body))
    }
  }

  def emailVerificationText(username: String, verificationUrl: String): (String, String) = {
    ("Please verify your email address","Please verify your email address by going to this website: " + verificationUrl)
  }


  def sendEmailVerification(participant:Participant, verificationHash: String) {
    val verificationUrl = hostname + "/participant/" + participant.participantId + "/verify/" + verificationHash +"/"
    sendOrMockNotification(participant.email,emailVerificationText(participant.username, verificationUrl))
  }
}
