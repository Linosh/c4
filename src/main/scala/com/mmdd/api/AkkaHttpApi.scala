package com.mmdd.api

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path, pathPrefix, post, _}
import akka.stream.ActorMaterializer
import com.mmdd.context.MContext
import com.mmdd.event._
import com.mmdd.model.{MField, MObject}
import com.mmdd.state.InMemoryMState

import scala.io.StdIn

object AkkaHttpApi extends App with JsonSupport {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val cnt = new AtomicInteger(0)

  val ctx = MContext("core", InMemoryMState())
  ctx.init()

  val routes =
    pathPrefix("mapi") {
      pathPrefix("object") {
        path(Remaining) { id =>
          get {
            complete(ctx.readCommand[Option[MObject]](FindMObject(id)))
          }
        } ~
          pathEnd {
            post {
              entity(as[MObject])(mo => {
                cnt.incrementAndGet()
                ctx.updateCommand(CreateMObject(mo))
                complete(HttpEntity(ContentTypes.`application/json`, "ok"))
              })
            }
          }
      } ~
        pathPrefix("field") {
          path(Remaining) { id =>
            get {
              complete(ctx.readCommand[Option[MField]](FindMField(id)))
            }
          } ~
            pathEnd {
              post {
                cnt.incrementAndGet()
                entity(as[MField])(mf => {
                  ctx.updateCommand(CreateMField(mf))
                  complete(HttpEntity(ContentTypes.`application/json`, "ok"))
                })
              }
            }
        } ~
      path("stats") {
        get {
          println(s"\n\nRecieved msgs ${cnt.intValue()}")
          ctx.updateCommand(PrintStats)
          complete(HttpEntity(ContentTypes.`application/json`, "ok"))
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
