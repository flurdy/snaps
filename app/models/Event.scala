package models

import play.api.Play.current
import play.api.db.DB
import anorm._
import anorm.SqlParser._
import play.Logger


case class Event (
  eventId: Long,
  eventName: String,
  organiserId: Option[Long],
  eventDate: Option[String],
  description: Option[String],
  public: Boolean = true
){
  require(eventName.trim.length > 1)
  require( organiserId.isDefined || public )
//  require(description == None || description.get.trim.length < 3900)


  var albums: List[Album] = List()

  def this(eventName: String) = this(0,eventName,None,None,None)

  def this(eventName: String,organiserId: Long) = this(0,eventName,Some(organiserId),None,None)

  def this(eventName: String, organiserId: Option[Long], eventDate: Option[String], description: Option[String]) = this(0,eventName,organiserId,eventDate,description)

  def this(eventId: Long, that: Event) = this(eventId,that.eventName, that.organiserId, that.eventDate, that.description)

  val organiser : Option[Participant] = organiserId.map { participantId =>
    Participant.findById(participantId)
  }.getOrElse(None)

  def findAlbum(albumId: Long) = {
    Album.findAlbum(eventId,albumId)
  }

  def addAlbum(album: Album) = {
    Album.insertAlbum(eventId,album)
  }

  def removeAlbum(album: Album) = {
    Album.deleteAlbum(album.albumId)
  }

  def isParticipant(participant: Participant) = {
    Event.isParticipant(eventId,participant.participantId) || isOrganiser(participant)
  }

  def isOrganiser(participant: Participant) = {
    organiser.map { organiser => organiser == participant }.getOrElse(false)
  }

}



object Event {


  val simple = {
    get[Long]("eventid") ~
      get[String]("eventname") ~
      get[Option[Long]]("organiserid") ~
      get[Option[String]]("eventdate") ~
      get[Option[String]]("description") ~
      get[Boolean]("publicevent") map {
      case eventid~eventname~organiserid~eventdate~description~publicevent => Event( eventid, eventname, organiserid, eventdate, description, publicevent )
    }
  }

  def createAndSaveEvent(eventName: String) : Event = {
    createAndSaveEvent(new Event(eventName))
  }

  def createAndSaveEvent(event: Event) : Event = {
    DB.withConnection { implicit connection =>
      Logger.info("Inserting : " + event)
      val eventId = SQL("SELECT NEXTVAL('snapevent_seq')").as(scalar[Long].single)
      SQL(
        """
          INSERT INTO snapevent
          (eventid, eventname, organiser, eventdate, description, publicevent)
          VALUES
          ({eventid}, {eventname}, {organiser},
              {eventdate}, {description},{publicevent})
        """
      ).on(
        'eventid -> eventId,
        'eventname -> event.eventName,
        'organiser -> event.organiserId,
        'eventdate -> event.eventDate,
        'description -> event.description,
        'publicevent -> event.public
      ).executeInsert()
      new Event(eventId,event)
    }
  }


  def findEvent(eventId : Long): Option[Event] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapevent
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
          SELECT * FROM snapevent
          ORDER BY eventdate DESC,eventname ASC
        """
      ).on(
      ).as(Event.simple *)
    }
  }

  def updateEvent(event: Event) = {
    assert(event.eventName.trim.length > 1)
    DB.withConnection { implicit connection =>
      SQL(
        """
          UPDATE snapevent
          SET eventname = {eventname},
            organiser = {organiser},
            eventdate = {eventdate},
            description = {description},
            publicevent = {publicevent}
          WHERE eventid = {eventid}
        """
      ).on(
        'eventid -> event.eventId,
        'eventname -> event.eventName,
        'organiser -> event.organiserId,
        'eventdate -> event.eventDate,
        'description -> event.description,
        'publicevent -> event.public
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

  def findAllEventsContaining(searchText: String) = {
    val sqlSearch = "%" + searchText +"%"
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapevent
          WHERE eventname like {searchtext}
          ORDER BY eventdate DESC,eventname ASC
        """
      ).on(
        'searchtext -> sqlSearch
      ).as(Event.simple *)
    }
  }

  def findAllEventsByOrganisersContaining(searchText: String) = {
    val sqlSearch = "%" + searchText +"%"
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapevent
          WHERE organiser like {searchtext}
          ORDER BY organiser ASC,eventdate DESC,eventname ASC
        """
      ).on(
        'searchtext -> sqlSearch
      ).as(Event.simple *)
    }
  }

  def isParticipant(eventId: Long, participantId: Long) : Boolean = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select count(participantid) = 1
          from eventparticipant
          where eventid = {eventid}
          and participantid = {participantid}
        """
      ).on(
        'participantid -> participantId,
        'eventid -> eventId
      ).as(scalar[Boolean].single)
    }
  }

}
