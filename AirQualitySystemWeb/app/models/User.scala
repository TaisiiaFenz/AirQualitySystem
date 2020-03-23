package models

import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import javax.inject.{Inject, Singleton}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future


//TODO: In future change type String on type enum in field status if it is possible
case class User (id: Option[Int] = None, email: String, password: String, fullName: String, status: String, userName: String){
    val websocketIn: String = s"ws://localhost:9000/${userName}in"
    val webSocketOut: String = s"ws://localhost:9000/${userName}out"
}

class UserTableModel(tag: Tag) extends Table[User](tag, "USERS") {


  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email")
  def password = column[String]("password")
  def status = column[String]("status")
  def fullName = column[String]("fullName")
  def userName = column[String]("userName")

  override def * =
    (id.?, email, password, fullName, status, userName) <>(User.tupled, User.unapply)
}


object UserForm {

  val form = Form(
    mapping(
      "id" -> optional(number),
      "email" -> text,
      "password" -> text,
      "fullName" -> text,
      "status" -> text,
      "userName" -> text

    )(User.apply)(User.unapply)
  )
}
@Singleton
class Users @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val users = TableQuery[UserTableModel]

  def add(data: User): Future[String] = {
    dbConfig.db.run(users += data).map(_ => "Data successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(fullName: String): Future[Int] = {
    dbConfig.db.run(users.filter(_.fullName === fullName ).delete)
  }

  def get(fullName: String): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.fullName === fullName).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }
}