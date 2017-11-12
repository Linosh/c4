package c4.mmdd.actor

import akka.actor.Actor

final class ActorWrapper(f: Any => Unit) extends Actor {
  override def receive: Receive = {
    case e@_ => f(e)
  }
}