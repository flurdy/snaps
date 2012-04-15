package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger

case class Album (
  albumId: Long = 0,
  publisher: String,
  url: String
){
  def this(publisher: String, url: String) = this(0,publisher,url)
}


object Album {

  val simple = {
      get[Long]("albumid") ~
      get[String]("publisher") ~
      get[String]("url") map {
      case albumid~publisher~url => Album( albumid, publisher, url )
    }
  }

  def findAlbum(eventId: Long, albumId: Long) = Album(0L,"John Smith","http://flickr.com")

  def findAlbums(eventId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapalbum
            WHERE eventid = {eventid}
            ORDER BY url
        """
      ).on(
        'eventid -> eventId
      ).as(Album.simple *)
    }
  }
}
