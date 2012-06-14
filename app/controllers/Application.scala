package controllers

import play.api._
import mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import notifiers._

object Application extends Controller with Secured {


  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText(maxLength = 99),
      "password" -> nonEmptyText(maxLength = 99)
    ) verifying("Log in failed. Username does not exist or password is invalid", fields => fields match {
      case (username, password) => Participant.authenticate(username, password).isDefined
    })
  )

  val registerForm = Form(
    tuple(
      "username" -> nonEmptyText(maxLength = 99),
      "fullname" -> optional(text(maxLength = 99)),
      "email" -> nonEmptyText(maxLength = 99),
      "password" -> nonEmptyText(minLength = 4, maxLength = 99),
      "confirm" -> nonEmptyText(minLength = 4, maxLength = 99)
    ) verifying("Passwords do not match", fields => fields match {
      case (username, fullname, email, password, confirmPassword) => {
        password.trim == confirmPassword.trim
      }
    }) verifying("Username is already taken", fields => fields match {
      case (username, fullname, email, password, confirmPassword) => {
        !Participant.findByUsername(username.trim).isDefined
      }
    }) verifying("Email address is not valid", fields => fields match {
      case (username, fullname, email, password, confirmPassword) => Participant.ValidEmailAddress.findFirstIn(email.trim).isDefined
    })
  )

  val initialRegisterForm = Form(
    tuple(
      "username" -> nonEmptyText(maxLength = 99),
      "email" -> nonEmptyText(maxLength = 99)
    ) verifying("Username is already taken", fields => fields match {
      case (username, email) => {
        !Participant.findByUsername(username.trim).isDefined
      }
    }) verifying("Email address is not valid", fields => fields match {
      case (username, email) => Participant.ValidEmailAddress.findFirstIn(email.trim).isDefined
    })
  )

  def index = Action { implicit request =>
    Ok(views.html.index(EventController.searchForm, EventController.createForm, registerForm))
  }

  def showLogin = Action { implicit request =>
      flash.get("eventId").map { eventId =>
        Ok(views.html.login(loginForm))
          .withSession( session + ("eventId" -> eventId))
      }.getOrElse(Ok(views.html.login(loginForm)))
  }

  def login = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        errors => {
          Logger.warn("Log in failed: " + errors)
          BadRequest(views.html.login(errors))
        },
        loggedInForm => {
          if (Logger.isDebugEnabled) Logger.debug("Logging in: " + loggedInForm._1)
          val eventId : Option[String] = session.get("eventId")
          Redirect(
            eventId.map { eventId =>
              routes.EventController.viewEvent(eventId.toLong)
            }.getOrElse(routes.Application.index())
          ).withSession("username" -> loggedInForm._1).flashing("message" -> "Logged in")
        }
      )
  }

  def logout = Action {
    if (Logger.isDebugEnabled) Logger.debug("Logging out")
    Redirect(routes.Application.index).withNewSession.flashing("message" -> "Logged out")
  }


  def showRegister = Action {
    implicit request =>
      Ok(views.html.register(registerForm))
  }

  def firstRegisterStep = Action {
    implicit request =>
      initialRegisterForm.bindFromRequest.fold(
        errors => {
          Logger.warn("Registration failed: " + errors)
          BadRequest(views.html.register(registerForm.fill(errors.get._1,None,errors.get._2,"","")))
        },
        registeredForm => {
          Ok(views.html.register(registerForm.fill(registeredForm._1,None,registeredForm._2,"",""))).flashing("message"->"Please fill in your name and choose a password")
        }
      )
  }


  def register = Action {
    implicit request =>
      registerForm.bindFromRequest.fold(
        errors => {
          Logger.warn("Registration failed: " + errors)
          BadRequest(views.html.register(errors))
        },
        registeredForm => {
          if (Logger.isDebugEnabled) Logger.debug("Registering: " + registeredForm._1)
          val participant = Participant(0, registeredForm._1, registeredForm._2, registeredForm._3, Some(registeredForm._4))
          val registeredParticipant = Participant.save(participant)
          registeredParticipant.storeAndSendEmailVerification
          EmailNotifier.registrationAlert(registeredParticipant)
          Redirect(routes.Application.index()).flashing("message" ->
            """
              Registered. Please verify email sendt to you to activate participation.
            """.stripMargin)
        }
      )
  }



}



trait Secured {

  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthenticated(request: RequestHeader)= {
    request.flash.get("eventId") match{
      case Some(eventId) => {
        Results.Redirect(routes.Application.showLogin).withSession(request.session+("eventId" -> eventId)).flashing("eventId" -> eventId)
      }
      case None => {
        Results.Redirect(routes.Application.showLogin)
      }
    }
  }

  def onUnauthorised(request: RequestHeader, event: Event)(implicit session: Session, flash: Flash) = {
    Results.Unauthorized(
        views.html.events.unauthorised(event)(currentParticipant,flash)
      ).flashing("message"->"Event private, and you do not have access to it")
  }

  def isAuthenticated(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthenticated) {
      username =>
        Action(request => f(username)(request))
    }
  }

  def withParticipant(f: Participant => Request[AnyContent] => Result) = isAuthenticated {
    username => implicit request =>
      Participant.findByUsername(username).map {
        participant => f(participant)(request)
      }.getOrElse(onUnauthenticated(request))
  }

  implicit def currentParticipant(implicit session: Session): Option[Participant] = {
    Participant.findByUsername(session.get(Security.username).getOrElse(""))
  }

}
