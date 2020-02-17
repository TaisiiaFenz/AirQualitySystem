package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.{Inject}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

case class SensorData(id: Int, value: Int, time: String )

class SensorDataTableModel(tag: Tag) extends Table[SensorData](tag, "SENSORS_DATA") {
  @Inject
  var sensors: Sensors = _

  def id = column[Int]("id")
  def value = column[Int]("value")
  def time = column[String]("time")
  def sensorId = foreignKey("fk_sensorId", id, sensors.sensorsData)(_.id)
  override def * =
    (id, value, time) <>(SensorData.tupled, SensorData.unapply)
}


object SensorDataForm {

  val form = Form(
    mapping(
      "id" -> number,
      "value" -> number,
      "time" -> text
    )(SensorData.apply)(SensorData.unapply)
  )
}

class SensorsData @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {



  val sensorsData = TableQuery[SensorDataTableModel]

  def add(data: SensorData): Future[String] = {
    dbConfig.db.run(sensorsData += data).map(_ => "Data successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Int): Future[Int] = {
    dbConfig.db.run(sensorsData.filter(_.id === id).delete)
  }

  def get(id: Int): Future[Option[SensorData]] = {
    dbConfig.db.run(sensorsData.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[SensorData]] = {
    dbConfig.db.run(sensorsData.result)
  }
}