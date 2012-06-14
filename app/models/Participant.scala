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
import notifiers.EmailNotifier

case class Participant(
                  participantId: Long = 0,
                  username: String,
                  fullName: Option[String],
                  email: String,
                  password: Option[String],
                  isAdmin:Boolean=false,
                  isSuperUser:Boolean=false){

  // require(Participant.ValidEmailAddress.findFirstIn(email).isDefined)

  lazy val encryptedPassword = Participant.encrypt(password)

  def createAndSaveEvent(eventName: String) = {
    Event.createAndSaveEvent(new Event(eventName,participantId,
          Participant.DateFormat.format(new java.util.Date())))
  }

  def storeAndSendEmailVerification {
    val verificationHash = Participant.generateVerificationHash
    Logger.debug("Email verification:"+email+" " + verificationHash)
    Participant.saveVerification(participantId,email,verificationHash)
    EmailNotifier.sendEmailVerification(this,verificationHash)
  }

  def markEmailAsVerified(verificationHash:String) {
    Participant.markEmailAsVerified(participantId,verificationHash)
  }

  def matchesVerificationHash(verificationHash: String) : Boolean = {
    Participant.findEmailVerification(participantId,verificationHash)
  }

}



object Participant {

  val ValidEmailAddress = """^[0-9a-zA-Z]([-\.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9}$""".r

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
      get[String]("email")  ~
      get[Boolean]("admin") ~
      get[Boolean]("superuser")  map {
      case participantid~username~fullname~email~isadmin~issuperuser=> Participant( participantid, username, fullname, email, None, isadmin, issuperuser )
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
          SELECT pa.* FROM participant pa
          LEFT JOIN emailverification ev
|          ON ev.participantid = pa.participantid
|          AND ev.email = pa.email
          WHERE pa.username = {username}
          AND ev.verified = TRUE
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

  def generateVerificationHash = {
    new BigInteger(130,  new SecureRandom()).toString(32)
  }

  def saveVerification(participantId: Long, email: String, verificationHash: String) {
    DB.withConnection { implicit connection =>
      val verificationId = SQL("SELECT NEXTVAL('emailverification_seq')").as(scalar[Long].single)
      SQL(
        """
              INSERT INTO emailverification
              (verificationid,participantid,email,verificationhash)
              VALUES
              ({verificationid},{participantid},{email},{verificationhash})
        """
      ).on(
        'verificationid -> verificationId,
        'participantid -> participantId ,
        'email -> email,
        'verificationhash -> verificationHash
      ).executeInsert()
    }
  }


  def markEmailAsVerified(participantId: Long,verificationHash: String) {
    DB.withConnection { implicit connection =>
      SQL(
        """
              UPDATE emailverification
              set verified = TRUE
              WHERE participantid = {participantid}
              AND verificationhash = {verificationhash}
        """
      ).on(
        'participantid -> participantId ,
        'verificationhash -> verificationHash
      ).executeUpdate()
    }
  }

  def findEmailVerification(participantId: Long, verificationHash: String) : Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
              SELECT count(verificationid) = 1 FROM emailverification
              WHERE participantid = {participantid}
              AND verificationhash = {verificationhash}
        """
      ).on(
        'participantid -> participantId ,
        'verificationhash -> verificationHash
      ).as(scalar[Boolean].single)
    }
  }


}