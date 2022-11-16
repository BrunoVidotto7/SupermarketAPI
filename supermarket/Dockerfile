FROM openjdk:17-jdk-slim
ARG JAR_FILE=supermarket/target/*.jar
COPY ${JAR_FILE} app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]