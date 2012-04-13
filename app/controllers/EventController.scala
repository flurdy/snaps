package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.Logger

object EventController extends Controller {

  val searchForm: Form[String] = Form(
    "searchText" -> text(maxLength = 100)
  )
  val createForm: Form[String] = Form(
    "eventName" -> nonEmptyText(maxLength = 100)
  )

  val updateForm: Form[String] = Form(
    "eventName" -> nonEmptyText(maxLength = 100)
  )

  def search = Action {  implicit request =>
    searchForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      searchText => {
        Logger.info("Find event")
        Ok(views.html.events.list(searchText))
      }
    )
  }

  def viewEvent(eventId: Long) = Action {
    Ok(views.html.events.view())
  }


  def createEvent = Action { implicit request =>
    createForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      eventName => {
        Logger.info("Create event")
        Redirect(routes.EventController.showEditEvent(1));
      }
    )
  }

  def showEditEvent(eventId: Long) = Action {
    Ok(views.html.events.edit())
  }



  def updateEvent(eventId: Long) = Action { implicit request =>
    updateForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors,createForm))
      },
      eventName => {
        Logger.info("Update event")
        Redirect(routes.EventController.viewEvent(1));
      }
    )
  }

}
