package notifiers

import com.typesafe.plugin._
import models._
import play.api.Play.current
import play.Logger
import play.api.{Mode, Play}
import play.core.Router


object EmailNotifier {

  private def noSmtpHostDefinedException = throw new NullPointerException("No SMTP host defined")
  private val alertRecipient = Play.configuration.getString("mail.alerts").getOrElse("Snaps")
  private val emailFrom = Play.configuration.getString("mail.alerts").getOrElse("Snaps")

  private def footer = {
    val hostname = java.net.InetAddress.getLocalHost().getHostName()
    "Sent by Snaps, event photo snaps. Host: " + hostname
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

  private def sendOrMockNotification(notification: (Option[String],String, String)) {
    if (Play.mode == Mode.Prod && notification._1.isDefined) {
      Play.configuration.getString("smtp.host") match {
        case None => noSmtpHostDefinedException
        case Some("mock") => mockNotification(notification._2)
        case _ => sendNotification(notification._1.get, notification._2,notification._3)
      }
    } else {
      mockNotification(notification._2)
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


  def registrationNotification(participant: Participant) {
    sendOrMockAlert(sendRegistrationNotification(participant))
  }

  def deleteParticipantNotification(participant: Participant) {
    sendOrMockAlert(sendDeleteParticipantNotification(participant))
  }

  def createEventNotification(participant: Participant, eventName: String) {
    sendOrMockAlert(sendCreateEventNotification(participant, eventName))
  }

//  def addParticipantToEventNotification(participant: Participant, event: Event) {
//    sendOrMock(sendAddParticipantToEventNotification(participant, event.eventName))
//  }
//
//  def removeParticipantFromEventNotification(participant: Participant, event: Event) {
//    sendOrMock(sendRemoveParticipantFromEventNotification(participant, event.eventName))
//  }

  def deleteEventNotification(participant: Participant, event: Event) {
    sendOrMockAlert(sendDeleteEventNotification(participant, event.eventName))
  }

//  def addAlbumNotification(participant: Participant, event: Event, album: Album) {
//    sendOrMock(sendAddAlbumNotification(participant, event.eventName))
//  }
//
//  def removeAlbumNotification(participant: Participant, event: Event, album: Album) {
//    sendOrMock(sendRemoveAlbumNotification(participant, event.eventName))
//  }


  private def sendRegistrationNotification(participant: Participant) = {
    ("New registration", "Participant " + participant.username + " has registered with Snaps")
  }

  private def sendDeleteParticipantNotification(participant: Participant) = {
    ("Participant deleted", "Participant " + participant.username + " has been deleted with Snaps")

  }


//  private def sendAddParticipantToEventNotification(participant: Participant, eventName: String) = {
//    ("Participant added", "Participant " + participant.username + " has been added to" + eventName)
//  }
//
//
//  private def sendRemoveParticipantFromEventNotification(participant: Participant, eventName: String) = {
//    ("Participant removed", "Participant " + participant.username + " has been removed from" + eventName)
//  }


  private def sendCreateEventNotification(participant: Participant, eventName: String) = {
    ("Event created", "Event " + eventName + " created by  " + participant.username)
  }


  private def sendDeleteEventNotification(participant: Participant, eventName: String) = {
    ("Event deleted", "Event " + eventName + " deleted by  " + participant.username)
  }


//  private def sendAddAlbumNotification(participant: Participant, eventName: String) = {
//    ("Snaps: Album added", "Album added to Event " + eventName + " by  " + participant.username)
//  }
//
//
//  private def sendRemoveAlbumNotification(participant: Participant, eventName: String) = {
//    ("Snaps: Album removed", "Album removed from Event " + eventName + " by  " + participant.username)
//  }

  def sendNewPasswordNotification(participant: Participant, newPassword: String): (Option[String], String, String) = {
    (participant.email,"Password reset","Your new password is : " + newPassword)
  }

  def sendNewPassword(participant: Participant, newPassword: String) {
    sendOrMockNotification(sendNewPasswordNotification(participant, newPassword))
  }

}
