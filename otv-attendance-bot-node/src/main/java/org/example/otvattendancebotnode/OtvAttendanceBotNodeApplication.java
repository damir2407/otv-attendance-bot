package org.example.otvattendancebotnode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OtvAttendanceBotNodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtvAttendanceBotNodeApplication.class, args);
    }

}
