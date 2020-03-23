package services


import models.{User, Users}

import scala.concurrent.Future

import scala.concurrent._
import ExecutionContext.Implicits.global

object UserService {

  val users: Users = new Users(DbCondigProvider.dbConfProvider)
  def addUser(user: User): Future[String] = {
    users.add(user)
  }

  def deleteUser(fullName: String): Future[Int] = {
    users.delete(fullName)
  }

  def getUser(fullName: String): Future[Option[User]] = {
    users.get(fullName)
  }

  def listAllUsers: Future[Seq[User]] = {
    users.listAll
  }
}
