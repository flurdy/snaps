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
  // require(Participant.usernameExists(username))
  //  private lazy val salt = scala.util.Random.nextInt

  private lazy val salt = BCrypt.gensalt()
  lazy val encryptedPassword = Participant.encrypt(password,salt)

  def createEvent(eventName: String) = {
    Event.createEvent(new Event(eventName,fullName))
  }
}



object Participant {

  val authenticationMapper = {
    get[Long]("participantid") ~
      get[String]("username") ~
      get[Option[String]]("password")  map {
      case participantid~username~password => Participant( participantid, username, null, null, password )
    }
  }

  val simple = {
    get[Long]("participantid") ~
      get[String]("username") ~
      get[String]("fullname") ~
      get[String]("email")  map {
      case participantid~username~fullname~email => Participant( participantid, username, fullname, email, None )
    }
  }

  def encrypt(passwordOption: Option[String], salt: String) = {
    passwordOption.map { password =>
      val encryptedPassword = BCrypt.hashpw(password,salt)
      Some(encryptedPassword)
    }.getOrElse(None)
  }

  def findByUsername1(username: String) = {
    if (username != null && username.length > 3)  {
      Logger.debug("Looking up "+username)
      Some(Participant(0,"testuser","Test User","test@example.com",None))
    }  else
      None
  }

  def findByUsername(username: String) = {
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

  def authenticate1(username: String, password: String) = {
//    if (new Random().nextInt(10) > 5 ){
      Some(Participant(1,"testuser","Test User","test@example.com",None))
//    } else {
//      None
//    }
  }

  def authenticate(username: String, password: String) : Option[Participant]  = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM participant
            WHERE username = {username}
        """
      ).on(
        'username -> username
      ).as(Participant.authenticationMapper.singleOpt) match {
        case Some(participant) => {
          if(BCrypt.checkpw(password,participant.password.get)){
            Some(participant)
          } else {
            None
          }
        }
        case None => None
      }
    }
  }


  def save(participant: Participant) = {
    Logger.info("Inserting : " + participant)
    findByUsername(participant.username) match {
      case None => {
        DB.withConnection { implicit connection =>
          val newParticipantId = SQL("SELECT NEXTVAL('participant_seq')").as(scalar[Long].single)
          SQL(
            """
              INSERT INTO participant
              (participantid,username,fullname,email,password)
              VALUES
              ({participantid},{username},{fullname},{email},{password})
            """
          ).on(
            'participantid -> newParticipantId ,
            'username -> participant.username,
            'fullname -> participant.fullName,
            'email -> participant.email,
            'password -> participant.encryptedPassword
          ).executeInsert()
          participant.copy(participantId = newParticipantId )
        }
      }
      case Some(existingParticipant) => {
        throw new IllegalArgumentException("Username already exists")
      }
    }
  }

}