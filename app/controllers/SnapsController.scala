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
    // TODO: find event
    Ok(views.html.events.view())
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
        // TODO: persist event
        Redirect(routes.EventController.showEditEvent(1));
      }
    )
  }

  def showEditEvent(eventId: Long) = Action {
    // TODO: find event
//    val event = new Event("Crhistmas","2012-01-01")
    Ok(views.html.events.edit(updateForm))//.fill(event)))
  }



  def updateEvent(eventId: Long) = Action { implicit request =>
    updateForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.events.edit(updateForm))
      },
      eventName => {
        Logger.info("Update event")
        // TODO: find event
        // TODO: persist changes
        Redirect(routes.EventController.viewEvent(1));
      }
    )
  }

  def showDeleteEvent(eventId: Long) = Action {
    // TODO: find event
    Ok(views.html.events.delete())
  }

  def deleteEvent(eventId: Long) =  Action {
    Logger.info("Delete event")
    // TODO: find event
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
    // TODO: find event
    Ok(views.html.albums.add(albumForm))
  }

  def addAlbum(eventId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.albums.add(albumForm))
      },
      album => {
        Logger.info("Add album")
        // TODO: find event
        // TODO: Create album
        // TODO: persist changes
        Redirect(routes.EventController.viewEvent(1));
      }
    )
  }

  def updateAlbum(eventId: Long,albumId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.events.edit(EventController.updateForm))
      },
      album => {
        Logger.info("Update album")
        // TODO: find album
        // TODO: persist changes
        Redirect(routes.EventController.showEditEvent(1));
      }
    )
  }

  def removeAlbum(eventId: Long,albumId: Long) = Action {
      Logger.info("Remove album")
      // TODO: find album
      // TODO: persist delete
      Redirect(routes.EventController.showEditEvent(1));
  }


}