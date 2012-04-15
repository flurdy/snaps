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
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => Ok(views.html.albums.add(event,albumForm))
    }
  }

  def addAlbum(eventId: Long) = Action { implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        Event.findEvent(eventId) match {
          case None => {
            Logger.warn("Not found")
            NotFound
          }
          case Some(event) => BadRequest(views.html.albums.add(event,albumForm))
        }
      },
      submittedAlbumForm => {
        Event.findEvent(eventId) match {
          case None => {
            Logger.warn("Not found")
            NotFound
          }
          case Some(event) => {
            val album = new Album(submittedAlbumForm._1,submittedAlbumForm._2)
            event.addAlbum(album)
            Redirect(routes.EventController.viewEvent(eventId));
          }
        }
      }
    )
  }

  def updateAlbum(eventId: Long,albumId: Long) = Action { implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => {
        event.findAlbum(albumId) match {
          case None => {
            Logger.warn("Not found")
            NotFound
          }
        case Some(album) => {
          albumForm.bindFromRequest.fold(
            errors => {
              Logger.warn("Bad request:"+errors)
              val albums = Album.findAlbums(eventId)
              BadRequest(views.html.events.edit(event,albums,EventController.updateForm))
            },
            submittedAlbumForm => {
                val updatedAlbum = album.copy(
                    publisher = submittedAlbumForm._1,
                    url = submittedAlbumForm._2)
                Album.updateAlbum(updatedAlbum)
                Redirect(routes.EventController.showEditEvent(eventId));
              }
            )
          }
        }
      }
    }
  }

  def removeAlbum(eventId: Long,albumId: Long) = Action {
    Logger.info("Remove album: "+albumId)
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Not found")
        NotFound
      }
      case Some(event) => {
        event.findAlbum(albumId) match {
          case None => {
            Logger.warn("Not found")
            NotFound
          }
          case Some(album) => {
            event.removeAlbum(album)
            Redirect(routes.EventController.showEditEvent(eventId));
          }
        }
      }
    }
  }


}