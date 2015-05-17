# Trinity

[![Build Status](https://travis-ci.org/sisioh/trinity.png?branch=develop)](https://travis-ci.org/sisioh/trinity)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.sisioh/trinity_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.sisioh/trinity_2.11)
[![Scaladoc](http://javadoc-badge.appspot.com/org.sisioh/trinity.svg?label=scaladoc)](http://javadoc-badge.appspot.com/org.sisioh/trinity_2.11)
[![Reference Status](https://www.versioneye.com/java/org.sisioh:trinity_2.11/reference_badge.svg?style=flat)](https://www.versioneye.com/java/org.sisioh:trinity_2.11/references)

Trinity is a lightweight MVC framework based on Finagle, written in Scala.  
Since May 2013

## Concepts
- Provide MVC functions not supported by Finagle.
- Support Domain Driven-Design by non CoC(Convention over Configuration).

## Features
- You can use Actions as Controller, instead of Finagle Service.
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

Add the following to your sbt build (Scala 2.10.x, and Scala 2.11.x):

#### Release Version

```scala
resolvers += "Sonatype Release Repository" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies +=  "org.sisioh" %% "trinity-core" % "1.0.11"
```

#### Snapshot Version

```scala
resolvers += "Sonatype Snapshot Repository" at "http://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies +=  "org.sisioh" %% "trinity-core" % "1.0.12-SNAPSHOT"
```
  
### Controller code

#### Scalatra like controller

```scala
object ScalatraLikeControllerApplication
  extends ConsoleApplication {

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
object PlayLikeApplicationForController extends ConsoleApplication {

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

