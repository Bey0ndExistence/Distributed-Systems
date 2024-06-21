FROM openjdk:8-jdk-alpine

WORKDIR /app

ADD target/StudentMicroservice-1.0-SNAPSHOT-jar-with-dependencies.jar StudentMicroservice.jar

ENTRYPOINT ["java","-jar", "StudentMicroservice.jar"]
