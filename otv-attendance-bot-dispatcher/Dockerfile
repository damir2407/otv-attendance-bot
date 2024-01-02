FROM openjdk:11-jre-slim
WORKDIR /app
COPY build/libs/otv-attendance-bot-dispatcher-0.0.1-SNAPSHOT-plain.jar /app/dispatcher.jar
EXPOSE 8080
CMD ["java", "-jar", "dispatcher.jar"]