package org.testmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.core.env.SimpleCommandLinePropertySource;

/**
 * Main.
 * 
 * @author Sergey Stotskiy
 *
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    /**
     * Profiles
     */
    public static final String PROFILE_LOCALDEBUG = "localdebug";
    public static final String PROFILE_DEV = "dev";

    public static void main(String[] args) {
        SimpleCommandLinePropertySource cmd = new SimpleCommandLinePropertySource(args);
        SpringApplication app = new SpringApplication(Application.class);
        // default set dev profile
        if (!cmd.containsProperty("spring.profiles.active")) {
            app.setAdditionalProfiles(PROFILE_LOCALDEBUG);
        }
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}