FROM openjdk:11-jre-slim
WORKDIR /app
COPY build/libs/otv-attendance-bot-node-0.0.1-SNAPSHOT-plain.jar /app/node.jar
EXPOSE 8082
CMD ["java", "-jar", "node.jar"]