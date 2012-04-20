package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import javax.swing.AbstractAction

object Application extends Controller with Secured {

  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> text
    ) verifying("Log in failed. Username does not exist or password is invalid", fields => fields match {
        case (username,password) => Participant.authenticate(username,password).isDefined
      }
//      )(
//      (username: String, password: String) => Participant.findByUsername(username)
//    )(
//      (participant:Option[Participant]) => Some(participant.get.username,"")
    )
  )

  def index = Action {  implicit request =>
    Ok(views.html.index(EventController.searchForm,EventController.createForm))
  }

  def showLogin = Action { implicit request =>
    // Logger.debug("message:"+flash.get("message").getOrElse(""))
    Ok(views.html.login(loginForm))// .flashing("message"->flash.get("message").getOrElse(""))
  }

  def login = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Log in failed: " + errors)
        BadRequest(views.html.login(errors))
      },
      loggedInForm => {
        if(Logger.isDebugEnabled) Logger.debug("Logging in: " + loggedInForm._1)
        Redirect(routes.Application.index()).withSession("username" -> loggedInForm._1)
      }
    )
  }

  def logout = Action {
    if(Logger.isDebugEnabled) Logger.debug("Logging out")
    Redirect(routes.Application.index).withNewSession
  }

  // implicit val participant: Participant = Participant.findByUsername("").get

}


trait Secured {

  def username(request: RequestHeader) = request.session.get(Security.username)

  def onUnauthorised(request: RequestHeader) = Results.Redirect(routes.Application.showLogin)

  def isAuthenticated(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorised) { username =>
      Action(request => f(username)(request))
    }
  }

  def withParticipant(f: Participant => Request[AnyContent] => Result) = isAuthenticated { username => implicit request =>
    Participant.findByUsername(username).map { participant =>
      f(participant)(request)
    }.getOrElse(onUnauthorised(request))
  }

  implicit def currentParticipant(implicit session : Session): Option[Participant] = {
    Participant.findByUsername(session.get(Security.username).getOrElse(""))
  }


}
