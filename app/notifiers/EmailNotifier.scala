package notifiers

import com.typesafe.plugin._
import models._
import play.api.Play.current
import play.Logger
import play.api.{Mode, Play}
import play.core.Router


object EmailNotifier {

  private def noSmtpHostDefinedException = throw new NullPointerException("No SMTP host defined")

  private def footer = {
    val hostname = java.net.InetAddress.getLocalHost().getHostName()
    "Sent by Snaps, event photo snaps. Host: " + hostname
  }

  private def sendOrMock(notification: (String, String)) {
    if (Play.mode == Mode.Prod) {
      Play.configuration.getString("smtp.host") match {
        case None => noSmtpHostDefinedException
        case Some("mock") => mockNotification(notification._1)
        case _ => sendNotification(notification._1, notification._2)
      }
    } else {
      mockNotification(notification._1)
    }
  }


  private def sendNotification(subject: String, bodyText: String) {
    val mail = use[MailerPlugin].email
    mail.setSubject("SNAPS: " + subject)
    mail.addFrom(Play.configuration.getString("mail.from").getOrElse("Snaps"))
    mail.addRecipient(Play.configuration.getString("mail.alerts").getOrElse("Snaps"))
    mail.send(bodyText+footer)
    Logger.info("Notification sent: " + subject)
  }

  private def mockNotification(subject: String) {
    Logger.info("Notification (mock): " + subject)
  }


  def registrationNotification(participant: Participant) {
    sendOrMock(sendRegistrationNotification(participant))
  }

  def deleteParticipantNotification(participant: Participant) {
    sendOrMock(sendDeleteParticipantNotification(participant))
  }

  def createEventNotification(participant: Participant, eventName: String) {
    sendOrMock(sendCreateEventNotification(participant, eventName))
  }

//  def addParticipantToEventNotification(participant: Participant, event: Event) {
//    sendOrMock(sendAddParticipantToEventNotification(participant, event.eventName))
//  }
//
//  def removeParticipantFromEventNotification(participant: Participant, event: Event) {
//    sendOrMock(sendRemoveParticipantFromEventNotification(participant, event.eventName))
//  }

  def deleteEventNotification(participant: Participant, event: Event) {
    sendOrMock(sendDeleteEventNotification(participant, event.eventName))
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


}
