package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.Logger

object EventController extends Controller {

  val searchForm: Form[String] = Form(
    "searchText" -> nonEmptyText(maxLength = 100)
  )

  def search = Action {  implicit request =>
    searchForm.bindFromRequest.fold(
      errors => {
        Logger.warn("Bad request:"+errors)
        BadRequest(views.html.index(errors))
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


}
