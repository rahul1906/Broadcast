package com.hm.routes

import java.sql.ResultSet

import com.hm.config.Configuration
import spray.routing.HttpService
import com.hm.connector.MysqlClient
import spray.http.MediaTypes.`text/html`

import scala.collection.mutable.ArrayBuffer
import com.hm.connector.MysqlClient

import scalaj.http.{Http, HttpRequest, HttpResponse}



/**
  * Created by rahul on 13/3/17.
  */
trait Routes extends HttpService with Configuration {

 // val buf = ArrayBuffer()
  val buf = scala.collection.mutable.ArrayBuffer.empty[Int]

  val route = {
    path("") {
      respondWithMediaType(`text/html`) {
       // val a : HttpRequest = Http("http://localhost:8081/badd?n=1")
        //println(a)
        complete("welcome")
      }
    }~path("add"){
      parameter("n") { (n) =>
        buf += n.toInt
        val rs = getInstances
        rs.foreach(i => {
          println(i._1+"  "+i._2)
          val request: HttpRequest = Http ("http://" + i._1 + ":" + i._2+ "/badd?n="+n.toInt)
          println(request.asString)
        })
        complete("elements are"+buf)
      }
      }~path("badd"){
        parameter("n"){ (n)=>
          buf += n.toInt
          complete("b elements are"+buf)
        }

      }~path("del"){
      parameter("n") { (n) =>
        buf -= n.toInt
        val rs = getInstances()
        rs.foreach(i => {
          Http ("http://" + i._1 + ":" + i._2+ "/bdel?n="+n.toInt).asString
         //println(request.asString.body+"broad")
        })

        complete("elements are"+buf)
      }
    }~path("bdel"){
      parameter("n"){ (n)=>
        buf -= n.toInt
       complete("b elements are"+buf)
      }

    }~path("list"){
      complete("elements are "+buf)
    }


  }



  def getInstances():ArrayBuffer[(String,Int)]={
    val rs = MysqlClient.getResultSet("select * from liveinstances where interface <> '"+serviceHost+"' or port <>"+servicePort)
    var a =new ArrayBuffer[(String,Int)]
    var buffer=new collection.mutable.ArrayBuffer[(String,Int)]
    while (rs.next()){
      buffer.append((rs.getString("interface"),rs.getInt("port")))
    }
    rs.close()
    buffer
  }
}
