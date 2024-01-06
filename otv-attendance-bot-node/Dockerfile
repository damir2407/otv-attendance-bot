FROM openjdk:11-jre-slim
WORKDIR /app
COPY build/libs/otv-attendance-bot-node-0.0.1-SNAPSHOT.jar /app/node.jar
EXPOSE 8080
CMD ["java", "-jar", "node.jar"]