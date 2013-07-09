# Trinity

[![Build Status](https://travis-ci.org/sisioh/trinity.png?branch=develop)](https://travis-ci.org/sisioh/trinity)

Trinity is a lightweight MVC framework based on Finagle, which can be described in Scala.

## Concepts
- We provide functions about MVC which does not support finagle.
- We support Domain Driven-Design by non CoC(Convention over Configuration).

## Features
- You can describe Actions over a Controller as a Finagle Service.
  - Routing information can described to action methods, like Scalatra.
  - Or, The Routing information can be aggregated on the outside of Controller, like Play2.
- You can use Template Engine (such as Scalatra) with Trinity.

## Functions
### Supported Functions
- Routing request to action
  - A action can be described as async process by using `com.twitter.util.Future`.
  - Or, You can also select action as sync process by using `com.twitter.util.FuturePool`.
  - We support an action adaptor for a process which returns `scala.concurrent.Future`.
- Finagle's Request/Response Enhance
  - multi-part file upload
  - json format reponse
  - file resouce support
- Binding to Template Engine
  - [Scalate](http://scalate.fusesource.org/)
  - [Velocity](http://velocity.apache.org/)
  - [FreeMarker](http://freemarker.org/)
  - [Thymeleaf](http://www.thymeleaf.org/)
- Testing
  - Unit Testing
  - Integration Testing
- JRebel support
  - see this gist for installation. https://gist.github.com/j5ik2o/5660744

### Unsupported Functions
- Functions for Form, Validation 
- Functions for Persistence(sush as RDBMS/NoSQL)

## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## Setup

### Build Configuration

Please add configuration in following to Build.scala.

```scala
  settings = Default.settings ++ Seq(
    resolvers ++= Seq(
      // ...
      "Sisioh Trinity Release Repository" at "http://sisioh.github.io/trinity/repos/release/",
      // ...
    ),
    libraryDependencies ++= Seq(
      // ...
      "org.sisioh" %% "trinity" % "0.0.7",
      // ...
    )
  )
```

### Bootstrap code

```scala
val config = Config.fromFile()
implicit val application = TrinityApplication(config)

application.registerController(new GreetingController)
application.start()   
```

### Controller code

Scalatra like controller

```scala
class GreetingController(implicit application: TrinityApplication) extends SimpleController {

    get("/hello") {
      request =>
        responseBuilder.withPlain("Hello!").toFinagleResponseFuture
    }
    
}
```

Play2 like controller

```scala
class GreetingController(implicit application: TrinityApplication) extends AbstractController {

    def hello = FutureAction {
      request =>
        responseBuilder.withPlain("Hello!").toFinagleResponseFuture
    }

}
```

Please add codes for routing in following to bootstrap.

```scala
// ...
implicit val application = TrinityApplication(config)
val greetingController = new GreetingController
application.addRoute(Method.Get, "/hello", greetingController, greetingController.hello)

application.registerController(new GreetingController)
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


