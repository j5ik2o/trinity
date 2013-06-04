package org.sisioh.trinity

case class Post(val title: String)

class PostsListView(val posts: List[Post]) extends MustacheView {
  val template = "posts.mustache"
  override val contentType = Some("text/plain")
}

class PostsView(val posts: List[Post]) extends MustacheView {
  val template = "posts_layout.mustache"
  val postsListView = new PostsListView(posts)

  def body = postsListView.render
}
