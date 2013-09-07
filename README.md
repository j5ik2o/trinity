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
- Routing request to action
  - Action can be defined as asynchronous process by using `com.twitter.util.Future`.
  - Action van be defined as synchronous process by using `com.twitter.util.FuturePool`.
  - Support an action adaptor for a process which returns `scala.concurrent.Future`.
- Finagle's Request/Response Enhance
  - Multi-part file upload
  - JSON format reponse
  - File resouce support
- Binding to Template Engine
  - [Scalate](http://scalate.fusesource.org/)
  - [Velocity](http://velocity.apache.org/)
  - [FreeMarker](http://freemarker.org/)
  - [Thymeleaf](http://www.thymeleaf.org/)
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
        "org.sisioh" %% "trinity-core" % "0.0.18",
        "org.sisioh" %% "trinity-view-scalate" % "0.0.18", // optional
        
        // ...
      )
    )
  )
}
```

### Bootstrap code

```scala
val config = Config.fromFile()
implicit val application = TrinityApplication(config)

application.registerController(new GreetingController)
application.start()   
```

### Controller code

#### Scalatra like controller

```scala
class GreetingController(implicit application: TrinityApplication) extends SimpleController {

    get("/hello") {
      request =>
        responseBuilder.withPlain("Hello!").toFinagleResponseFuture
    }
    
}
```

#### Play2 like controller

```scala
class GreetingController(implicit application: TrinityApplication) extends AbstractController {

    def hello = FutureAction {
      request =>
        responseBuilder.withPlain("Hello!").toFinagleResponseFuture
    }

}
```

Please modify bootstrap for routing to Play2 like controler.

```scala
// ...
implicit val application = TrinityApplication(config)

val greetingController = new GreetingController
application.addRoute(Method.Get, "/hello", greetingController, greetingController.hello)

application.registerController(greetingController)
// ...
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


