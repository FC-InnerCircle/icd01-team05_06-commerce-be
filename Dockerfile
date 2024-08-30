FROM amazoncorretto:21-alpine
LABEL authors="psh10066@gmail.com"
EXPOSE 8080

ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]