package services


//
import javax.inject.{Inject, Named, Singleton}
import models.{SensorData, Sensors, SensorsData}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.Future
import ExecutionContext.Implicits.global

object SensorDataService {

//  @Inject
//  var sensorsData: SensorsData = _
  val sensorsData = new SensorsData(DbCondigProvider.dbConfProvider)
  def addData(data: SensorData): Future[String] = {
    sensorsData.add(data)
  }

  def deleteData(id: Int): Future[Int] = {
    sensorsData.delete(id)
  }

  def getData(id: Int): Future[Option[SensorData]] = {
    sensorsData.get(id)
  }

  def listAllData: Future[Seq[SensorData]] = {
    sensorsData.listAll
  }
}
