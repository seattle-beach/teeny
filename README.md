# teeny
Geode demo app

## Running the demo
1. Start Geode cluster **TBD**
1. Start the server <BR> ``` mvn spring-boot:run ```
1. Create a teeny <BR> ``` curl -X POST http://localhost:8080?url=yahoo.com ```
1. Reverser lookup a teeny <BR> ``` curl http://localhost:8080/<return_value_from_create> ```
1. Delete a teeny <BR> ``` curl -X DELETE http://localhost:8080/<return_value_from_create> ```
1. List of most poplular teenyies <BR> ``` curl http://localhost:8080 ```
