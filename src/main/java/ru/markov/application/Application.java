package ru.markov.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.markov.application.service.Serial;
import ru.markov.application.service.TimeAdapter;


@SpringBootApplication
public class Application implements AppShellConfigurator {
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(Application.class, args);
        TimeAdapter.initWorkTime();
        Serial.load();
    }
}

