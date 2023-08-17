package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.poi.Template;
import java.io.IOException;

@Route(value = "reports", layout = MainLayout.class)
@PermitAll
@UIScope

public class Reports extends Div {
    public static int month;

    public Reports() {
        ComboBox<String> selectReport = new ComboBox<>("Выбор бригады для отчета");
        ComboBox<String> selectMonth = new ComboBox<>("Месяц");
        selectMonth.setItems("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Откябрь", "Ноябрь", "Декабрь");
        selectReport.setAllowCustomValue(true);
        selectReport.setMinWidth("220px");
        selectReport.setItems("Бригада техники", "Бригада сборщики", "Бригада монтажники", "Все бригады", "Пустой шаблон");
        Button currentReport = new Button("Скачать отчет за выбранный месяц", new Icon(VaadinIcon.FILE));
        currentReport.setIconAfterText(true);
        currentReport.setHeightFull();
        currentReport.addClickListener(event -> {
            switch (selectMonth.getValue()) {
                case "Январь" -> {
                    month = 1;
                }
                case "Февраль" -> {
                    month = 2;
                }
                case "Март" -> {
                     month = 3;
                }
                case "Апрель" -> {
                    month = 4;
                }
                case "Май" -> {
                    month = 5;
                }
                case "Июнь" -> {
                    month = 6;
                }
                case "Июль" -> {
                    month = 7;
                }
                case "Август" -> {
                    month = 8;

                }
                case "Сентябрь" -> {
                     month = 9;
                }
                case "Откябрь" -> {
                    month = 10;
                }
                case "Ноябрь" -> {
                     month = 11;
                }
                case "Декабрь" -> {
                    month = 12;
                }
            }
            try {
                new Template(selectReport.getValue());
                Notification n = Notification.show("Файл был создан и лежит в корне приложения!");
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.setPosition(Notification.Position.MIDDLE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        add(selectReport, selectMonth, currentReport);

    }
}

