package com.c4.core.lifecycle

/**
  * Created by dmitriiiermiichuk on 6/13/16.
  */
sealed trait State {}

trait KeyValueState[K, V] extends State {
  def get(key: K): V

  def add(key: K, value: V): KeyValueState[K, V]

  def remove(key: K): KeyValueState[K, V]
}

case class MapLikeState[K, V](stateEntity: Map[K, V]) extends KeyValueState[K, V] {
  override def get(key: K): V = stateEntity(key)

  override def remove(key: K): KeyValueState[K, V] = copy(stateEntity - key)

  override def add(key: K, value: V): KeyValueState[K, V] = copy(stateEntity + (key -> value))
}

object MapLikeState {
  def apply[K, V](): MapLikeState[K, V] = new MapLikeState[K, V](Map())
}

