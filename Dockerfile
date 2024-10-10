FROM openjdk:17.0.2-jdk-slim
WORKDIR /app
COPY ./build/libs/customer-service-0.0.1-SNAPSHOT.jar /app/app.jar
CMD [ "java", "-jar", "/app/app.jar"]