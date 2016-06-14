package com.c4.core.lifecycle

/**
  * Created by dmitriiiermiichuk on 6/13/16.
  */
sealed trait State {}

class KeyValueState extends State {}
object KeyValueState {
  def apply(): KeyValueState = new KeyValueState
}

