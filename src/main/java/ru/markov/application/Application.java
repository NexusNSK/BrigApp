package ru.markov.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.markov.application.service.Serial;
import ru.markov.application.service.TimeAdapter;
import ru.markov.application.views.BrigEdit;

@SpringBootApplication
@Push
@Theme("brigapp")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(Application.class, args);
        TimeAdapter.initWorkTime();
        Serial.load();
        Serial.loadDevice();
        Serial.loadPreset();
        BrigEdit.initSplitDistrictWorkersList();
    }
}

