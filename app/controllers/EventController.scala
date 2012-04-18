package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.Logger
import models._

object EventController extends Controller with Secured {

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


  def search = Action {  implicit request =>
    searchForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad search request:"+errors)
        BadRequest(views.html.index(errors,createForm))
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



  def viewEvent(eventId: Long) = Action { implicit request =>
    Event.findEventWithAlbums(eventId) match {
      case None => {
        Logger.warn("Event not found on view")
        NotFound
      }
      case Some(event) => Ok(views.html.events.view(event))
    }
  }


  def createEvent = withParticipant { participant => implicit request =>
    createForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad create event request:"+errors)
        BadRequest(views.html.index(searchForm,errors))
      },
      eventName => {
        val event = participant.createEvent(eventName)
        Redirect(routes.EventController.showEditEvent(event.eventId))
      }
    )
  }


  def showEditEvent(eventId: Long) = isAuthenticated { username => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("No event found on show edit")
        NotFound
      }
      case Some(event) => {
        val editForm = updateForm.fill((event.eventName,
            event.organiser.getOrElse(""),
            event.eventDate.getOrElse(""),
            event.description.getOrElse(""),
            event.public))
        val albums = Album.findAlbums(eventId)
        Ok(views.html.events.edit(event,albums,editForm))
      }
    }
  }


  def updateEvent(eventId: Long) = isAuthenticated { username => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found om update event")
        NotFound
      }
      case Some(event) =>  {
        updateForm.bindFromRequest.fold(
          errors => {
            Logger.warn("Bad update event request:"+errors)
            val albums = Album.findAlbums(event.eventId)
            BadRequest(views.html.events.edit(event,albums,updateForm))
          },
          updatedForm => {
            val updatedEvent = event.copy(
              eventName = updatedForm._1,
              organiser = Option(updatedForm._2),
              eventDate = Option(updatedForm._3),
              description = Option(updatedForm._4) )
            Event.updateEvent(updatedEvent)
            Redirect(routes.EventController.viewEvent(event.eventId));
          }
        )
      }
    }
  }


  def showDeleteEvent(eventId: Long) = isAuthenticated { username => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("No event found on show delete event")
        NotFound
      }
      case Some(event) => Ok(views.html.events.delete(event))
    }
  }


  def deleteEvent(eventId: Long) =  isAuthenticated { username => implicit request =>
    Logger.info("Deleting event: " + eventId)
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("No event found on delete event")
        NotFound
      }
      case Some(event) => {
        Event.deleteEvent(eventId)
        Redirect(routes.Application.index());
      }
    }
  }

  implicit def participant(implicit session : Session): Option[Participant] = {
    Participant.findByUsername(session.get(Security.username).getOrElse(""))
  }

}