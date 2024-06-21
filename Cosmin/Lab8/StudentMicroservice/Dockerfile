FROM openjdk:8-jdk-alpine
ADD target/StudentMicroservice-1.0-SNAPSHOT-jar-with-dependencies.jar com.sd.laborator.StudentMicroservice.jar
ADD questions_database.txt questions_database.txt

ENTRYPOINT ["java","-jar", "com.sd.laborator.StudentMicroservice.jar"]