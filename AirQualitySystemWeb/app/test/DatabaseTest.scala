package test
import models.User

import collection.mutable.Stack
import org.scalatest.FunSuite
import services.UserService
import javax.inject.Inject
import play.api.inject.guice.GuiceInjectorBuilder
import play.api.inject.bind
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration


class DatabaseTest extends FunSuite {

    test("Database insertion data in Users table") {
      //email: String, password: String, fullName: String, status: String
        val user1 = new User(1, "grmad@gmail.com", "qwe", "Alah", "admin", "akbar")

       // Await.result(UserService.addUser(user1), Duration.Inf)

        val resultStr = Await.result(UserService.getUser("Alah"), Duration.Inf)
        assert(user1.fullName === resultStr.get.fullName)
        assert(user1.websocketIn === resultStr.get.webSocketIn)

    }
//    test("Database insertion data in  table"){
//        val resultStr = Await.result(UserService.getUser("Alah"), Duration.Inf)
//
//    }
//




}
