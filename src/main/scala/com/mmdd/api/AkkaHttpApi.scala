package com.mmdd.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path, pathPrefix, post, _}
import akka.stream.ActorMaterializer
import com.mmdd.event.{CreateCObject, FindCObject}
import com.mmdd.model.CObject
import com.mmdd.state._

import scala.io.StdIn

object AkkaHttpApi extends App with JsonSupport {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val state = InMemoryCState()
  val writer = StateWriter(state)
  val reader = StateReader(state)
  val stateMngr = StateManager(writer, reader)

  val routes =
    pathPrefix("capi") {
      pathPrefix("object") {
        path(Remaining) { id =>
          get {
            complete(stateMngr.readCommand[CObject](FindCObject(id)))
          }
        } ~
          pathEnd {
            post {
              entity(as[CObject])(mo => {
                stateMngr.updateCommand(CreateCObject(mo))
                complete(HttpEntity(ContentTypes.`application/json`, "ok"))
              })
            }
          }
      }
    }

  val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => {
    // and shutdown when done
    system.terminate()
  })

}
