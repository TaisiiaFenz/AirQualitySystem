package services

import javax.inject.Inject
import models.{Sensor, Sensors}

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
object SensorService {
  val sensors = new Sensors(DbCondigProvider.dbConfProvider)

  def addSensor(data: Sensor): Future[String] = {
    sensors.add(data)
  }

  def deleteSensor(id: Int): Future[Int] = {
    sensors.delete(id)
  }

  def getSensor(id: Int): Future[Option[Sensor]] = {
    sensors.get(id)
  }

  def listAllSensors: Future[Seq[Sensor]] = {
    sensors.listAll
  }
}
