# teeny
Geode demo app

## Running the demo
1. Start Geode cluster **TBD**
1. Start the server <BR> ``` mvn spring-boot:run ```
1. OR Create executable/deployable war <BR> ``` mvn clean package ``` <BR> and <BR> ``` java -jar target/teeny-0.1-SNAPSHOT.war ```

## Client
1. To create a teeny <BR> ``` curl -X POST http://localhost:8080?url=yahoo.com ```
1. To lookup a teeny <BR> ``` curl http://localhost:8080/<return_value_from_create> ```
1. To delete a teeny <BR> ``` curl -X DELETE http://localhost:8080/<return_value_from_create> ```
1. To list of most poplular teenyies <BR> ``` curl http://localhost:8080 ```
