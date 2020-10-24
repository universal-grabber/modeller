package net.tislib.ugm.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ShellApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ShellApplication.class);

        application.setWebApplicationType(WebApplicationType.NONE);

        application.run(args);
    }

}
