package notifiers

import com.typesafe.plugin._
import models._
import play.api.Play.current
import play.Logger
import play.api.{Mode, Play}


object EmailNotifier {

  private def NoSmtpHostDefinedException = throw new NullPointerException("No SMTP host defined")

  private def sendOrMock(sendFunction:Unit,mockFunction:Unit) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockFunction
        case _ => sendFunction
      }
    } else {
      mockFunction
    }
  }


  private def sendRegistrationNotification(participant: Participant) {
    Logger.info("Sending notification email for registration by " + participant.username)
    val mail = use[MailerPlugin].email
    mail.setSubject("New Snaps registration")
    mail.addRecipient("Ivar <ivar+snapsregistration@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Participant " + participant.username + " has registered with Snaps" )
  }

  private def mockRegistrationNotification(participant: Participant) {
    Logger.info("Notification (mock): Registration by " + participant.username)
  }

  def registrationNotification(participant: Participant) {
    Logger.info("smtp.username=" + Play.configuration.getString("smtp.username"))
    Logger.info("smtp.username=" + Play.current.configuration.getString("smtp.username"))
    Logger.info("smtp.username=" + Play.application.configuration.getString("smtp.username"))
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockRegistrationNotification(participant)
        case _ => sendRegistrationNotification(participant)
      }
    } else {
      mockRegistrationNotification(participant)
    }
  }


  private def sendDeleteParticipantNotification(participant: Participant) {
    Logger.info("Sending notification email for deletetion of " + participant.username)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Participant deleted")
    mail.addRecipient("Ivar <ivar+snapdeletion@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Participant " + participant.username + " has been deleted with Snaps" )

  }

  private def mockDeleteParticipantNotification(participant: Participant) {
    Logger.info("Notification (mock): Deleting " + participant.username)
  }


  def deleteParticipantNotification(participant: Participant) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockDeleteParticipantNotification(participant)
        case _ => sendDeleteParticipantNotification(participant)
      }
    } else {
      mockDeleteParticipantNotification(participant)
    }
  }


  private def sendAddParticipantToEventNotification(participant: Participant, eventName :String) {
    Logger.info("Sending notification email for addition of " + participant.username)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Participant added")
    mail.addRecipient("Ivar <ivar+snapevent@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Participant " + participant.username + " has been added to" + eventName )

  }

  private def mockAddParticipantToEventNotification(participant: Participant, eventName :String) {
    Logger.info("Notification (mock): Adding " + participant.username + " to " + eventName)
  }


  def addParticipantToEventNotification(participant: Participant, event: Event) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockAddParticipantToEventNotification(participant,event.eventName)
        case _ => sendAddParticipantToEventNotification(participant,event.eventName)
      }
    } else {
      mockAddParticipantToEventNotification(participant,event.eventName)
    }
  }


  private def sendRemoveParticipantFromEventNotification(participant: Participant, eventName :String) {
    Logger.info("Sending notification email for removal of " + participant.username)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Participant removed")
    mail.addRecipient("Ivar <ivar+snapevent@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Participant " + participant.username + " has been removed from" + eventName )

  }

  private def mockRemoveParticipantFromEventNotification(participant: Participant, eventName :String) {
    Logger.info("Notification (mock): Removing " + participant.username + " from " + eventName)
  }


  def removeParticipantFromEventNotification(participant: Participant, event: Event) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockRemoveParticipantFromEventNotification(participant,event.eventName)
        case _ => sendRemoveParticipantFromEventNotification(participant,event.eventName)
      }
    } else {
      mockRemoveParticipantFromEventNotification(participant,event.eventName)
    }
  }


  private def mockCreateEventNotification(participant: Participant,eventName: String) {
    Logger.info("Notification (mock): Create event " + eventName + " by " + participant.username)
  }


  private def sendCreateEventNotification(participant: Participant, eventName: String){
    Logger.info("Sending notification email for creation of event " + eventName)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Event created")
    mail.addRecipient("Ivar <ivar+snapcreate@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Event " + eventName + " created by  " + participant.username)
  }

  def createEventNotification(participant: Participant, eventName: String) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockCreateEventNotification(participant,eventName)
        case _ => sendCreateEventNotification(participant, eventName)
      }
    } else {
      mockCreateEventNotification(participant, eventName)
    }
  }

  private def mockDeleteEventNotification(participant: Participant,eventName: String) {
    Logger.info("Notification (mock): Delete event " + eventName + " by " + participant.username)
  }


  private def sendDeleteEventNotification(participant: Participant, eventName: String){
    Logger.info("Sending notification email for deletion of event " + eventName)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Event deleted")
    mail.addRecipient("Ivar <ivar+snapdelete@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Event " + eventName + " deleted by  " + participant.username)
  }

  def deleteEventNotification(participant: Participant, event: Event) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockDeleteEventNotification(participant,event.eventName)
        case _ => sendDeleteEventNotification(participant, event.eventName)
      }
    } else {
      mockCreateEventNotification(participant, event.eventName)
    }
  }



  private def mockAddAlbumNotification(participant: Participant,eventName: String) {
    Logger.info("Notification (mock): Album added to event " + eventName + " by " + participant.username)
  }


  private def sendAddAlbumNotification(participant: Participant, eventName: String){
    Logger.info("Sending notification email for album added to event " + eventName)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Album added")
    mail.addRecipient("Ivar <ivar+snapalbum@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Album added to Event " + eventName + " by  " + participant.username)
  }

  def addAlbumNotification(participant: Participant, event: Event, album: Album) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockAddAlbumNotification(participant,event.eventName)
        case _ => sendAddAlbumNotification(participant, event.eventName)
      }
    } else {
      mockAddAlbumNotification(participant, event.eventName)
    }
  }




  private def mockRemoveAlbumNotification(participant: Participant,eventName: String) {
    Logger.info("Notification (mock): Album removed from event " + eventName + " by " + participant.username)
  }


  private def sendRemoveAlbumNotification(participant: Participant, eventName: String){
    Logger.info("Sending notification email for album removed from event " + eventName)
    val mail = use[MailerPlugin].email
    mail.setSubject("Snaps: Album removed")
    mail.addRecipient("Ivar <ivar+snapalbum@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( "Album removed from Event " + eventName + " by  " + participant.username)
  }

  def removeAlbumNotification(participant: Participant, event: Event, album: Album) {
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockRemoveAlbumNotification(participant,event.eventName)
        case _ => sendRemoveAlbumNotification(participant, event.eventName)
      }
    } else {
      mockRemoveAlbumNotification(participant, event.eventName)
    }
  }



}
