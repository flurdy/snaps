package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.Logger
import models._

object EventController extends Controller with EventWrappers with Secured {

  val searchForm: Form[String] = Form(
    "searchText" -> text(maxLength = 100)
  )

  val createForm: Form[String] = Form(
      "eventName" -> nonEmptyText(minLength = 3 , maxLength = 100)
  )

  val updateForm:  Form[(String,String,String,String,Boolean)] = Form(
    tuple(
      "eventName" -> nonEmptyText(maxLength = 100),
      "organiser" -> text(maxLength = 100),
      "eventDate" -> text(maxLength = 100),
      "description" -> text (maxLength = 3900),
      "public"  -> boolean
    )//(Event.apply)(Event.unapply)
  )

  val participantForm = Form(
    "username" -> nonEmptyText(maxLength = 99)
  )

  def search = Action {  implicit request =>
    searchForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad search request:"+errors)
        BadRequest(views.html.index(errors,createForm,Application.registerForm))
      },
      searchText => {
        if (searchText.trim.length==0){
          Ok(views.html.events.list(searchText, Event.findAll))
        } else {
          val events = Event.findAllEventsContaining(searchText)
          val organisersEvents = Event.findAllEventsByOrganisersContaining(searchText)
          Ok(views.html.events.list(searchText,events ::: organisersEvents))
        }
      }
    )
  }

//  def eventNotFound = {
//    Logger.warn("Event not found")
//    NotFound.flashing("message" -> "Event not found")
//  }

  def notEventParticipant(event: Event)(implicit session:Session) = {
    Logger.debug("Not an event participant")
    Unauthorized(views.html.events.unauthorised(event)(currentParticipant)).flashing("message"->"Event private, and you do not have access to it")
  }

