FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/intouch.jar /app/intouch.jar
ENTRYPOINT ["java","-jar","/app/intouch.jar"]