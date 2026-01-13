package org.example.bookmycut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookMyCutApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyCutApplication.class, args);
    }

}
