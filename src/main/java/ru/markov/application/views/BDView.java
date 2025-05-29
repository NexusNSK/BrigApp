package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Route(value = "db_setting", layout = MainLayout.class)
@PageTitle("BrigApp א Настройки БД")
@RolesAllowed("ADMIN")
public class BDView extends VerticalLayout {

    private TextField dbUrlField;
    private Button saveButton;

    public BDView() {
        // Основные настройки выравнивания
        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(false);

        // Поле ввода
        dbUrlField = new TextField();
        dbUrlField.setPlaceholder("jdbc:mysql://localhost:3306/mydb");
        dbUrlField.setWidth("350px");
        dbUrlField.setClearButtonVisible(true);

        // Кнопка
        saveButton = new Button("Сохранить настройки", event -> saveSettings());
        saveButton.getStyle().set("margin-left", "12px");

        // Горизонтальное размещение поля и кнопки
        HorizontalLayout inputLayout = new HorizontalLayout(dbUrlField, saveButton);
        inputLayout.setAlignItems(Alignment.END);
        inputLayout.setSpacing(true);
        inputLayout.getStyle().set("margin-bottom", "20px");

        add(inputLayout);
    }

    private void saveSettings() {
        String dbUrl = dbUrlField.getValue();
        if (dbUrl == null || dbUrl.isEmpty()) {
            Notification.show("Пожалуйста, введите адрес БД", 3000, Notification.Position.MIDDLE);
            return;
        }

        Properties props = new Properties();
        props.setProperty("url", dbUrl);

        try (FileOutputStream out = new FileOutputStream("DB_setting.properties")) {
            props.store(out, "DB Settings");
            Notification.show("Настройки сохранены!", 3000, Notification.Position.MIDDLE);
        } catch (IOException e) {
            Notification.show("Ошибка при сохранении файла: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}