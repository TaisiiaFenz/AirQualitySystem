package controllers
import javax.inject._
import models.SensorDataForm
import play.api.Logging
import play.api.mvc._
import services.SensorDataService

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class ShowDataController @Inject()(executionContext: DatabaseExecutionContext, cc: ControllerComponents) extends AbstractController(cc) with Logging{
  def showData = Action.async { implicit request: Request[AnyContent] =>
    SensorDataService.listAllData
    SensorDataService.listAllData map { data =>
      Ok(views.html.data(SensorDataForm.form, data))
    }

  }
}
