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



  def viewEvent(eventId: Long) = Action { implicit request =>
    Event.findEventWithAlbums(eventId) match {
      case None => {
        Logger.warn("Event not found on view")
        NotFound
      }
      case Some(event) => {
        if(event.public || !event.organiser.isDefined){
          Ok(views.html.events.view(event))
        } else {
          currentParticipant match {
            case None => {
              Logger.warn("Event requires participant")
              Unauthorized(views.html.login(Application.loginForm)).flashing("message"->"Event private, please log in")
            }
            case Some(participant) => {
              if(event.isParticipant(participant) ) {
                Ok(views.html.events.view(event))
              } else {
                Logger.warn("Current participant is not a participant: " + participant.username )
                Logger.warn("Event organiser: " + event.organiser.get.username )
                //                Unauthorized(views.html.login(Application.loginForm)).flashing("message"->"Event private, and you do not have access to it")
               Unauthorized(views.html.events.unauthorised(event)).flashing("message"->"Event private, and you do not have access to it")
              }
            }
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
         if( event.isOrganiser(currentParticipant.get) ){
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
         } else {
             Logger.warn("Not an organiser:" + currentParticipant.get + " | " + event.organiserId)
           Unauthorized.flashing("message" -> "Participant is not an organiser")
        }
      }
    }
  }


  def updateEvent(eventId: Long) = withParticipant { participant => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found on update event")
        NotFound
      }
      case Some(event) =>  {
        updateForm.bindFromRequest.fold(
          errors => {
            Logger.warn("Bad update event request:"+errors)
            val albums = Album.findAlbums(event.eventId)
            val participants = event.findParticipants
            BadRequest(views.html.events.edit(event,albums,participants,updateForm))
          },
          updatedForm => {
            if( event.isOrganiser(participant) ) {
              val updatedEvent = event.copy(
                eventName = updatedForm._1,
                //organiser = Option(updatedForm._2),
                eventDate = Option(updatedForm._3),
                description = Option(updatedForm._4) ,
                public = updatedForm._5 )
              Logger.warn("P:"+updatedEvent.public)
              Event.updateEvent(updatedEvent)
              Redirect(routes.EventController.viewEvent(event.eventId));
            } else {
                 Logger.warn("Not an organiser")
              Unauthorized.flashing("message" -> "Participant is not an organiser")
             }
          }
        )
      }
    }
  }


  def showDeleteEvent(eventId: Long) = withParticipant { participant => implicit request =>
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("No event found on show delete event")
        NotFound
      }
      case Some(event) => {
        if( event.isOrganiser(participant) ) {
          Ok(views.html.events.delete(event))
       } else {
          Logger.warn("Not an organiser")
          Unauthorized.flashing("message" -> "Participant is not an organiser")
        }
      }
    }
  }


  def deleteEvent(eventId: Long) =  withParticipant { participant => implicit request =>
    Logger.info("Deleting event: " + eventId)
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("No event found on delete event")
        NotFound
      }
      case Some(event) => {
        if( event.isOrganiser(participant) ) {
          Event.deleteEvent(eventId)
          Redirect(routes.Application.index()).flashing("message" -> "Event deleted");
        } else {
            Logger.warn("Not an organiser")
          Unauthorized
        }
      }
    }
  }


  def removeParticipant(eventId: Long, participantId: Long) = withParticipant { participant => implicit request =>
    Logger.info("Remove participant: "+eventId)
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found for remove participant")
        NotFound.flashing("message" -> "Event not found")
      }
      case Some(event) => {
        if( event.isOrganiser(participant) ) {
          Participant.findById(participantId) match {
            case None => {
              Logger.warn("Participant not found for remove participant")
              NotFound.flashing("error" -> "Participant not found")
            }
            case Some(participantToBeRemoved) => {
              event.removeParticipant(participantToBeRemoved)
              Redirect(routes.EventController.showEditEvent(eventId)).flashing("message" -> "Participant removed");
            }
          }
        } else {
          Logger.warn("Not an organiser")
          Unauthorized.flashing("error" -> "Participant is not an organiser")
        }
      }
    }
  }

  def addParticipant(eventId: Long) = withParticipant { participant => implicit request =>
    Logger.info("Add participant: "+eventId)
    Event.findEvent(eventId) match {
      case None => {
        Logger.warn("Event not found for add participant")
        NotFound.flashing("message" -> "Event not found")
      }
      case Some(event) => {
        if( event.isOrganiser(participant) ) {
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
                case None => {
                  Logger.info("Participant not found for add participant")
                  NotFound.flashing("message" -> "Participant not found")
                  Redirect(routes.EventController.showEditEvent(eventId)).flashing("errorMessage" -> "Participant not found");
                }
                case Some(participantToBeAdded) => {
                  event.addParticipant(participantToBeAdded)
                  Redirect(routes.EventController.showEditEvent(eventId)).flashing("message" -> "Participant added");
                }
              }
            }
          )
        } else {
          Logger.warn("Not an organiser")
          Unauthorized.flashing("error" -> "Participant is not an organiser")
        }
      }
    }
  }

}