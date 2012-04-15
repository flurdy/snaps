package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.Logger
import models._

object EventController extends Controller {

  val searchForm: Form[String] = Form(
    "searchText" -> text(maxLength = 100)
  )

  val createForm: Form[String] = Form(
    "eventName" -> nonEmptyText(maxLength = 100)
  )

  val updateForm:  Form[(String,String)] = Form(
    tuple(
      "eventName" -> nonEmptyText(maxLength = 100),
      "eventDate" -> text
    ) //(Event.apply)(Event.unapply)
  )


  def search = Action {  implicit request =>
    searchForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      searchText => {
        Logger.debug("Find events")
        val events = Event.findAll
        // TODO: search for events
        Ok(views.html.events.list(searchText,events))
      }
    )
  }


  def viewEvent(eventId: Long) = Action {
    Event.findEventWithAlbums(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => Ok(views.html.events.view(event))
    }
  }


  def createEvent = Action { implicit request =>
    createForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      eventName => {
        val event = new Event(eventName)
        val eventId = Event.insertEventAndReturnId(event)
        Redirect(routes.EventController.showEditEvent(eventId))
      }
    )
  }


  def showEditEvent(eventId: Long) = Action {
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => {
        val editForm:  Form[(String,String)] = updateForm
        val editForm2 = editForm.fill((event.eventName,event.eventDate.getOrElse("")))
        val albums = Album.findAlbums(eventId)
        Ok(views.html.events.edit(event,albums,editForm2))
      }
    }
  }


  def updateEvent(eventId: Long) = Action { implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => {
        updateForm.bindFromRequest.fold(
          errors => {
            Logger.warn("Bad request:"+errors)
            val albums = Album.findAlbums(eventId)
            BadRequest(views.html.events.edit(event,albums,updateForm))
          },
          updatedForm => {
            val updatedEvent = event.copy(
                  eventName = updatedForm._1,
                  eventDate = Option(updatedForm._2) )
            Event.updateEvent(updatedEvent)
            Redirect(routes.EventController.viewEvent(eventId));
          }
        )
      }
    }
  }



  def showDeleteEvent(eventId: Long) = Action {
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => Ok(views.html.events.delete(event))
    }
  }


  def deleteEvent(eventId: Long) =  Action {
    Logger.info("Deleting event: " + eventId)
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => {
        Event.deleteEvent(eventId)
        Redirect(routes.Application.index());
      }
    }
  }

}