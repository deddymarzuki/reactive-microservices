# reactive-microservices
Message Broker Implementation using spring-boot, spring-kafka, spring-webflux

This projects is also build using gradle

##prerequisite software to be installed

1. Gradle
2. Apache Kafka
3. MongoDB
4. Java SDK 11


## prerequisite for software to be run before executing any other service
1. Apache Kafka with topic name
2. MongoDB

# airport-web-service
airport-web-service is a microservice to serve airport data, and when application is boot up, it will also run a listener to a kafka topic

Default value for airport-web-service
```yaml
server.port=8081
#mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=test1

#kafka custom
kafka.bootstrapAddress=localhost:9092
kafka.topic=test
kafka.clientId=airport-web-service
kafka.groupId=tiket
```

## Building and running airport-web-service
1. Ensure that you are in airport-web-service
2. Run ./gradlew build
3. Project will be build and there will be a jar file created in build\libs\airport-web-service-1.0.0.jar
4. Run java -jar build\libs\airport-web-service-1.0.0.jar
5. Any other properties such as mongodb location, can be changed by specifiying --spring.data.mongodb.host=otherserver
6. Query can be executed by using any browser http://localhost:8081/airport


# airport-consumer-service
airport-consumer-service is a scheduled task using reactive WebClient to consume large array of json objects. 
The task is scheduled every 5 minutes.

Default value for airport-consumer-service
```yaml
#mongodb
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=test

#kafka custom
kafka.bootstrapAddress=localhost:9092
kafka.topic=test
kafka.clientId=airport-web-service

#custom api
airport.serverUri=http://localhost:3330
airport.resourceUri=/userInfo/
```

## Running airport-consumer-service
1. Ensure that you are in airport-consumer-service
2. Run ./gradlew build
3. Project will be build and there will be a jar file created in build\lib\airport-consumer-service-1.0.0.jar
4. Run java -jar build\libs\airport-consumer-service-1.0.0.jar --airport.serverUri=https://jsonblob.com/api/jsonBlob --airport.resourceUri=/f568d439-7e1d-11e9-b74c-694be5b93b2d
5. Any other properties such as mongodb location, can be changed by specifiying --spring.data.mongodb.host=otherserver

