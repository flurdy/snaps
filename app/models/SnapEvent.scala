package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger


case class Event (
  eventId: Long,
  eventName: String,
  eventDate: Option[String]
){

  // require(eventName.trim.length > 1)

  var albums: List[Album] = List()

  def this(eventName: String) = this(0,eventName,None)

  def this(eventId: Long, that: Event) = this(eventId,that.eventName,that.eventDate)

  def this(eventId: Long, eventName: String, eventDate: Option[String], albums: List[Album]) {
    this(eventId, eventName, eventDate)
    this.albums = albums;
  }

  def findAlbum(albumId: Long) = {
    Album.findAlbum(eventId,albumId)
  }

  def addAlbum(album: Album) = {
    Album.insertAlbum(eventId,album)
  }

  def removeAlbum(album: Album) = {
    Album.deleteAlbum(album.albumId)
  }
}


object Event {

  val simple = {
    get[Long]("eventid") ~
      get[String]("eventname") ~
      get[String]("eventdate") map {
      case eventid~eventname~eventdate => Event( eventid, eventname, Option(eventdate) )
    }
  }

  def insertEventAndReturnId(event: Event) : Long = {
    DB.withConnection { implicit connection =>
      Logger.info("Inserting : " + event)
      val eventId = SQL("SELECT NEXTVAL('snapevent_seq')").as(scalar[Long].single)
      SQL(
        """
          INSERT INTO snapevent (eventid,eventname, eventdate) VALUES
          ({eventid}, {eventname}, {eventdate})

        """
      ).on(
        'eventid -> eventId,
        'eventname -> event.eventName,
        'eventdate -> event.eventDate.getOrElse("")
      ).executeInsert()
      eventId
    }
  }


  def findEvent(eventId : Long): Option[Event] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT eventid,eventname,eventdate FROM snapevent
            WHERE eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).as(Event.simple.singleOpt)
    }
  }


  def findEventWithAlbums(eventId : Long): Option[Event] = {
    findEvent(eventId) match {
      case None => None
      case Some(event) => {
        event.albums = Album.findAlbums(eventId)
        Option(event)
      }
    }
  }


  def findAll: Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT eventid,eventname,eventdate FROM snapevent
          ORDER BY eventid
        """
      ).on(
      ).as(Event.simple *)
    }
  }

  def updateEvent(event: Event) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          UPDATE snapevent
          SET eventname = {eventname},
            eventdate = {eventdate}
          WHERE eventid = {eventid}
        """
      ).on(
        'eventid -> event.eventId,
        'eventname -> event.eventName,
        'eventdate -> event.eventDate.getOrElse("")
      ).executeUpdate()
      event
    }
  }

  def deleteEvent(eventId: Long){
    DB.withConnection { implicit connection =>
      SQL(
        """
          DELETE FROM snapevent
          WHERE eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).execute()
    }
  }

}
