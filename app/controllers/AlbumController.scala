package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.Logger
import models._
import notifiers.EmailNotifier


object AlbumController extends Controller with EventWrappers with Secured with Tracked {

  val albumForm = Form(
    tuple(
      "publisher" -> optional(text(minLength = 3, maxLength = 100)),
      "url" -> nonEmptyText(minLength = 8, maxLength = 150)
    )
  )


  def showAddAlbum(eventId: Long) = isEventParticipant(eventId) { (event,participant) => implicit request =>
    Ok(views.html.albums.add(event,albumForm.fill(Option(participant.username),"")))
  }


  def addAlbum(eventId: Long) = isEventParticipant(eventId) { (event,participant) => implicit request =>
    albumForm.bindFromRequest.fold(
      errors => {
        for(error<-errors.errors){
          Logger.warn("Bad add album request:"+error.key + " " + error.message)
        }
        BadRequest(views.html.albums.add(event,errors)).flashing("errorMessage" -> "Invalid album data")
      },
      submittedAlbumForm => {
        val album = new Album(participant.username,submittedAlbumForm._2)
        EmailNotifier.addAlbumNotification(participant,event,album)
        event.addAlbum(album)
        Redirect(routes.EventController.viewEvent(eventId)).flashing("message" -> "Album added")
      }
    )
  }


  def albumNotFound = {
    Logger.warn("Album not found")
    NotFound.flashing("message" -> "Album not found")
  }


  def updateAlbum(eventId: Long,albumId: Long) = isEventParticipantOrAdmin(eventId) { (event,participant) => implicit request =>
    event.findAlbum(albumId) match {
      case None => albumNotFound
      case Some(album) => {
        albumForm.bindFromRequest.fold(
          errors => {
            Logger.warn("Bad update album request: "+errors)
            val albums = Album.findAlbums(eventId)
            val participants = event.findParticipants
            val requesters = event.findRequests
            BadRequest(views.html.events.edit(event,albums,participants,requesters,EventController.updateForm,EventController.addParticipantToEventForm)
              ).flashing("errorMessage" -> "Invalid album data")
          },
          submittedAlbumForm => {
            val updatedAlbum = album.copy(
                publisher = submittedAlbumForm._1.getOrElse(participant.username),
                url = submittedAlbumForm._2)
            Album.updateAlbum(updatedAlbum)
            Redirect(routes.EventController.showEditEvent(eventId)).flashing("message" -> "Album updated")
          }
        )
      }
    }
  }


  def removeAlbum(eventId: Long, albumId: Long) = isEventParticipantOrAdmin(eventId) { (event,participant) => implicit request =>
    event.findAlbum(albumId) match {
      case None => albumNotFound
      case Some(album) => {
        Logger.info("Remove album: "+albumId)
//        EmailNotifier.removeAlbumNotification(participant,event,album)
        event.removeAlbum(album)
        Redirect(routes.EventController.showEditEvent(eventId)).flashing("message" -> "Album removed")
      }
    }
  }


}