//  def notEventOrganiser = {
//    Logger.warn("Not an organiser")
//    Unauthorized.flashing("message" -> "Participant is not an organiser")
//  }

  def eventRequireAuthentication(implicit currentParticipant: Option[Participant], flash: Flash)  = {
    Logger.warn("Event requires participant")
    Unauthorized(views.html.login(Application.loginForm)).flashing("message"->"Event private, please log in")
  }

  def viewEvent(eventId: Long) = withEventAndAlbumsAndParticipants(eventId) { (event,albums,participants) => implicit request =>
    if(event.public || !event.organiser.isDefined){
      Ok(views.html.events.view(event,albums,participants))
    } else {
      currentParticipant match {
        case None => eventRequireAuthentication
        case Some(participant) => {
          if(event.isParticipant(participant) ) {
            Ok(views.html.events.view(event,albums,participants))
          } else {
            Logger.warn("Current participant is not a participant: " + participant.username )
            Logger.warn("Event organiser: " + event.organiser.get.username )
            notEventParticipant(event)
          }
        }
      }
    }
  }


  def createEvent = withParticipant { participant => implicit request =>
    createForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad create event request:"+errors)
        BadRequest(views.html.index(searchForm,errors,Application.registerForm))
      },
      eventName => {
        val event = participant.createAndSaveEvent(eventName)
        Redirect(routes.EventController.showEditEvent(event.eventId)).flashing("message" -> "Event created")
      }
    )
  }


  def showEditEvent(eventId: Long) = isEventOrganiser(eventId) { event => implicit request =>
     val editForm = updateForm.fill((event.eventName,
       event.organiser match {
         case None => ""
         case Some(participant) => participant.username
       },
       event.eventDate.getOrElse(""),
       event.description.getOrElse(""),
       event.public))
     val albums = Album.findAlbums(eventId)
     val participants = event.findParticipants
     Ok(views.html.events.edit(event,albums,participants,editForm))
  }


  def updateEvent(eventId: Long) = isEventOrganiser(eventId) { event => implicit request =>
    updateForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad update event request:"+errors)
        val albums = Album.findAlbums(event.eventId)
        val participants = event.findParticipants
        BadRequest(views.html.events.edit(event,albums,participants,errors))
      },
      updatedForm => {
          val updatedEvent = event.copy(
            eventName = updatedForm._1,
            //organiser = Option(updatedForm._2),
            eventDate = Option(updatedForm._3),
            description = Option(updatedForm._4) ,
            public = updatedForm._5 )
          Logger.warn("P:"+updatedEvent.public)
          Event.updateEvent(updatedEvent)
          Redirect(routes.EventController.viewEvent(event.eventId));
      }
    )
  }


  def showDeleteEvent(eventId: Long) = isEventOrganiser(eventId) { event => implicit request =>
    Ok(views.html.events.delete(event))
  }


  def deleteEvent(eventId: Long) =  isEventOrganiser(eventId) { event => implicit request =>
    Logger.info("Deleting event: " + eventId)
    Event.deleteEvent(eventId)
    Redirect(routes.Application.index()).flashing("message" -> "Event deleted");
  }


  def eventParticipantNotFound(eventId: Long) = {
    Logger.warn("Participant not found for remove participant")
    Redirect(routes.EventController.showEditEvent(eventId)).flashing("errorMessage" -> "Participant not found")
  }


  def removeParticipant(eventId: Long, participantId: Long) = isEventOrganiser(eventId) { event => implicit request =>
    Logger.debug("Remove participant: " + participantId +" from "+eventId)
    Participant.findById(participantId) match {
      case None => eventParticipantNotFound(eventId)
      case Some(participantToBeRemoved) => {
        event.removeParticipant(participantToBeRemoved)
        Redirect(routes.EventController.showEditEvent(eventId)).flashing("message" -> "Participant removed");
      }
    }
  }

  def addParticipant(eventId: Long) = isEventOrganiser(eventId) { event => implicit request =>
    participantForm.bindFromRequest.fold(
      errors => {
        for(error<-errors.errors){
          Logger.warn("Bad add participant request:"+error.key + " " + error.message)
        }
        val albums = Album.findAlbums(event.eventId)
        val participants = event.findParticipants
        BadRequest(views.html.events.edit(event,albums,participants,updateForm)).flashing("errorMessage" -> "Invalid participant");
      },
      addParticipantForm => {
        Participant.findByUsername(addParticipantForm) match {
          case None => eventParticipantNotFound(eventId)
          case Some(participantToBeAdded) => {
            Logger.debug("Add participant: " + participantToBeAdded.participantId + " to: "+eventId)
            event.addParticipant(participantToBeAdded)
            Redirect(routes.EventController.showEditEvent(eventId)).flashing("message" -> "Participant added");
          }
        }
      }
    )
  }

}



trait EventWrappers extends Secured {

  def onEventNotFound(request: RequestHeader) = Results.NotFound.flashing("message" -> "Event not found")

  def withEventAndAlbumsAndParticipants(eventId: Long)(f: (Event,Seq[Album],Seq[Participant]) => Request[AnyContent] => Result) =  {
    Event.findEvent(eventId).map {
      event => Action(request => f(event,event.findAlbums,event.findParticipants)(request))
    }.getOrElse(Action(request => onEventNotFound(request)))
  }

  def withEventAndParticipant(eventId: Long)(f: (Event,Participant) => Request[AnyContent] => Result) = withParticipant {
    participant => implicit request =>
      Event.findEvent(eventId).map {
        event =>
          f(event,participant)(request)
      }.getOrElse(onEventNotFound(request))
  }

  def isEventOrganiser(eventId: Long)(f: Event => Request[AnyContent] => Result) = withEventAndParticipant(eventId) {
    (event,participant) => implicit request =>
      if( event.isOrganiser(participant) ){
        f(event)(request)
      } else {
        onUnauthorised(request,event)(request.session)
      }
  }

  def isEventParticipant(eventId: Long)(f: (Event,Participant) => Request[AnyContent] => Result) = withEventAndParticipant(eventId) {
    (event,participant) => implicit request =>
      if( event.isParticipant(participant) ){
        f(event,participant)(request)
      } else {
        onUnauthorised(request,event)(request.session)
      }
  }

}