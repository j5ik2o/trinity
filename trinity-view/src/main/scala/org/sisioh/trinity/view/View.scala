package org.sisioh.trinity.view

import org.sisioh.trinity.domain.BodyRender

trait View extends BodyRender {

  def render: String

}
