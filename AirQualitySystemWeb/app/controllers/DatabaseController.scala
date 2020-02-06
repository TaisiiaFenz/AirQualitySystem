package controllers
import javax.inject._
import play.api.db._
import models.SensorData
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
      println(msg)
      val data = DecoderController.decodeData(msg)
      updateDatabase(data)
      actor ! msg


    }
  }

  def isAllDigits(x: String) = x forall Character.isDigit
  def updateDatabase(msg: SensorData): Unit = {
    Future {
      val connection = db.getConnection()
      val sql: String = "INSERT INTO sensorData (id, value)" + "VALUES(?,?)"
      val preparedStatement = connection.prepareStatement(sql)
      preparedStatement.setInt(1, msg.id)
      preparedStatement.setInt(2, msg.value)
      preparedStatement.executeUpdate()
      //TODO: delete in some cases
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
