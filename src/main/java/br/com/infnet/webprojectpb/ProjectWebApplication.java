package br.com.infnet.webprojectpb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectWebApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "h2");
        SpringApplication.run(ProjectWebApplication.class, args);
    }
}
