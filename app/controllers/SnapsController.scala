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
      "eventDate" -> nonEmptyText
    ) //(Event.apply)(Event.unapply)
  )

  def search = Action {  implicit request =>
    searchForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      searchText => {
        Logger.info("Find events")
        // TODO: find events
        Ok(views.html.events.list(searchText))
      }
    )
  }

  def viewEvent(eventId: Long) = Action {
    val event = Event.findEvent(eventId);
    Ok(views.html.events.view(event))
  }


  def createEvent = Action { implicit request =>
    createForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      eventName => {
        Logger.info("Create event")
        val event = new Event(eventName)
        // TODO: logic
        // TODO: persist event
        Redirect(routes.EventController.showEditEvent(event.eventId));
      }
    )
  }

  def showEditEvent(eventId: Long) = Action {
    val event = Event.findEvent(eventId);
    // TODO: transform
    Ok(views.html.events.edit(event,updateForm))//.fill(event)))
  }



  def updateEvent(eventId: Long) = Action { implicit request =>
    updateForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        val event = Event.findEvent(eventId);
        BadRequest(views.html.events.edit(event,updateForm))
      },
      eventName => {
        Logger.info("Update event")
        val event = Event.findEvent(eventId);
        // TODO: logic
        // TODO: persist changes
        Redirect(routes.EventController.viewEvent(eventId));
      }
    )
  }

  def showDeleteEvent(eventId: Long) = Action {
    val event = Event.findEvent(eventId);
    Ok(views.html.events.delete(event))
  }

  def deleteEvent(eventId: Long) =  Action {
    Logger.info("Delete event")
    val event = Event.findEvent(eventId);
    // TODO: persist delete
    Redirect(routes.Application.index());
  }

}


object AlbumController extends Controller {

  val albumForm = Form(
    tuple(
      "publisher" -> nonEmptyText(maxLength = 100),
      "url" -> nonEmptyText(maxLength = 150)
    )
  )

  def showAddAlbum(eventId: Long) = Action {
    val event = Event.findEvent(eventId);
    Ok(views.html.albums.add(event,albumForm))
  }

  def addAlbum(eventId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        val event = Event.findEvent(eventId);
        BadRequest(views.html.albums.add(event,albumForm))
      },
      album => {
        Logger.info("Add album")
        val event = Event.findEvent(eventId);
        val album = event.createAlbum(new Album("publisher","url"))
        // TODO: Logic
        // TODO: persist changes
        Redirect(routes.EventController.viewEvent(eventId));
      }
    )
  }

  def updateAlbum(eventId: Long,albumId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        val event = Event.findEvent(eventId);
        BadRequest(views.html.events.edit(event,EventController.updateForm))
      },
      album => {
        Logger.info("Update album")
        val event = Event.findEvent(eventId);
        val album = event.findAlbum(albumId);
        // TODO: Logic
        // TODO: persist changes
        Redirect(routes.EventController.showEditEvent(eventId));
      }
    )
  }

  def removeAlbum(eventId: Long,albumId: Long) = Action {
      Logger.info("Remove album")
      val event = Event.findEvent(eventId);
      val album = event.findAlbum(albumId);
      // TODO: Logic
      // TODO: persist delete
      Redirect(routes.EventController.showEditEvent(eventId));
  }


}