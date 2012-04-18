package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger
import util.Random

case class Participant(
                  username: String,
                  fullName: String,
                  email: String,
                  password: Option[String] ){

  def createEvent(eventName: String) = {
    Event.createEvent(new Event(eventName,fullName))
  }

}



object Participant {

  val simple = {
    get[String]("username") ~
      get[String]("fullname") ~
      get[String]("email")  map {
      case username~fullname~email => Participant( username, fullname, email, None )
    }
  }

  def findByUsername(username: String) = {
    if (username != null && username.length > 3)  {
      Logger.debug("Looking up "+username)
      Some(Participant("testuser","Test User","test@example.com",None))
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
      Some(Participant("testuser","Test User","test@example.com",None))
//    } else {
//      None
//    }
  }


}