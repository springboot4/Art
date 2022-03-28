FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/fxz-auth.jar fxz-auth.jar
# RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig
ENTRYPOINT ["java","-jar","/fxz-auth.jar", "&"]