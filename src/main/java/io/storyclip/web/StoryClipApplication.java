package io.storyclip.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class StoryClipApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryClipApplication.class, args);
    }

}
