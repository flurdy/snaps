package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.Logger
import models._


object AlbumController extends Controller {

  val albumForm = Form(
    tuple(
      "publisher" -> nonEmptyText(maxLength = 100),
      "url" -> nonEmptyText(maxLength = 150)
    )
  )

  def showAddAlbum(eventId: Long) = Action {
    Event.findEvent(eventId) match {
      case None => NotFound
      case Some(event) => Ok(views.html.albums.add(event,albumForm))
    }
  }

  def addAlbum(eventId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        Event.findEvent(eventId) match {
          case None => NotFound
          case Some(event) => BadRequest(views.html.albums.add(event,albumForm))
        }
      },
      album => {
        Logger.info("Add album")
        Event.findEvent(eventId) match {
          case None => NotFound
          case Some(event) => {
            val album = event.createAlbum(new Album("publisher","url"))
            // TODO: Logic
            // TODO: persist changes
            Redirect(routes.EventController.viewEvent(eventId));
          }
        }
      }
    )
  }

  def updateAlbum(eventId: Long,albumId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        Event.findEvent(eventId) match {
          case None => NotFound
          case Some(event) => {
            val albums = Album.findAlbums(eventId)
            BadRequest(views.html.events.edit(event,albums,EventController.updateForm))
          }
        }
      },
      album => {
        Logger.info("Update album")
        Event.findEvent(eventId) match {
          case None => NotFound
          case Some(event) => {
            val album = event.findAlbum(albumId);
            // TODO: Logic
            // TODO: persist changes
            Redirect(routes.EventController.showEditEvent(eventId));
          }
        }
      }
    )
  }

  def removeAlbum(eventId: Long,albumId: Long) = Action {
    Logger.info("Remove album")
    Event.findEvent(eventId) match {
      case None => NotFound
      case Some(event) => {
        val album = event.findAlbum(albumId);
        // TODO: Logic
        // TODO: persist delete
        Redirect(routes.EventController.showEditEvent(eventId));
      }
    }
  }


}