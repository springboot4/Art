FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/fxz-auth.jar fxz-auth.jar
ENTRYPOINT ["java","-jar","/fxz-auth.jar", "&"]