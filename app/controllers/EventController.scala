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
        val participantEvents = currentParticipant.map{ participant =>
          if (searchText.trim.length==0) {
            Event.findAllEventsAsParticipantOrOrganiser(participant.participantId)
          } else {
            Event.searchAllEventsAsParticipantOrOrganiser(searchText.trim,participant.participantId)
          }
        }.getOrElse(Seq.empty)
        if (searchText.trim.length==0){
          Ok(views.html.events.list("", participantEvents, Event.findAll, Participant.findAll))
        } else {
          val events : Seq[Event] = Event.searchAllSearchableEventsContaining(searchText.trim)
          val participants : Seq[Participant] = Participant.findParticipantsContaining(searchText.trim)
          Ok(views.html.events.list(searchText,participantEvents,events,participants))
        }
      }
    )
  }


  private def notEventParticipant(event: Event)(implicit session:Session, flash: Flash) = {
    Logger.info("Not an event participant")
    Unauthorized(views.html.events.unauthorised(event)).withSession(session+("eventId"->event.eventId.toString))
     //.flashing("errorMessage"->"Event private, and you do not have access to it")
  }


  private def eventRequireAuthentication(eventId: Long)(implicit request: RequestHeader, currentParticipant: Option[Participant], flash: Flash)  = {
    Logger.info("Event requires authentication")
    onUnauthenticated(request).withSession("eventId"-> eventId.toString).flashing("errorMessage"->"Event private, please log in")
  }


  def viewEvent(eventId: Long) = withRichEvent(eventId) { (event,albums,participants) => implicit request =>
    if(event.public || !event.organiser.isDefined){
      Ok(views.html.events.view(event,albums,participants)).withSession(session+("eventId"->eventId.toString))
    } else {
      currentParticipant match {
        case None => eventRequireAuthentication(eventId)
        case Some(participant) => {
          if(event.isParticipant(participant) ) {
            Ok(views.html.events.view(event,albums,participants))
          } else {
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
     val requests = event.findRequests
     Ok(views.html.events.edit(event,albums,participants,requests,editForm))
  }


  def updateEvent(eventId: Long) = isEventOrganiser(eventId) { event => implicit request =>
    updateForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad update event request:"+errors)
        val albums = Album.findAlbums(event.eventId)
        val participants = event.findParticipants
        val requests = event.findRequests
        BadRequest(views.html.events.edit(event,albums,participants,requests,errors))
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


  private def eventParticipantNotFound(eventId: Long) = {
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


  def addCurrentParticipant(eventId: Long) = isEventOrganiser(eventId) { event => implicit request =>
    participantForm.bindFromRequest.fold(
      errors => {
        for(error<-errors.errors){
          Logger.warn("Bad add participant request:"+error.key + " " + error.message)
        }
        val albums = Album.findAlbums(event.eventId)
        val participants = event.findParticipants
        val requests = event.findRequests
        BadRequest(views.html.events.edit(event,albums,participants,requests,updateForm)).flashing("errorMessage" -> "Invalid participant");
      },
      addParticipantForm => addAnyParticipant(event,Participant.findByUsername(addParticipantForm))
    )
  }

  def addRequestedParticipant(eventId: Long, participantId: Long) = isEventOrganiser(eventId) { event => implicit request =>
      addAnyParticipant(event,Participant.findById(participantId))
  }

  private def addAnyParticipant(event: Event, participantFound: Option[Participant]) = {
    participantFound.map { participant =>
      event.addParticipant(participant)
      event.removeJoinRequest(participant)
      Redirect(routes.EventController.showEditEvent(event.eventId)).flashing("message" -> "Participant added");
  }.getOrElse(eventParticipantNotFound(event.eventId))
}



  def requestToJoin(eventId: Long) = withEventAndParticipant(eventId) { (event,participant) => implicit request =>
    Logger.debug("Request to join:"+eventId)
    event.addJoinRequest(participant)
    if(event.public || !event.organiser.isDefined || event.isParticipant(participant) ){
      Redirect( routes.EventController.viewEvent(eventId) ).flashing(("message"->"Request to join sent"),("eventId"->eventId.toString))
    } else {
      Unauthorized(views.html.events.joinrequest(event))
    }
  }


}



trait EventWrappers extends Secured {

  def onEventNotFound(request: RequestHeader) = Results.NotFound.flashing("message" -> "Event not found")

  def withRichEvent(eventId: Long)(f: (Event,Seq[Album],Seq[Participant]) => Request[AnyContent] => Result) =  {
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
        onUnauthorised(request,event)(request.session,request.flash)
      }
  }

  def isEventParticipant(eventId: Long)(f: (Event,Participant) => Request[AnyContent] => Result) = withEventAndParticipant(eventId) {
    (event,participant) => implicit request =>
      if( event.isParticipant(participant) ){
        f(event,participant)(request)
      } else {
        onUnauthorised(request,event)(request.session,request.flash)
      }
  }

}