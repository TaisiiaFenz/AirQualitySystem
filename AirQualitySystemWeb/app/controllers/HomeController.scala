package controllers

import javax.inject._
import play.api.db._
import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl._
import play.api.libs.concurrent.CustomExecutionContext
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, db: Database, executionContext: DatabaseExecutionContext)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {


  def index = Action {
    Ok(views.html.index("ffdas"))
  }



  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out, executionContext, db)

    }
  }

  def arduinodata = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out,executionContext,db)

    }
  }

}
import akka.actor._
object Senders{
   var senders: Set[ActorRef] = Set.empty
}
object ConnectionHandler {
  case object Join
  case class ChatMessage(message: String)
}


object MyWebSocketActor {
  def props(out: ActorRef, executionContext: DatabaseExecutionContext, db: Database) = Props(new MyWebSocketActor(out,executionContext,db))

}

class MyWebSocketActor (out: ActorRef, executionContext: DatabaseExecutionContext, db: Database) extends Actor {

  def receive = {
    case msg:  String => {
        Senders.senders+=sender()
     //   println(msg)
        if(isAllDigits(msg)){
          updateSomething(msg.toInt)
        }

    }

  }
  def isAllDigits(x: String) = x forall Character.isDigit
  def updateSomething(msg: Int): Unit = {

    Future {
      // get jdbc connection
      val connection = db.getConnection()

      val sql: String = "INSERT INTO sensorTable (value)" + "VALUES(?)"
      val preparedStatement = connection.prepareStatement(sql)
      preparedStatement.setInt(1,msg)
      preparedStatement.executeUpdate()
      println(msg)
      //val statement = connection.cre
      // do whatever you need with the db connection

      // remember to close the connection
      connection.close()
    }(executionContext)
  }
  //out ! ("fda")
}
//case object SomeMessage
//class TestWebsocketActor extends Actor {
//  def receive  ={
//    case SomeMessage => MyWebSocketActor ! "a"
//  }
//}