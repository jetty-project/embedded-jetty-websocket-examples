# WebSockets on Embedded Jetty

This project contains examples on using WebSockets with Embedded Jetty.

Note: If you want to use CDI + websockets with Jetty, check out the example project at

https://github.com/jetty-project/embedded-jetty-weld

There are 2 APIs you can use with Jetty, the native WebSocket API and the javax.websocket API.

##Build Project
``` mvn clean install```

# Project: javax.websocket-example

Demonstration of how to create a WebSocket Client or a WebSocket Server using `javax.websocket` APIs. 
After building the project, from the project root directory, run the following commands 

### Server
```java -cp javax.websocket-example/target/javax.websocket-example-1.0-SNAPSHOT-jar-with-dependencies.jar  org.eclipse.jetty.demo.EventServer```

### Client
```java -cp javax.websocket-example/target/javax.websocket-example-1.0-SNAPSHOT-jar-with-dependencies.jar  org.eclipse.jetty.demo.EventClient```


# Project: native-jetty-websocket-example

Demonstration of how to create a WebSocket Client or WebSocket Server using `org.eclipse.jetty.websocket` APIs.
After building the project, from the project root directory, run the following commands

### Server
```java -cp native-jetty-websocket-example/target/native-jetty-websocket-example-1.0-SNAPSHOT-jar-with-dependencies.jar  org.eclipse.jetty.demo.EventServer```

### Client
```java -cp native-jetty-websocket-example/target/native-jetty-websocket-example-1.0-SNAPSHOT-jar-with-dependencies.jar  org.eclipse.jetty.demo.EventClient```
