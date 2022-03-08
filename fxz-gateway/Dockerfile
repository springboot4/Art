FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/fxz-gateway.jar fxz-gateway.jar
RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig
ENTRYPOINT ["java","-jar","/fxz-gateway.jar", "&"]