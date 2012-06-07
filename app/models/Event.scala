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
  public: Boolean = true,
  searchable: Boolean = true
){

  require(eventName.trim.length > 1)
  require( organiserId.isDefined || public )
//  require(description == None || description.get.trim.length < 3900)


//  var albums: List[Album] = List()

  def this(eventName: String) = this(0,eventName,None,None,None)

  def this(eventName: String,organiserId: Long) = this(0,eventName,Some(organiserId),None,None)

  def this(eventName: String,organiserId: Long,eventDate: String) = this(0,eventName,Option(organiserId),Option(eventDate),None)

  def this(eventName: String, organiserId: Option[Long], eventDate: Option[String], description: Option[String]) = this(0,eventName,organiserId,eventDate,description)

  def this(eventId: Long, that: Event) = this(eventId,that.eventName, that.organiserId, that.eventDate, that.description)

  val organiser : Option[Participant] = organiserId.map { participantId =>
    Participant.findById(participantId)
  }.getOrElse(None)

  def findAlbum(albumId: Long) = Album.findAlbum(eventId,albumId)

  def findAlbums: Seq[Album] = Album.findAlbums(eventId)

  def addAlbum(album: Album) = Album.insertAlbum(eventId,album)

  def removeAlbum(album: Album) = Album.deleteAlbum(album.albumId)

  def isParticipant(participant: Participant) = {
    Event.isParticipant(eventId,participant.participantId) || isOrganiser(participant)
  }

  def isOrganiser(participant: Participant) = {
    organiser.map { organiser => organiser == participant }.getOrElse(false)
  }

  def findParticipants : Seq[Participant]= {
    Participant.findParticipantsByEvent(eventId)
  }

  def removeParticipant(participant: Participant) = {
    Event.removeParticipant(eventId,participant.participantId)
    this
  }

  def addParticipant(participant: Participant) = {
    if( !Event.isParticipant(eventId,participant.participantId) ) {
      Event.addParticipant(eventId,participant.participantId)
    } else {
      Logger.info("Already event participant: " + participant.participantId)
    }
    this
  }


  def findRequests : Seq[Participant]= {
    Participant.findRequestsByEvent(eventId)
  }

  def addJoinRequest(participant: Participant) = {
    if( !Event.isParticipant(eventId,participant.participantId) ) {
      if( !Event.isAlreadyJoinRequested(eventId,participant.participantId) ) {
        Event.addJoinRequest(eventId,participant.participantId)
      } else {
        Logger.info("Already requested to join event: " + participant.participantId)
      }
    } else {
      Logger.info("Already event participant: " + participant.participantId)
    }
    this
  }

  def removeJoinRequest(participant: Participant) = {
      Event.removeJoinRequest(eventId,participant.participantId)
  }

}



object Event {


  val simple = {
    get[Long]("eventid") ~
      get[String]("eventname") ~
      get[Option[Long]]("organiserid") ~
      get[Option[String]]("eventdate") ~
      get[Option[String]]("description") ~
      get[Boolean]("publicevent") ~
      get[Boolean]("searchable") map {
      case eventid~eventname~organiserid~eventdate~description~publicevent~searchable => Event( eventid, eventname, organiserid, eventdate, description, publicevent, searchable )
    }
  }

  def createAndSaveEvent(eventName: String, organiserId: Long) : Event = {
    createAndSaveEvent(new Event(eventName,organiserId))
  }

  def createAndSaveEvent(event: Event) : Event = {
    DB.withConnection { implicit connection =>
      Logger.info("Inserting : " + event)
      val eventId = SQL("SELECT NEXTVAL('snapevent_seq')").as(scalar[Long].single)
      SQL(
        """
          INSERT INTO snapevent
          (eventid, eventname, organiserid, eventdate, description, publicevent, searchable)
          VALUES
          ({eventid}, {eventname}, {organiserid},
              {eventdate}, {description},{publicevent},{searchable})
        """
      ).on(
        'eventid -> eventId,
        'eventname -> event.eventName,
        'organiserid -> event.organiserId,
        'eventdate -> event.eventDate,
        'description -> event.description,
        'publicevent -> event.public,
        'searchable -> event.searchable
      ).executeInsert()
      new Event(eventId,event)
    }
  }


  def updateEvent(event: Event) = {
    assert(event.eventName.trim.length > 1)
    DB.withConnection { implicit connection =>
      SQL(
        """
          UPDATE snapevent
          SET eventname = {eventname},
            organiserid = {organiserid},
            eventdate = {eventdate},
            description = {description},
            publicevent = {publicevent},
            searchable = {searchable}
          WHERE eventid = {eventid}
        """
      ).on(
        'eventid -> event.eventId,
        'eventname -> event.eventName,
        'organiserid -> event.organiserId,
        'eventdate -> event.eventDate,
        'description -> event.description,
        'publicevent -> event.public,
        'searchable -> event.searchable
      ).executeUpdate()
      event
    }
  }

