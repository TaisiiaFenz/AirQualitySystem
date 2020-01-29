package controllers
import javax.inject._
import play.api.db._

import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import javax.inject.Inject
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer

class DatabaseController @Inject()(cc: ControllerComponents, db: Database, executionContext: DatabaseExecutionContext)
                                  (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {


  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out => {
      val actor = system.actorOf(Props[SenderActor])
      ArduinoDataActor.props(out, executionContext, db, actor)
    }
    }
  }

}
import akka.actor._

object ArduinoDataActor {
  def props(out: ActorRef, executionContext: DatabaseExecutionContext, db: Database, actor: ActorRef) = Props(new ArduinoDataActor(out,executionContext,db, actor))
}


class ArduinoDataActor(out: ActorRef, executionContext: DatabaseExecutionContext, db: Database, actor: ActorRef) extends Actor {
  def receive = {
    case msg:  String => {
      if(isAllDigits(msg)){
        actor ! msg
        updateDatabase(msg.toInt)
      }

    }
  }

  def isAllDigits(x: String) = x forall Character.isDigit
  def updateDatabase(msg: Int): Unit = {
    Future {
      val connection = db.getConnection()
      val sql: String = "INSERT INTO sensorTable (value)" + "VALUES(?)"
      val preparedStatement = connection.prepareStatement(sql)
      preparedStatement.setInt(1,msg)
      preparedStatement.executeUpdate()
      //TODO: delete in some time
      println(msg)
      connection.close()
    }(executionContext)
  }

}
class SenderActor extends Actor{
  def receive ={
    case msg: String =>{
      Senders.senders.foreach(actorRefs => actorRefs ! msg)
      println(msg)
    }
  }


}
