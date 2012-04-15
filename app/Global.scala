
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

object Global extends GlobalSettings  {

  override def onHandlerNotFound(request: RequestHeader) = {
    if (request.path != "/favicon.ico" && Logger.isInfoEnabled)
      Logger.info("Not found: " + request.path)
    NotFound(views.html.errors.notfound())
  }

  //override def onBadRequest(request: RequestHeader, error: String) = {
  //  BadRequest("Bad Request: " + error)
  //}

  override def onError(request: RequestHeader, ex: Throwable) = {
    Logger.error("Server error: " + request.path,ex)
    InternalServerError(views.html.errors.servererror())
  }


}
