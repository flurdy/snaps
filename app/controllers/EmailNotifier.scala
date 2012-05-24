package notifiers

import com.typesafe.plugin._
import models.Participant
import play.api.Play.current
import play.Logger
import play.api.{Mode, Play}


object EmailNotifier {


  def sendRegistrationNotification(participant: Participant) {
    Logger.info("Sending notification email for registration by " + participant.username)
    val mail = use[MailerPlugin].email
    mail.setSubject("New Snaps registration")
    mail.addRecipient("Ivar <ivar+snapsexample@flurdy.com>")
    mail.addFrom("Snaps mail monkey <ivar+snapsexample@flurdy.com>")
    mail.send( "Participant " + participant.username + " has registered with Snaps" )
    Logger.debug("Notification email sent" )
  }

  def mockRegistrationNotification(participant: Participant) {
    Logger.info("Notification (mock): Registration by " + participant.username)
  }

  def registrationNotification(participant: Participant) {
    Logger.info("smtp.host=" + Play.current.configuration.getString("smtp.host"))
    Logger.info("smtp.host=" + Play.application.configuration.getString("smtp.host"))
    Logger.info("smtp.host=" + Play.configuration.getString("smtp.host"))
    Logger.info("smtp.username=" + Play.configuration.getString("smtp.username"))
    if(Play.mode == Mode.Prod){
      Play.configuration.getString("smtp.host") match {
        case None => throw new NullPointerException("No SMTP host defined")
        case Some("mock") => mockRegistrationNotification(participant)
        case _ => sendRegistrationNotification(participant)
      }
    } else {
      Logger.info("Development mode")
      mockRegistrationNotification(participant)
    }
  }


}
