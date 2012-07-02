package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.mvc.Controller
import play.Logger
import models._
import notifiers.EmailNotifier

object ParticipantController extends Controller with Secured with Tracked {

  val resetForm = Form(
      "username" -> nonEmptyText(maxLength = 99)
  )
  //verifying("Participant not found", fields => fields match {
  //    case (username,username2) => Participant.findByUsername(username.trim).isDefined
  //)}


  val updateParticipantForm = Form(
    tuple(
      "username" -> nonEmptyText(maxLength = 99),
      "fullname" -> optional(text(maxLength = 99)),
      "email" -> text(maxLength = 99)
    ) verifying("Email address is not valid", fields => fields match {
      case (username, fullname, email ) => Participant.ValidEmailAddress.findFirstIn(email.trim).isDefined
      }
    )
  )

  val passwordForm = Form(
    tuple(
      "password" -> nonEmptyText(minLength = 4, maxLength = 99),
      "newpassword" -> nonEmptyText(minLength = 4, maxLength = 99),
      "confirm" ->    nonEmptyText(minLength = 4, maxLength = 99)
    ) verifying("Passwords do not match", fields => fields match {
      case ( password, newPassword, confirmPassword) => {
        newPassword.trim == confirmPassword.trim
      }
      //    }) verifying("Orignal password incorrect", fields => fields match {
      //      case (password,_,_) => Participant.authenticate(username, password).isDefined
    })
  )


  def viewParticipant(participantId: Long) = Action { implicit request =>
     Participant.findById(participantId).map { participant =>
       Ok(views.html.participant.viewparticipant(participant,Event.findAllEventsAsParticipantOrOrganiser(participantId),updateParticipantForm.fill((participant.username,participant.fullName,participant.email)),passwordForm))
     }.getOrElse{
       NotFound.flashing("message" -> "Participant not found")
     }
   }


  def updateParticipant(participantId: Long) = withParticipant { participant => implicit request =>
    updateParticipantForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad update participant request:"+errors)
        BadRequest(views.html.participant.viewparticipant(participant,Event.findAllEventsAsParticipantOrOrganiser(participantId),errors,passwordForm)).flashing("messageError"->"Update failed")
      },
      updatedForm => {
        Participant.findById(participantId).map { editParticipant =>
          if (participant.isAdmin || participant == editParticipant){
            val updatedParticipant = editParticipant.copy(
            //username = updatedForm._1,
            fullName = updatedForm._2,
            email = updatedForm._3)
            Participant.updateParticipant(updatedParticipant)
            Redirect(routes.ParticipantController.viewParticipant(participantId)).flashing("message" -> "Participant updated")
          } else {
            Logger.warn("Participant is not an admin and trying to edit someone else")
            Unauthorized.flashing("message" -> "Not allowed to edit participant")
          }
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
      Logger.info("Participant deleting:" + participantId + " | " + participant.username)
      EmailNotifier.deleteParticipantAlert(participant)
      Participant.deleteAccount(participantId)
      Logger.warn("Participant deleted:" + participantId + " | " + participant.username)
      Redirect(routes.Application.index()).withNewSession;
    } else {
      Logger.warn("Participant:" + participant.participantId + " can not delete " + participantId)
      Unauthorized.flashing("messageError"->"Can only delete your own account")
    }
  }

  def changePassword(participantId: Long) = withParticipant { participant => implicit request =>
    passwordForm.bindFromRequest.fold(
      errors => {
        for(error <- errors.errors){
          Logger.warn("Bad change passwords request:"+error.key + ":"+error.message)
        }
        BadRequest(views.html.participant.viewparticipant(participant,Event.findAllEventsAsParticipantOrOrganiser(participantId),updateParticipantForm,errors)).flashing("errorMessage"->"Password change failed")
      },
      passwords => {
        Participant.findById(participantId).map { participantToUpdate =>
          if(participant.isAdmin){
            changeAndStoreNewPassword(participantToUpdate,passwords._2)
          } else {
            Participant.authenticate(participantToUpdate.username, passwords._1).map { _ =>
              changeAndStoreNewPassword(participantToUpdate,passwords._2)
            }.getOrElse{
              Redirect(routes.ParticipantController.viewParticipant(participantId)).flashing("errorMessage" -> "Password incorrect")
            }
          }
        }.getOrElse{
          Redirect(routes.ParticipantController.viewParticipant(participantId)).flashing("errorMessage" -> "Participant not found")
        }
      }
    )
  }

  private def changeAndStoreNewPassword(participant:Participant,newPassword:String) = {
    Logger.info("Changing password for " + participant.participantId)
    val newParticipant = participant.copy(password = Option(newPassword))
    Participant.updatePassword(newParticipant)
    Redirect(routes.ParticipantController.viewParticipant(participant.participantId)).flashing("message" -> "Password changed")
  }



  def showResetPassword = Action { implicit request =>
    Ok(views.html.participant.resetpassword())
  }



  def resetPassword =  Action { implicit request =>
    resetForm.bindFromRequest.fold(
      errors => {
        for(error <- errors.errors){
          Logger.warn("Bad change passwords request:"+error.key + ":"+error.message)
        }
        BadRequest(views.html.participant.resetpassword()).flashing("errorMessage"->"Password reset failed")
      },
      username => {
        Participant.findByUsername(username).map { participant =>
          Logger.info("Password reset requested for: " + participant.participantId)
          val newPassword = Participant.resetPassword(participant.participantId)
          EmailNotifier.sendNewPassword(participant,newPassword)
        }
        Redirect(routes.Application.index()).flashing("message" -> "Password reset and sent by email")
      }
    )
  }


  def verify(participantId:Long, verificationHash:String) = Action {
    Participant.findById(participantId).map { participant =>
      Logger.info("Verifying participant: " + participantId)
      if( participant.matchesVerificationHash(verificationHash) ) {
        participant.markEmailAsVerified(verificationHash)
        Redirect(routes.Application.showLogin()).flashing("message" -> "Email verified. Please log in")
      } else {
        Logger.warn("Email verification failed")
        NotFound.flashing("message" -> "Participant or verification not found")
      }
    }.getOrElse{
      NotFound.flashing("message" -> "Participant or verification not found")
    }
  }

}
