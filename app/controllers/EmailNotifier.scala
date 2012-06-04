package notifiers

import com.typesafe.plugin._
import models._
import play.api.Play.current
import play.Logger
import play.api.{Mode, Play}


object EmailNotifier {

  private def NoSmtpHostDefinedException = throw new NullPointerException("No SMTP host defined")

  private def sendOrMock(notification:(String,String)) {
    Logger.info("smtp.username=" + Play.configuration.getString("smtp.username"))
    Logger.info("smtp.username=" + Play.current.configuration.getString("smtp.username"))
    Logger.info("smtp.username=" + Play.application.configuration.getString("smtp.username"))
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => NoSmtpHostDefinedException
        case Some("mock") => mockNotification( notification._1 )
        case _ => sendNotification( notification._1, notification._2 )
      }
    } else {
      mockNotification( notification._1 )
    }
  }


  private def sendNotification( subject:String, bodyText:String ) {
    val mail = use[MailerPlugin].email
    mail.setSubject("SNAPS: "+ subject)
    mail.addRecipient("Ivar <ivar+snaps@flurdy.com>")
    mail.addFrom("Snaps mail monkey <snaps+monkey@sites.flurdy.com>")
    mail.send( bodyText )
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

  def createEventNotification(participant: Participant, eventName :String) {
    sendOrMock(sendCreateEventNotification(participant, eventName))
  }

  def addParticipantToEventNotification(participant: Participant, event : Event) {
    sendOrMock(sendAddParticipantToEventNotification(participant,event.eventName))
  }

  def removeParticipantFromEventNotification(participant: Participant, event : Event) {
    sendOrMock(sendRemoveParticipantFromEventNotification(participant,event.eventName))
  }

  def deleteEventNotification(participant: Participant, event : Event) {
    sendOrMock(sendDeleteEventNotification(participant,event.eventName))
  }

  def addAlbumNotification(participant: Participant, event : Event, album: Album) {
    sendOrMock(sendAddAlbumNotification(participant,event.eventName))
  }

  def removeAlbumNotification(participant: Participant, event : Event, album: Album) {
    sendOrMock(sendRemoveAlbumNotification(participant,event.eventName))
  }


  private def sendRegistrationNotification(participant: Participant) = {
    ("New Snaps registration", "Participant " + participant.username + " has registered with Snaps")
  }

  private def sendDeleteParticipantNotification(participant: Participant) = {
    ("Snaps: Participant deleted","Participant " + participant.username + " has been deleted with Snaps" )

  }


  private def sendAddParticipantToEventNotification(participant: Participant, eventName :String) = {
    ("Snaps: Participant added", "Participant " + participant.username + " has been added to" + eventName )
  }


  private def sendRemoveParticipantFromEventNotification(participant: Participant, eventName :String) = {
    ("Snaps: Participant removed","Participant " + participant.username + " has been removed from" + eventName )

  }


  private def sendCreateEventNotification(participant: Participant, eventName: String) = {
    ("Snaps: Event created","Event " + eventName + " created by  " + participant.username)
  }


  private def sendDeleteEventNotification(participant: Participant, eventName: String) = {
    ("Snaps: Event deleted", "Event " + eventName + " deleted by  " + participant.username)
  }


  private def sendAddAlbumNotification(participant: Participant, eventName: String) = {
    ("Snaps: Album added","Album added to Event " + eventName + " by  " + participant.username)
  }


  private def sendRemoveAlbumNotification(participant: Participant, eventName: String) = {
    ("Snaps: Album removed","Album removed from Event " + eventName + " by  " + participant.username)
  }


}
