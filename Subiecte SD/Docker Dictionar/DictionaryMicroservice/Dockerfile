FROM openjdk:8-jdk-alpine
WORKDIR /usr/src/student
ADD target/DictionaryMicroservice-1.0-SNAPSHOT-jar-with-dependencies.jar DictionaryMicroservice.jar
ADD dictionar.txt dictionary.txt

ENTRYPOINT ["java","-jar", "DictionaryMicroservice.jar"]