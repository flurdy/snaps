package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.Controller
import play.Logger
import models._

object ParticipantController extends Controller with Secured {



  val updateParticipantForm = Form(
    tuple(
      "username" -> nonEmptyText(maxLength = 99),
      "fullname" -> optional(text(maxLength = 99)),
      "email" -> optional(text(maxLength = 99))
    ) verifying("Email address is not valid", fields => fields match {
      case (username, fullname, email ) => {
        email match {
          case Some(emailAddress) => Application.ValidEmailAddress.findFirstIn(emailAddress.trim).isDefined
          case None => true
        }
      }
    })
  )

  val passwordForm = Form(
    tuple(
      "password" -> nonEmptyText(minLength = 4, maxLength = 99),
      "newpassword" -> nonEmptyText(minLength = 4, maxLength = 99),
      "confirm" ->    nonEmptyText(minLength = 4, maxLength = 99)
    ) verifying("Passwords does not match", fields => fields match {
      case ( password, newPassword, confirmPassword) => {
        newPassword.trim == confirmPassword.trim
      }
    })
  )

   def viewParticipant(participantId: Long) = Action { implicit request =>
     Participant.findById(participantId).map { participant =>
       Ok(views.html.participant.viewparticipant(participant,Event.findAllEventsAsParticipantOrOrganiser(participantId),updateParticipantForm.fill((participant.username,participant.fullName,participant.email))))
     }.getOrElse{
       NotFound.flashing("message" -> "Participant not found")
     }
   }


  def updateParticipant(participantId: Long) = withParticipant { participant => implicit request =>
    updateParticipantForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad update participant request:"+errors)
        BadRequest(views.html.participant.viewparticipant(participant,Event.findAllEventsAsParticipantOrOrganiser(participantId),errors)).flashing("messageError"->"Update failed")
      },
      updatedForm => {
        Participant.findById(participantId).map { editParticipant =>
        val updatedParticipant = editParticipant.copy(
          //username = updatedForm._1,
          fullName = updatedForm._2,
          email = updatedForm._3)
          Participant.updateParticipant(updatedParticipant)
          Redirect(routes.ParticipantController.viewParticipant(participantId)).flashing("message" -> "Participant updated")
        }.getOrElse{
          NotFound.flashing("message" -> "Participant not found")
        }
      }
    )
  }

  def confirmDeleteParticipant(participantId: Long) = withParticipant { participant => implicit request =>
    if(participant.participantId == participantId){
      Ok(views.html.participant.deleteparticipant(participant))
    } else {
      Logger.warn("Participant:" + participant.participantId + " can not delete " + participantId)
      Unauthorized.flashing("messageError"->"Can only delete your own account")
    }
  }

  def deleteParticipant(participantId: Long) = withParticipant { participant => implicit request =>
    if(participant.participantId == participantId){
      Logger.info("Participant deleted:" + participantId + " | " + participant.username)
      participant.deleteAccount
      Redirect(routes.Application.index()).withNewSession;
    } else {
      Logger.warn("Participant:" + participant.participantId + " can not delete " + participantId)
      Unauthorized.flashing("messageError"->"Can only delete your own account")
    }
  }

  def changePassword(participantId: Long) = withParticipant { participant => implicit request =>
    passwordForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad change passwords request:"+errors)
        BadRequest(views.html.participant.viewparticipant(participant,Event.findAllEventsAsParticipantOrOrganiser(participantId),updateParticipantForm)).flashing("messageError"->"Password change failed")
      },
      passwords => {
         val newParticipant = participant.copy(password = Option(passwords._2))
        Logger.info("Changing password for " + participantId)
         Participant.updatePassword(newParticipant)
        Redirect(routes.ParticipantController.viewParticipant(participantId)).flashing("message" -> "Password changed")
      }
    )
  }

}
