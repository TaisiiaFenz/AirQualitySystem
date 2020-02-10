package controllers
import javax.inject._
import play.api.db._
import models.SensorData

import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import controllers.DatabaseExecutionContext
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import services.SensorDataService
import org.springframework.beans.factory.annotation.Configurable
//import org.springframework.context.annotation.{ Bean, AnnotationConfigApplicationContext, Configuration, Scope }
//import org.springframework.beans.factory.annotation.Configurable
class ArduinoController @Inject()(sensorDataServiceObject: SensorDataService, cc: ControllerComponents, executionContext: DatabaseExecutionContext)
                                 (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {


  def socket = WebSocket.accept[String, String] { request =>
      ActorFlow.actorRef { out => {
        val actor = system.actorOf(Props[SenderActor])
        ArduinoDataActor.props(sensorDataServiceObject, out, executionContext, actor)
      }
    }
  }

}
import akka.actor._

object ArduinoDataActor {
  def props( sensorDataServiceObject: SensorDataService, out: ActorRef, executionContext: DatabaseExecutionContext,  actor: ActorRef) = Props(new ArduinoDataActor(sensorDataServiceObject, out,executionContext, actor))
}


class ArduinoDataActor @Inject() (sensorDataServiceObject: SensorDataService, out: ActorRef, executionContext: DatabaseExecutionContext, actor: ActorRef ) extends Actor {

  def receive = {
    case msg:  String => {
      println(msg)
      actor ! msg
      val data = DecoderController.decodeData(msg)

        Future{
          sensorDataServiceObject.addData(data)
        }(executionContext)



    }
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
