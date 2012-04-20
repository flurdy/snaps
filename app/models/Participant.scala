package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger
import util.Random
import java.security.MessageDigest
import org.mindrot.jbcrypt.BCrypt

case class Participant(
                  participantId: Long = 0,
                  username: String,
                  fullName: String,
                  email: String,
                  password: Option[String] ){
  private lazy val salt = scala.util.Random.nextInt
  val encryptedPassword = Participant.encrypt(password,salt)

  def createEvent(eventName: String) = {
    Event.createEvent(new Event(eventName,fullName))
  }

}



object Participant {

  val simple = {
    get[Long]("participantid") ~
      get[String]("username") ~
      get[String]("fullname") ~
      get[String]("email")  map {
      case participantid~username~fullname~email => Participant( participantid, username, fullname, email, None )
    }
  }

  def encrypt(passwordOption: Option[String], salt: Int) = {
    passwordOption.map { password =>
//      val messageDigest = java.security.MessageDigest.getInstance("SHA-256");
//      val encryptedPassword = messageDigest.digest(password.getBytes);
    val encryptedPassword = BCrypt.hashpw(password,BCrypt.gensalt())
      Some(encryptedPassword)
    }.getOrElse(None)
  }

  def findByUsername(username: String) = {
    if (username != null && username.length > 3)  {
      Logger.debug("Looking up "+username)
      Some(Participant(0,"testuser","Test User","test@example.com",None))
    }  else
      None
  }

  def findByUsername2(username: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM participant
            WHERE username = {username}
        """
      ).on(
        'username -> username
      ).as(Participant.simple.singleOpt)
    }
  }

  def authenticate(username: String, password: String) = {
//    if (new Random().nextInt(10) > 5 ){
      Some(Participant(1,"testuser","Test User","test@example.com",None))
//    } else {
//      None
//    }
  }

  def authenticate2(username: String, password: String) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM participant
            WHERE username = {username}
            AND password = {password}
        """
      ).on(
        'username -> username,
        'password -> password
      ).as(Participant.simple.singleOpt).map { participant =>
        if(BCrypt.checkpw(password,participant.password.get)){
          Some(participant)
        } else {
          None
        }
      }.getOrElse(None)
    }
  }


  def save(participant: Participant) = {
    Logger.info("Inserting : " + participant)
    DB.withConnection { implicit connection =>
      val participantId = SQL("SELECT NEXTVAL('participant_seq')").as(scalar[Long].single)
      SQL(
        """
          INSERT INTO participant
          (participantid,username,fullname,email,password)
          VALUES
          ({participantid},{username},{fullname},{email},{password})
        """
      ).on(
        'participantid -> participantId,
        'username -> participant.username,
        'fullname -> participant.fullName,
        'email -> participant.email,
        'password -> participant.encryptedPassword
      ).executeInsert()
      participant.copy(participantId = participantId)
    }
  }


}