  def deleteEvent(eventId: Long){
    removeAllJoinRequestsByEvent(eventId)
    removeAllParticipantsByEvent(eventId)
    Album.deleteAllAlbumsByEvent(eventId)
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


  def findEventWithAlbumsAndParticipants(eventId : Long): Option[(Event,Seq[Album],Seq[Participant])] = {
    findEvent(eventId).map { event =>
      Option((event,event.findAlbums,event.findParticipants))
    }.getOrElse(None)
  }


  def findAll: Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapevent
          WHERE searchable = TRUE
          ORDER BY eventdate DESC,eventname ASC
        """
      ).on(
      ).as(Event.simple *)
    }
  }


  def findAllEventsAsParticipantOrOrganiser(participantId: Long) : Seq[Event] = {
    findAllEventsByOrganiser(participantId) union findAllEventsByParticipant(participantId)
  }


  def findAllEventsByOrganiser(organiserId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT sn.*
          FROM snapevent sn
          WHERE sn.organiserid = {organiserid}
          ORDER BY sn.eventdate DESC, sn.eventname ASC
        """
      ).on(
        'organiserid -> organiserId
      ).as(Event.simple *)
    }
  }

  def findAllEventsByParticipant(participantid: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        SELECT sn.*
        FROM eventparticipant ep
        LEFT JOIN snapevent sn
          ON ep.eventid = sn.eventid
        WHERE ep.participantid = {participantid}
        ORDER BY sn.eventdate DESC, sn.eventname ASC
        """
      ).on(
        'participantid -> participantid
      ).as(Event.simple *)
    }
  }


  def searchAllSearchableEventsContaining(searchText: String) = {
    val sqlSearch = "%" + searchText +"%"
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT * FROM snapevent
          WHERE eventname like {searchtext}
          AND searchable = TRUE
          ORDER BY eventdate DESC,eventname ASC
        """
      ).on(
        'searchtext -> sqlSearch
      ).as(Event.simple *)
    }
  }


  private def searchAllEventsAsParticipant(sqlSearch: String, participantId: Long) : Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          SELECT sn.*
          FROM eventparticipant ep
          LEFT JOIN snapevent sn
            ON ep.eventid = sn.eventid
          WHERE sn.eventname like {searchtext}
          AND ep.participantid = {participantid}
          ORDER BY sn.eventdate DESC, sn.eventname ASC
        """
      ).on(
        'searchtext -> sqlSearch,
        'participantid -> participantId
      ).as(Event.simple *)
    }
  }

  private def searchAllEventsAsOrganiser(sqlSearch: String, participantId: Long) : Seq[Event] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
        SELECT sn.*
        FROM snapevent sn
        WHERE sn.eventname like {searchtext}
        AND sn.organiserid = {participantid}
        ORDER BY sn.eventdate DESC, sn.eventname ASC
        """
      ).on(
        'searchtext -> sqlSearch,
        'participantid -> participantId
      ).as(Event.simple *)
    }
  }


  def searchAllEventsAsParticipantOrOrganiser(searchText: String, participantId: Long) : Seq[Event] = {
    val   sqlSearch = "%" + searchText +"%"
    searchAllEventsAsParticipant(sqlSearch,participantId) union searchAllEventsAsOrganiser(sqlSearch,participantId)
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

  def removeParticipant(eventId: Long, participantId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from eventparticipant
          where eventid = {eventid}
          and participantid = {participantid}
        """
      ).on(
        'participantid -> participantId,
        'eventid -> eventId
      ).execute()
    }
  }


  def addParticipant(eventId: Long, participantId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into eventparticipant
          (eventid,participantid)
          values
          ({eventid},{participantid})
        """
      ).on(
        'participantid -> participantId,
        'eventid -> eventId
      ).executeInsert()
    }
  }


  def addJoinRequest(eventId: Long, participantId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into eventrequest
          (eventid,participantid,requestdate)
          values
          ({eventid},{participantid},CURRENT_TIMESTAMP)
        """
      ).on(
        'participantid -> participantId,
        'eventid -> eventId
      ).executeInsert()
    }
  }


  def isAlreadyJoinRequested(eventId: Long, participantId: Long) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select count(participantid) = 1
          from eventrequest
          where eventid = {eventid}
          and participantid = {participantid}
        """
      ).on(
        'participantid -> participantId,
        'eventid -> eventId
      ).as(scalar[Boolean].single)
    }
  }


  def removeJoinRequest(eventId: Long, participantId: Long) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from eventrequest
          where eventid = {eventid}
          and participantid = {participantid}
        """
      ).on(
        'participantid -> participantId,
        'eventid -> eventId
      ).execute()
    }
  }


  def removeAllJoinRequestsByParticipant(participantId: Long) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from eventrequest
          where participantid = {participantid}
        """
      ).on(
        'participantid -> participantId
      ).execute()
    }
  }

  private def removeAllJoinRequestsByEvent(eventId: Long) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from eventrequest
          where eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).execute()
    }
  }



  private def removeAllParticipantsByEvent(eventId: Long) {
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from eventparticipant
          where eventid = {eventid}
        """
      ).on(
        'eventid -> eventId
      ).execute()
    }
  }



  def removeParticipantFromAllEvents(participantId: Long) {
    // TODO: resolve PG sql error
    Logger.info("Delete participant from events: "+participantId)
    DB.withConnection { implicit connection =>
      SQL(
        """
          delete from eventparticipant
          where participantid = {participantid}
        """
      ).on(
        'participantid -> participantId
      ).execute()
    }
  }

  def removeAllEventsByOrganiser(participantId: Long) = {
    findAllEventsByOrganiser(participantId).map { event =>
      Event.deleteEvent(event.eventId)
    }
  }
}
