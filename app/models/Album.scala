package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger

case class Album (
  albumId: Long = 0,
  publisher: String,
  url: String,
  notes: Option[String]
){
  require(publisher.trim.length > 1)

  require(url.trim.length > 9)

  def this(publisher: String, url: String) = this(0,publisher,url,None)

  def this(albumId: Long, that: Album) = this(albumId,that.publisher,that.url,None)

}


object Album {

  val simple = {
      get[Long]("albumid") ~
      get[String]("publisher") ~
      get[String]("url")~
      get[Option[String]]("notes") map {
      case albumid~publisher~url~notes => Album( albumid, publisher, url, notes )
    }
  }

  def findAlbum(eventId: Long, albumId: Long) : Option[Album] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapalbum
          WHERE eventid = {eventid}
          AND albumid = {albumid}
        """
      ).on(
        'eventid -> eventId,
        'albumid -> albumId
      ).as(Album.simple.singleOpt)
    }
  }

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

  def insertAlbum(eventId: Long, album: Album) = {
    DB.withConnection { implicit connection =>
      Logger.info("Inserting : " + album)
      val albumId = SQL("SELECT NEXTVAL('snapalbum_seq')").as(scalar[Long].single)
      SQL(
        """
          INSERT INTO snapalbum (albumid,publisher,url,eventid,notes)
          VALUES ( {albumid},{publisher},{url},{eventid},{notes} )
        """
      ).on(
        'albumid -> albumId,
        'publisher -> album.publisher,
        'url -> album.url,
        'eventid -> eventId,
        'notes -> album.notes
      ).executeInsert()
      new Album(albumId,album)
    }
  }

  def updateAlbum(album: Album) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          UPDATE snapalbum
          SET  publisher = {publisher},
            url = {url}
          WHERE albumid = {albumid}
        """
      ).on(
        'albumid -> album.albumId,
        'publisher -> album.publisher,
        'url -> album.url
      ).executeUpdate()
    }
  }

  def deleteAlbum(albumId: Long) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          DELETE FROM snapalbum
          WHERE albumid = {albumid}
        """
      ).on(
        'albumid -> albumId
      ).execute()
    }
  }


  def deleteAllAlbumsByEvent(eventId: Long) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          DELETE FROM snapalbum
          WHERE eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).execute()
    }
  }

}
