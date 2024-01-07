FROM openjdk:11-jre-slim
WORKDIR /app
COPY build/libs/otv-attendance-bot-dispatcher-0.0.1-SNAPSHOT.jar /app/dispatcher.jar
EXPOSE 8080
ENTRYPOINT java -jar dispatcher.jar