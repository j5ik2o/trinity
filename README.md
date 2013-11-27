# Trinity

[![Build Status](https://travis-ci.org/sisioh/trinity.png?branch=develop)](https://travis-ci.org/sisioh/trinity)

Trinity is a lightweight MVC framework based on Finagle, written in Scala.

## Concepts
- Provide MVC functions not supported by Finagle.
- Support Domain Driven-Design by non CoC(Convention over Configuration).

## Features
- You can deine Actions as Controller of Finagle Service.
  - URLs can be mapped to action methods, like Scalatra.
  - Otherwise, The Routing information can be outside of the Controller, like Play2.
- You can use Template Engine (e.g. Scalatra) with Trinity.

## Functions
### Supported Functions
- Routing request to action. return's type is `scala.concurrent.Future`
- Finagle's Request/Response Enhance
  - Multi-part file upload
  - JSON format reponse
  - File resouce support
- Binding to Template Engine
  - [Scalate](http://scalate.fusesource.org/)
  - [Velocity](http://velocity.apache.org/) TBD
  - [FreeMarker](http://freemarker.org/) TBD
  - [Thymeleaf](http://www.thymeleaf.org/) TBD
- Testing
  - Unit testing
  - Integration testing
- JRebel support
  - see this gist for installation. https://gist.github.com/j5ik2o/5660744

### Unsupported Functions
- Form
- Validation 
- Persistence(e.g. RDBMS/NoSQL)

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## Setup

### Build Configuration

Please add the following configuration to Build.scala.

```scala
object AppBuild extends Build {
  val root = Project(
    id = "app",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      resolvers ++= Seq(
        // ...
        "Sonatype Snapshot Repository" at "http://oss.sonatype.org/content/repositories/snapshots/",
        "Sonatype Release Repository" at "http://oss.sonatype.org/content/repositories/releases/",
       // ...
      ),
      libraryDependencies ++= Seq(
        // ...
        "org.sisioh" %% "trinity-core" % "1.0.0-SNAPSHOT",
        "org.sisioh" %% "trinity-view-scalate" % "1.0.0-SNAPSHOT", // optional
        
        // ...
      )
    )
  )
}
```

### Controller code

#### Scalatra like controller

```scala
object ScalatraLikeControllerApplication
  extends App with BootstrapWithScalatraLikeSupport {

  get("/hello") {
    request =>
      responseBuilder.withTextPlain("Hello World!!").toFuture
  }

  get("/json") {
    request =>
      val jValue = JObject(
        JField("name", JString("value"))
      )
      responseBuilder.withJson(jValue).toFuture
  }

  get("/user/:userId") {
    request =>
      responseBuilder.withTextPlain("userId = " + request.routeParams("userId")).toFuture
  }

  get( """/group/(.*)""".r, Seq("name")) {
    request =>
      ResponseBuilder().withTextPlain("name = " + request.routeParams("name")).toFuture
  }

  startWithAwait()

}
```

#### Play2 like controller

```scala
object PlayLikeApplicationForController extends App with Bootstrap {

  case class MainController() extends ControllerSupport {

    def helloWorld = SimpleAction {
      request =>
        responseBuilder.withTextPlain("Hello World!!").toFuture
    }

    def getUser = SimpleAction {
      request =>
        responseBuilder.withTextPlain("userId = " + request.routeParams("userId")).toFuture
    }

    def getGroup(name: String) = SimpleAction {
      request =>
        responseBuilder.withTextPlain("name = " + name).toFuture
    }
  }

  val mainController = MainController()

  override protected val routingFilter = Some(RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> mainController.helloWorld,
        Get % "/user/:userId" -> mainController.getUser,
        Get % ("""/group/(.*)""".r -> Seq("name")) -> {
          request =>
            mainController.getGroup(request.routeParams("name"))(request)
        }
      )
  })

  startWithAwait()

}
```

### Test

#### for Unit Testing

```scala
class ControllerUnitTestSupportSpec extends Specification with ControllerUnitTestSupport {

  def helloWorld = SimpleAction {
    request =>
      responseBuilder.withContent("Hello World!").toFuture
  }

  val routingFilter = RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(Get % "/hello" -> helloWorld)
  }

  implicit val testContext = UnitTestContext(routingFilter)

  "helloWorld" should {
    "be able to get response" in {
      testGet("/hello") {
        result =>
          result must beSuccessfulTry.like {
            case response =>
              response.contentAsString() must_== "Hello World!"
          }
      }
    }
  }
}
```

#### for Integration Testing

```scala

class ControllerIntegrationTestSupportSpec extends Specification with ControllerIntegrationTestSupport {

  def helloWorld = SimpleAction {
    request =>
      ResponseBuilder().withContent("Hello World!").toFuture
  }

  val routingFilter = RoutingFilter.createForActions {
    implicit pathPatternParser =>
      Seq(
        Get % "/hello" -> helloWorld
      )
  }

  implicit val testContext = IntegrationTestContext()

  "HelloWorld" should {
    "be able to get response" in new WithServer(Server(filter = Some(routingFilter))) {
      testGet("/hello") {
        result =>
          result must beSuccessfulTry.like {
            case response =>
              response.contentAsString() must_== "Hello World!"
          }
      }
    }
    
    "be able to get response, when without server" in {
      val server = Server(filter = Some(routingFilter))
      val f = server.start(Environment.Development).map {
        _ =>
          testGet("/hello") {
            result =>
              result must beSuccessfulTry.like {
                case response =>
                  response.contentAsString() must_== "Hello World!"
              }
          }
      }.flatMap {
        result =>
          server.stop.map(_ => result)
      }
      Await.result(f, Duration.Inf)
    }

  }
}
```


### Build 

```sh
$ sbt clean compile
```

### Run

```sh
$ sbt run
```

### Test

```sh
$ curl -X GET http://localhost:7070/hello
Hello!
```

