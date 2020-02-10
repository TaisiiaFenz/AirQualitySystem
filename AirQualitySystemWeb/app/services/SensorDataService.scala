package services


//
import javax.inject.{Named, Singleton, Inject}
import models.{SensorData, SensorsData}

import scala.concurrent.Future

@Named
class SensorDataService {

  @Inject
  var sensorsData: SensorsData = _

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
