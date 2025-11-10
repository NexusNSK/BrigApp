package ru.markov.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.markov.application.service.Serial;
import ru.markov.application.views.BrigEdit;

@SpringBootApplication
@Push
@Theme("brigapp")
@PWA(name = "BrigApp",
        shortName = "BrigApp",
        description = "BrigApp mobile",
        backgroundColor = "#ffffff",
        themeColor = "#000000",
        offlinePath = "offline.html")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(Application.class, args);
        Serial.load();
        Serial.loadDevice();
        Serial.loadPreset();
        BrigEdit.initSplitDistrictWorkersList();
    }
}

