FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/fxz-server-system.jar fxz-server-system.jar
ENTRYPOINT ["java","-jar","/fxz-server-system.jar", "&"]