package org.sisioh.trinity

import org.specs2.mutable.Specification

class LayoutViewSpec extends Specification {

  "A LayoutView" should {
    "render" in {
      val posts = List(new Post("One"), new Post("Two"))
      val layout = new PostsView(posts)
      layout.render must contain("Posts")
      layout.render must contain("Title: One")
    }
  }

}
