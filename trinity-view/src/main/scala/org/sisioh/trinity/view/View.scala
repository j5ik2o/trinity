package org.sisioh.trinity.view

import org.sisioh.trinity.BodyRender

trait View extends BodyRender {

  def render: String

}
