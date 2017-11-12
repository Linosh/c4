package c4.mmdd.state

import java.util.concurrent.{CountDownLatch, TimeUnit}

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import c4.mmdd.event.{CreateCObject, FindCObject}
import c4.mmdd.model.CObject
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class StateManagerTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll {
  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  behavior of "State Manager"

  it should "async update app state" in {
    val cnt = new CountDownLatch(1)
    val writer = StateWriter(NoCState, _ => cnt.countDown())
    val reader = StateReader(NoCState, identity)

    val stateMngr = StateManager(writer, reader, "1")
    val createObjCmd = CreateCObject(CObject.empty)

    stateMngr.updateCommand(createObjCmd)

    assert(cnt.await(1, TimeUnit.SECONDS))
  }

  it should "sync read app state" in {
    val cnt = new CountDownLatch(1)
    val writer = StateWriter(NoCState)
    val reader = StateReader(NoCState, _ => cnt.countDown())

    val stateMngr = StateManager(writer, reader, "2")
    val findCmd = FindCObject("")

    stateMngr.readCommand(findCmd)

    assert(cnt.await(1, TimeUnit.SECONDS))
  }
}
