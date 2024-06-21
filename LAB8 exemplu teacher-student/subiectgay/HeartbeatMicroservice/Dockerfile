FROM openjdk:8-jdk-bullseye

#RUN sh -c 'cat > /etc/apk/repositories << EOF; $(echo) \
#https://dl-cdn.alpinelinux.org/alpine/v$(cut -d'.' -f1,2 /etc/alpine-release)/main/ \
#https://dl-cdn.alpinelinux.org/alpine/v$(cut -d'.' -f1,2 /etc/alpine-release)/community \
#https://dl-cdn.alpinelinux.org/alpine/edge/testing/ \
#EOF'
#
#RUN apk update
#
#RUN apk add curl docker-cli-compose
#RUN apt-get update
#RUN apt-get install docker-compose-plugin
#RUN apt install docker-compose-plugin
RUN curl -SL https://github.com/docker/compose/releases/download/v2.27.2/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose
RUN ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

WORKDIR /app

ADD target/HeartbeatMicroservice-1.0-SNAPSHOT-jar-with-dependencies.jar HeartbeatMicroservice.jar

ENTRYPOINT ["java","-jar", "HeartbeatMicroservice.jar"]
