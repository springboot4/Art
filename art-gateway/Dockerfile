FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/fxz-gateway.jar fxz-gateway.jar

#openjdk比oraclejdk简化了一些功能，所以用docker打包的时候更小。
#所以后端画的验证码所要用到java的awt组件就被简化了，就一堆的空指针无法初始化错误报出来。
#需要在系统中安装FontConfig组件
RUN apk add --update font-adobe-100dpi ttf-dejavu fontconfig

ENTRYPOINT ["java","-jar","/fxz-gateway.jar", "&"]