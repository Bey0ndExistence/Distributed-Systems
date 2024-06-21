exemplu dockerfile pentru un serviciu (pune copy paste asta la toate microserviciile MAI PUTIN la MessageLibrary CARE E O LIBRARIE NU MICROSERVICIU)
# AuctioneerMicroservice/Dockerfile
FROM openjdk:8-jdk-alpine
ADD out/artifacts/AuctioneerMicroservice_jar/AuctioneerMicroservice.jar AuctioneerMicroservice.jar

ENTRYPOINT ["java", "-jar", "AuctioneerMicroservice.jar"]
