package controllers

import akka.actor.ActorSystem
import play.libs.concurrent.CustomExecutionContext

import javax.inject.Inject



class DatabaseExecutionContext @Inject()(system: ActorSystem)
  extends CustomExecutionContext(system, "database.dispatcher")
