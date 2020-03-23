package services

import play.api.Mode
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder

object DbCondigProvider {
    val appBuilder: GuiceApplicationBuilder = new GuiceApplicationBuilder().in(Mode.Test)
    val injector: Injector = appBuilder.injector()
    val dbConfProvider: DatabaseConfigProvider = injector.instanceOf[DatabaseConfigProvider]
}
