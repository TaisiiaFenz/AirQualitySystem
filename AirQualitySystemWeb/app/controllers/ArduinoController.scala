package controllers

import javax.inject._

import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import services.{DataDecoder, SensorDataService}

class ArduinoController @Inject()( cc: ControllerComponents, executionContext: DatabaseExecutionContext)
                                 (implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {


  def socket = WebSocket.accept[String, String] { request =>
      ActorFlow.actorRef { out => {
        val actor = system.actorOf(Props[SenderActor])
        ArduinoDataActor.props(out, executionContext, actor)
      }
    }
  }

}
import akka.actor._

object ArduinoDataActor {
  def props( out: ActorRef, executionContext: DatabaseExecutionContext,  actor: ActorRef) = Props(new ArduinoDataActor(out,executionContext, actor))
}


class ArduinoDataActor @Inject() (out: ActorRef, executionContext: DatabaseExecutionContext, actor: ActorRef ) extends Actor {


  def receive = {
    case msg:  String => {
      println(msg)
      actor ! msg
      val data = DataDecoder.decodeData(msg)

        Future{
          SensorDataService.addData(data)
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
