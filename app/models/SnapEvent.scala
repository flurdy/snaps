package models

import play.api.Play.current

case class Event (
  eventId: Long,
  eventName: String,
  eventDate: Option[String],
  var albums : List[Album] = List()
){
  // require(eventName.trim.length > 1)

  def this(eventName: String) = this(0,eventName,None)

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