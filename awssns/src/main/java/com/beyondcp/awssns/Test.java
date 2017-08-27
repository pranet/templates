package com.beyondcp.awssns;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Created by pranet on 28/08/17.
 */
@SpringBootApplication
@Slf4j
public class Test {

    @Autowired
    private Sender sender;

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return (args) -> {
            sender.send("hello2", "world");
        };
    }

}
