package com.c4.core.lifecycle

/**
  * Created by dmitriiiermiichuk on 6/13/16.
  */
trait State[+This] {
  this: This =>
  def map[R <: State[R]](fun: This => R): R = fun(this)
}

trait KeyValueState[K, V] extends State[KeyValueState[K, V]] {
  def get(key: K): Option[V]

  def add(key: K, value: V): KeyValueState[K, V]

  def remove(key: K): KeyValueState[K, V]
}

case class MapLikeState[K, V](stateEntity: Map[K, V]) extends KeyValueState[K, V] {
  override def get(key: K): Option[V] = stateEntity.get(key)

  override def remove(key: K): MapLikeState[K, V] = copy(stateEntity - key)

  override def add(key: K, value: V): MapLikeState[K, V] = copy(stateEntity + (key -> value))
}

object MapLikeState {
  def apply[K, V](): MapLikeState[K, V] = new MapLikeState[K, V](Map())
}

