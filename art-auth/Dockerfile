FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/art-auth.jar art-auth.jar
# RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig
ENTRYPOINT ["java","-jar","/art-auth.jar", "&"]