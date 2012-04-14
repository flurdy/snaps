package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger


case class Event (
  eventId: Long,
  eventName: String,
  eventDate: Option[String],
  var albums : List[Album] = List()
){
  // require(eventName.trim.length > 1)

  def this(eventName: String) = this(0,eventName,None)

  def this(eventId: Long, that: Event) = this(eventId,that.eventName,that.eventDate)

  def findAlbum(albumId: Long) = {
    Album.findAlbum(eventId,albumId)
  }

  def createAlbum(album: Album)={
    albums = album :: albums
    album
  }

}


object Event {

  def findEvent(eventId : Long) = Event(1L,"Christmas",Option("2011-12-24"))

  def insertEvent(event: Event) : Long = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          INSERT INTO snapevent (eventname, eventdate) VALUES (
            {eventname}, {eventdate}
          )
        """
      ).on(
        'eventname -> event.eventName,
        'eventdate -> event.eventDate.getOrElse("")
      ).executeInsert()
      SQL(
        """
          SELECT eventid FROM snapevent
            WHERE eventname = {eventname}
            AND eventdate = {eventdate}
            ORDER BY eventid DESC
        """
      ).on(
        'eventname -> event.eventName,
        'eventdate -> event.eventDate.getOrElse("")
      ).as(scalar[Long].single)
    }
  }

}



case class Album (
  albumId: Long = 0,
  publisher: String,
  url: String
){
  def this(publisher: String, url: String) = this(0,publisher,url)
}


object Album {
  def findAlbum(eventId: Long, albumId: Long) = Album(0L,"John Smith","http://flickr.com")
}