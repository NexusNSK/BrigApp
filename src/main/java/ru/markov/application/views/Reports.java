package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.poi.Template;
import ru.markov.application.security.SecurityService;

import java.io.IOException;
@Route(value = "reports", layout = MainLayout.class)
@PermitAll
@UIScope

public class Reports extends Div {
    public Reports(SecurityService securityService){

        Button currentReport = new Button("Скачать отчет за текущий месяц");
        currentReport.addClickListener(event -> {
            boolean flag = false;
            getStyle().set("color", "var(--lumo-secondary-text-color)");
            ProgressBar progressBar = new ProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setVisible(flag);
            Div progressBarLabel = new Div();
            progressBarLabel.setText("Создаю отчёт...");
            progressBarLabel.setVisible(flag);
            add(progressBarLabel, progressBar);
            try {
                flag = true;
                new Template();
                Notification n = Notification.show("Файл был создан и лежит в корне приложения!");
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.setPosition(Notification.Position.MIDDLE);
                flag = false;
            } catch (IOException e) {
                flag = false;
                throw new RuntimeException(e);
            }

        });
        add(currentReport);

    }
}
//разобраться с прогресс баром
