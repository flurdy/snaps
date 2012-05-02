package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.Logger
import models._


object AlbumController extends Controller with Secured {

  val albumForm = Form(
    tuple(
      "publisher" -> nonEmptyText(minLength = 3, maxLength = 100),
      "url" -> nonEmptyText(minLength = 8, maxLength = 150)
    )
  )


  def showAddAlbum(eventId: Long) = withParticipant { participant => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found on show album")
        NotFound
      }
      case Some(event) => Ok(views.html.albums.add(event,albumForm.fill(participant.username,"")))
    }
  }


  def addAlbum(eventId: Long) = isAuthenticated { username => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found on add album ")
        NotFound
      }
      case Some(event) => {
        albumForm.bindFromRequest.fold(
          errors => {
            for(error<-errors.errors){
              Logger.warn("Bad add album request:"+error.key + " " + error.message)
            }
            BadRequest(views.html.albums.add(event,errors))
          },
          submittedAlbumForm => {
            val album = new Album(submittedAlbumForm._1,submittedAlbumForm._2)
            event.addAlbum(album)
            Redirect(routes.EventController.viewEvent(eventId));
          }
        )
      }
    }
  }



  def updateAlbum(eventId: Long,albumId: Long) = isAuthenticated { username => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found on update album")
        NotFound
      }
      case Some(event) => {
        event.findAlbum(albumId) match {
          case None => {
            Logger.warn("Album not found on update album")
            NotFound
          }
        case Some(album) => {
          albumForm.bindFromRequest.fold(
            errors => {
              Logger.warn("Bad update album request: "+errors)
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
        Logger.warn("Event not found for remove album")
        NotFound
      }
      case Some(event) => {
        event.findAlbum(albumId) match {
          case None => {
            Logger.warn("Album not found for remove album")
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