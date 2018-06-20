package net.task.replicator;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReplicatorApplication {

    /**
     * This bean allows to connect to the running H2 DB from browser
     * Running on the application's port in sub-path "/console"
     * @return
     */
    @Profile("!test")
    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/console/*");
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReplicatorApplication.class, args);
    }

}
