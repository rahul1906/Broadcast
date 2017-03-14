package com.hm.app

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import com.hm.ServerServiceActor
import com.hm.config.Configuration
import com.hm.connector.MysqlClient
import spray.can.Http

/**
  * Created by rahul on 13/3/17.
  */
object App extends App with Configuration{

  implicit val system = ActorSystem("on-spray-can")

  val service = system.actorOf(Props[ServerServiceActor], "Broadcast")
  implicit val timeout = Timeout(5)
  IO(Http) ! Http.Bind(service, serviceHost, servicePort)
  MysqlClient.insert("liveinstances", Map("interface" -> serviceHost, "port" -> servicePort))


}
