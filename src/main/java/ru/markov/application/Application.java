package ru.markov.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.markov.application.data.Seria;

import java.io.IOException;


@SpringBootApplication
@Theme(variant = Lumo.DARK)

public class Application implements AppShellConfigurator {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SpringApplication.run(Application.class, args);
        Seria.load();
    }
}

