# teeny
Teeny is an [Apache Geode (incubating)](geode.incubator.apache.org) based URL shortner application.

This application is intended to server as an introduction to Apache Geode basic concepts.

## Requirements

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

* [Maven](https://maven.apache.org/download.cgi)

* Docker & Compose

On Linux use package managers available for most distributions.  
On Mac use [boot2docker](boot2docker.io) or [Kitematic](https://kitematic.com/)

Then install docker-compose with ```sudo pip install -U docker-compose```

##  Building and running the application

1. Start Apache Geode cluster 
  * You can either do it using GFSH or through Docker. 
  ``` docker-compose up ```
  If you are using Docker on this step it may take a while depending on your internet connection.
2. Create the teeny client container:
  ```docker build -t geode/teeny-client .```

This step will clone this repository and build the application, which consists 
of a SpringBoot application that has a single Servlet. 

Otherwhise if you're running locally without Docker, clone this repository and build an executable jar:

``` mvn clean package ``` 

And start the client application with:

```java -jar /teeny/target/teeny-0.1-SNAPSHOT.war```

## Calling the application
1. To create a teeny
``` curl -X POST http://localhost:8080?url=yahoo.com ```
1. To lookup a teeny 
``` curl http://localhost:8080/<return_value_from_create> ```
1. To delete a teeny
``` curl -X DELETE http://localhost:8080/<return_value_from_create> ```
1. To list of most poplular teenyies 
``` curl http://localhost:8080 ```

## Scale using Docker & Apache Geode (incubating)

Given that it's easy to scale an Apache Geode cluster you can easily leverage docker-compose functionalities to add more containers and more nodes to any component of the solution.

For instance in order to add more instances of the teeny client just execute:

```docker-compose scale client=2 ```

To add more Apache Geode servers:

```docker-compose scale server=2 ```
