ce e modificat aici fata de original e ca am pus WORKDIR /app
si am scos linia de ADD questions_database.txt
NU ADAUGA TU questions_database.txt in DOCKERFILE!!!
SE OCUPA DOCKERCOMPOSE

FROM openjdk:8-jdk-alpine

WORKDIR /app

ADD target/StudentMicroservice-1.0-SNAPSHOT-jar-with-dependencies.jar StudentMicroservice.jar

ENTRYPOINT ["java","-jar", "StudentMicroservice.jar"]