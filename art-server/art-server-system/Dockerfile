FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/art-server-system.jar art-server-system.jar
# RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig
ENTRYPOINT ["java","-jar","/art-server-system.jar", "&"]