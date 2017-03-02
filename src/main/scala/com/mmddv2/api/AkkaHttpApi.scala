package com.mmddv2.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path, pathPrefix, post, _}
import akka.stream.ActorMaterializer
import com.mmddv2.context.CContext
import com.mmddv2.event.{CreateCObject, FindCObject}
import com.mmddv2.model.CObject
import com.mmddv2.state.InMemoryCState

import scala.io.StdIn

object AkkaHttpApi extends App with JsonSupport {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val ctx = CContext("core", InMemoryCState())
  ctx.init()

  val routes =
    pathPrefix("capi") {
      pathPrefix("object") {
        path(Remaining) { id =>
          get {
            complete(ctx.readCommand[CObject](FindCObject(id)))
          }
        } ~
          pathEnd {
            post {
              entity(as[CObject])(mo => {
                ctx.updateCommand(CreateCObject(mo))
                complete("ok")
              })
            }
          }
      } ~
        path("field" / Remaining) { id =>
          get {
            complete("ok")
          } ~
            post {
              complete("ok")
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
    ctx.destroy()
    system.terminate()
  })

}
