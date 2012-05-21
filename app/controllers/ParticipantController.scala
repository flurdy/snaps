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
//      "password" -> nonEmptyText(minLength = 4, maxLength = 99),
//      "confirm" -> nonEmptyText(minLength = 4, maxLength = 99)
//    ) verifying("Passwords does not match", fields => fields match {
//      case (username, fullname, email, password, confirmPassword) => {
//        password.trim == confirmPassword.trim
//      }
//    ) verifying("Username is already taken", fields => fields match {
//      case (username, fullname, email, password, confirmPassword) => {
//        !Participant.findByUsername(username.trim).isDefined
//      }
    ) verifying("Email address is not valid", fields => fields match {
      case (username, fullname, email ) => {
        email match {
          case Some(emailAddress) => Application.ValidEmailAddress.findFirstIn(emailAddress.trim).isDefined
          case None => true
        }
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
          Redirect(routes.ParticipantController.viewParticipant(participantId));
        }.getOrElse{
          NotFound.flashing("message" -> "Participant not found")
        }
      }
    )
  }

  def deleteParticipant(participantId: Long) = TODO

}
