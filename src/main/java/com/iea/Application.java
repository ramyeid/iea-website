package com.iea;

import com.iea.listener.AsynchronousScreenListenersNotifier;
import com.iea.listener.SimulatorListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static final Logger LOGGER = LogManager.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        LOGGER.info("------------ Server Started ------------ ");
        AsynchronousScreenListenersNotifier.addListener(new SimulatorListener());
        SpringApplication.run(Application.class, args);
    }
}