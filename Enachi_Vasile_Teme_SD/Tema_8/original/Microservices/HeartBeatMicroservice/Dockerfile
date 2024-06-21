FROM openjdk:8-jdk-alpine
ADD target/HeartBeatMicroservice-1.0.0-jar-with-dependencies.jar HeartBeatMicroservice.jar
ADD questions_database.txt questions_database.txt
ENTRYPOINT ["java","-jar", "HeartBeatMicroservice.jar"]