package org.sisioh.trinity.infrastructure

class MutableVector[A] {

  private var _vector = Vector[A]()

  def vector = _vector

  def add(x: A) {
    _vector = x +: _vector
  }

}
