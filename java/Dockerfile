FROM ubuntu:22.04 AS builder

RUN apt-get update
RUN apt-get install -qy git
RUN apt-get install -y wget
RUN apt-get install -qy openjdk-17-jdk
RUN wget https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz -P /tmp
RUN tar xf /tmp/apache-maven-*.tar.gz -C /opt
RUN ln -s /opt/apache-maven-3.8.8 /opt/maven

ENV JAVA_HOME=/usr/lib/jvm/java-1.17.0-openjdk-amd64
ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17

COPY --from=builder ./target/chatbot-0.0.1-SNAPSHOT.jar .
EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=pro", "-jar", "./chatbot-0.0.1-SNAPSHOT.jar"]
