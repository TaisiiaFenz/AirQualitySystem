package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.{Inject, Named}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

case class Sensor(id: Int, ownerId: Int, name: String, longitude: String, latitude: String)


class SensorTableModel(tag: Tag) extends Table[Sensor](tag, "SENSORS") {

  def id = column[Int]("id", O.PrimaryKey)
  def ownerId = column[Int]("owner")
  def name = column[String]("name")
  def longitude = column[String]("longitude")
  def latitude = column[String]("latitude")
//TODO: add owner foreign key
  override def * = (id, ownerId, name, longitude,latitude) <> (Sensor.tupled, Sensor.unapply)

}

object SensorForm {

  val form = Form(
    mapping(
      "id" -> number,
      "owner" -> number,
      "name" -> text,
      "longitude" -> text,
      "latitude"-> text
    )(Sensor.apply)(Sensor.unapply)
  )
}

class Sensors @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {



  val sensorsData = TableQuery[SensorTableModel]

  def add(sensor: Sensor): Future[String] = {
    dbConfig.db.run(sensorsData += sensor).map(_ => "Data successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Int): Future[Int] = {
    dbConfig.db.run(sensorsData.filter(_.id === id).delete)
  }

  def get(id: Int): Future[Option[Sensor]] = {
    dbConfig.db.run(sensorsData.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Sensor]] = {
    dbConfig.db.run(sensorsData.result)
  }
}

