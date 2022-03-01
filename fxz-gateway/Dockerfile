FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/fxz-gateway.jar fxz-gateway.jar
ENTRYPOINT ["java","-jar","/fxz-gateway.jar", "&"]