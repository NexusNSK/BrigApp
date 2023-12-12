package ru.markov.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.markov.application.service.Serial;
import ru.markov.application.service.TimeAdapter;

import java.io.IOException;


@SpringBootApplication
@Theme(value = "brigapp")
public class Application implements AppShellConfigurator {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        TimeAdapter.initWorkTime();
        Serial.load();
        //Serial.loadMigration();
        //GridEdit.initSplitDistrictWorkersList();
        //WhoNext.initListForWhoNext();
    }
}

