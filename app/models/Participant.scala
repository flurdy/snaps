package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger
import util.Random
import org.mindrot.jbcrypt.BCrypt
import java.util.Date
import java.text.SimpleDateFormat
import java.math.BigInteger
import java.security.{SecureRandom, MessageDigest}

case class Participant(
                  participantId: Long = 0,
                  username: String,
                  fullName: Option[String],
                  email: Option[String],
                  password: Option[String] ){

  lazy val encryptedPassword = Participant.encrypt(password)

  def createAndSaveEvent(eventName: String) = {
    Event.createAndSaveEvent(new Event(eventName,participantId,Participant.DateFormat.format(new java.util.Date())))
  }


}



object Participant {

  val DateFormat = new SimpleDateFormat("yyyy-MM-dd")

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
      get[Option[String]]("fullname") ~
      get[Option[String]]("email")  map {
      case participantid~username~fullname~email => Participant( participantid, username, fullname, email, None )
    }
  }

  def encrypt(passwordOption: Option[String]) = {
    passwordOption.map { password =>
      val encryptedPassword = BCrypt.hashpw(password,BCrypt.gensalt())
      Some(encryptedPassword)
    }.getOrElse(None)
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

  def findById(participantId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM participant
            WHERE participantid = {participantid}
        """
      ).on(
        'participantid -> participantId
      ).as(Participant.simple.singleOpt)
    }
  }

  def findAll = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM participant
          ORDER BY username
        """
      ).as(Participant.simple *)
    }
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
          if(BCrypt.checkpw(password,participant.password.getOrElse(""))){
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


  def findParticipantsByEvent(eventId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select pa.*
          from eventparticipant ep
          left join participant pa
          on pa.participantid = ep.participantid
          where ep.eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).as(Participant.simple *)
    }
  }


  def findRequestsByEvent(eventId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select pa.*
          from eventrequest er
          left join participant pa
          on pa.participantid = er.participantid
          where er.eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).as(Participant.simple *)
    }
  }

  def findParticipantsContaining(searchText: String) = {
      val sqlSearch = "%" + searchText +"%"
      DB.withConnection { implicit connection =>
        SQL(
          """
            SELECT * FROM participant pa
            WHERE pa.username like {searchtext}
            OR pa.fullname like {searchtext}
            ORDER BY pa.username ASC
          """
        ).on(
          'searchtext -> sqlSearch
        ).as(Participant.simple *)
      }
  }


  def updateParticipant(participant: Participant) = {
    findById(participant.participantId) match {
      case Some(existingParticipant) => {
        DB.withConnection { implicit connection =>
          SQL(
            """
              UPDATE participant
              SET username  = {username},
                fullname = {fullname},
                email = {email}
              WHERE participantid = {participantid}
            """
          ).on(
            'participantid -> participant.participantId,
            'username -> participant.username,
            'fullname -> participant.fullName,
            'email -> participant.email
          ).executeInsert()
        }
      }
      case None => participantNotFound
    }
  }


  def updatePassword(participant: Participant) = {
    findById(participant.participantId) match {
      case Some(existingParticipant) => {
        DB.withConnection { implicit connection =>
          SQL(
            """
              UPDATE participant
              SET password  = {password}
              WHERE participantid = {participantid}
            """
          ).on(
            'participantid -> participant.participantId,
            'password -> participant.encryptedPassword
          ).executeInsert()
        }
      }
      case None => participantNotFound
    }
  }

  def deleteAccount(participantId: Long)  {
    require(participantId>0)
    try {
      Event.removeAllJoinRequestsByParticipant(participantId)
      Event.removeParticipantFromAllEvents(participantId)
      Event.removeAllEventsByOrganiser(participantId)
    } catch {
      case exception: Exception => Logger.error("Exception in delete account: " + exception.getMessage,exception)
    }
    deleteParticipant(participantId)
  }

  def deleteParticipant(participantId: Long) {
    Logger.info("Deleting participant: " + participantId)
    DB.withConnection { implicit connection =>
      SQL(
        """
          DELETE FROM participant
          WHERE participantid = {participantid}
        """
      ).on(
        'participantid -> participantId
      ).execute()
    }
  }

  def generateRandomPassword = {
    val source = new BigInteger(130,  new SecureRandom()).toString(32);
    source.substring(0,3)+"-"+source.substring(4,7)+"-"+source.substring(8,11)+"-"+source.substring(12,15)
  }

  def resetPassword(participantId:Long) : String = {
    findById(participantId).map { participant =>
        val newPassword = generateRandomPassword
        updatePassword( participant.copy(password=Option(newPassword)) )
        newPassword
    }.getOrElse(participantNotFound)
  }

  def participantNotFound = throw new NullPointerException("Participant not found")

}