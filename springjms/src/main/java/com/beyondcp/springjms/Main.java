package com.beyondcp.springjms;

import com.beyondcp.springjms.messaging.CustomMessage;
import com.beyondcp.springjms.messaging.CustomMessageQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by pranet on 08/07/17.
 */
@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    private CustomMessageQueue messageQueue;

    @Bean
    public CommandLineRunner run() {
        return (args) -> {
            CustomMessage message = new CustomMessage("pranet", "verma");
//            messageQueue.send(message);
        };
    }

}
