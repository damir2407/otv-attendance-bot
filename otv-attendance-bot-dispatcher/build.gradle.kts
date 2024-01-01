plugins {
    java
    id("org.springframework.boot") version "2.5.2"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.amazonaws:amazon-sqs-java-messaging-lib:1.0.8")
    implementation("com.amazonaws:aws-java-sdk-sqs:1.11.176")
    implementation("org.telegram:telegrambots-spring-boot-starter:6.1.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
