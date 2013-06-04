package org.sisioh.trinity

import com.github.mustachejava._
import com.google.common.base.Charsets;
import com.twitter.io.TempFile
import com.twitter.mustache._
import java.io._
import java.util.concurrent.Callable
import java.util.concurrent.Executors

private[trinity] class FinatraMustacheFactory(baseTemplatePath: String) extends DefaultMustacheFactory {
  override def encode(str: String, wtr: Writer) {
    wtr.append(str, 0, str.length)
  }

  override def getReader(resourceName: String): Reader = {
    if (!"development".equals(Config.get("env"))) {
      super.getReader(resourceName)
    }
    // In development mode, we look to the local file
    // system and avoid using the classloader which has
    // priority in DefaultMustacheFactory.getReader
    else {
      val file: File = new File(baseTemplatePath, resourceName)
      if (file.exists() && file.isFile()) {
        try {
          new BufferedReader(new InputStreamReader(new FileInputStream(file),
            Charsets.UTF_8));
        } catch {
          case exception: FileNotFoundException =>
            throw new MustacheException("Found file, could not open: " + file, exception)
        }
      }
      else {
        throw new MustacheException("Template '" + resourceName + "' not found at " + file);
      }
    }
  }

  /**
   * Invalidate template caches during development
   */
  def invalidateMustacheCaches(): Unit = {
    mustacheCache.invalidateAll()
    templateCache.invalidateAll()
  }

}

object MustacheView {
  lazy val mustacheFactory = new FinatraMustacheFactory(Config.get("local_docroot"))
  var baseTemplatePath = Config.get("template_path")

  def templatePath = baseTemplatePath

  mustacheFactory.setObjectHandler(new TwitterObjectHandler)
  mustacheFactory.setExecutorService(Executors.newCachedThreadPool)
}

abstract class MustacheView extends View {

  def template: String

  def templatePath = baseTemplatePath + template

  private val factory = MustacheView.mustacheFactory
  private val baseTemplatePath = MustacheView.templatePath
  protected val contentType: Option[String] = None

  private def mustache = factory.compile(new InputStreamReader(FileResolver.getInputStream(templatePath)), "template")

  def render = {
    // In development mode, we flush all of our template
    // caches on each render. Otherwise, partials will
    // remain unchanged in the browser while being edited.
    if ("development".equals(Config.get("env"))) {
      factory.invalidateMustacheCaches()
    }

    val output = new StringWriter
    mustache.execute(output, this).flush()
    output.toString
  }

}
