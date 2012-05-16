package controllers

import play.api._
import mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import javax.swing.AbstractAction

object Application extends Controller with Secured {

  val ValidEmailAddress = """^[0-9a-zA-Z]([-\.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9}$""".r

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
      "email" -> optional(text(maxLength = 99)),
      "password" -> nonEmptyText(minLength = 4, maxLength = 99),
      "confirm" -> nonEmptyText(minLength = 4, maxLength = 99)
    ) verifying("Passwords does not match", fields => fields match {
      case (username, fullname, email, password, confirmPassword) => {
        password.trim == confirmPassword.trim
      }
    }) verifying("Username is already taken", fields => fields match {
      case (username, fullname, email, password, confirmPassword) => {
        !Participant.findByUsername(username.trim).isDefined
      }
    }) verifying("Email address is not valid", fields => fields match {
      case (username, fullname, email, password, confirmPassword) => {
        email match {
          case Some(emailAddress) => ValidEmailAddress.findFirstIn(emailAddress.trim).isDefined
          case None => true
        }
      }
    })
  )

  def index = Action {
    implicit request =>
      Ok(views.html.index(EventController.searchForm, EventController.createForm, registerForm))
  }

  def showLogin = Action {
    implicit request =>
      Ok(views.html.login(loginForm))
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
          Redirect(routes.Application.index()).withSession("username" -> loggedInForm._1).flashing("message" -> "Logged in")
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
          Participant.save(participant)
          Redirect(routes.Application.index()).withSession("username" -> participant.username).flashing("message" -> "Registered. Welcome")
        }
      )
  }

}



trait Secured {

  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthenticated(request: RequestHeader) = Results.Redirect(routes.Application.showLogin)

  def onUnauthorised(request: RequestHeader, event: Event)(implicit session: Session) = {
    Results.Unauthorized(
        views.html.events.unauthorised(event)(currentParticipant)
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
