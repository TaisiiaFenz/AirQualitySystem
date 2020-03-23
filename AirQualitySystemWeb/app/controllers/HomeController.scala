package controllers

import javax.inject._

import play.api.mvc._
import play.api.libs.streams.ActorFlow
import javax.inject.Inject
import akka.actor.ActorSystem
import akka.stream.Materializer

import akka.actor._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {


  def index = Action {
    Ok(views.html.index())
  }
  def socket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>

      MyWebSocketActor.props(out)

    }
  }
}

object Senders{
   var senders: Set[ActorRef] = Set.empty
}
// TODO: Implement connection handler
object ConnectionHandler {
  case object Join
  case class ChatMessage(message: String)
}


object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))

}

class MyWebSocketActor (out: ActorRef) extends Actor {

  def receive = {
    case _:  String => {
        Senders.senders+=out
    }

  }

  override def postStop(){
      Senders.senders -= out
  }

}

