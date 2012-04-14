package models

import play.api.Play.current

class Event (
  eventName: String,
  eventDate: Option[String]
){
  require(eventName != null)

  var eventId: Long = 0

  def this(eventName: String) = this(eventName,None)

  def this(eventId: Long, eventName: String){
    this(eventName)
    this.eventId = eventId
  }
  def this(eventId: Long, eventName: String, eventDate: Option[String]){
    this(eventName,eventDate)
    this.eventId = eventId
  }
}

object Event {

}



class Album (
  albumId: Long,
  publisher: String,
  url: String
)
