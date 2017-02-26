package com.mmdd.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path, pathPrefix, post}
import akka.stream.ActorMaterializer
import com.mmdd.context.MContext
import com.mmdd.event.{CreateMField, CreateMObject, FindMField, FindMObject}
import com.mmdd.model.{MField, MObject}
import com.mmdd.state.InMemoryMState
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object AkkaHttpApi extends App with JsonSupport {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val ctx = MContext("core", InMemoryMState())
  ctx.init()

  val routes =
    pathPrefix("mapi") {
      path("object" / Remaining) { id =>
        get {
          complete(ctx.readCommand[Option[MObject]](FindMObject(id)))
        } ~
          post {
            entity(as[MObject])(mo => {
              ctx.updateCommand(CreateMObject(mo))
              complete("ok")
            })
          }
      } ~
        path("field" / Remaining) { id =>
          get {
            complete(ctx.readCommand[Option[MField]](FindMField(id)))
          } ~
            post {
              entity(as[MField])(mf => {
                ctx.updateCommand(CreateMField(mf))
                complete("ok")
              })
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
