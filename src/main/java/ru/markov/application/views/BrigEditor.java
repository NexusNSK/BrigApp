package ru.markov.application.views;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

import static java.util.concurrent.TimeUnit.SECONDS;


@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("BrigApp")
public class BrigEditor extends Div {
    public BrigEditor() {
        Notification addWorkerNotification = new Notification("Техник был добавлен в базу данных");
        TextField firstName = new TextField("Имя");
        TextField lastName = new TextField("Фамилия");
        TextField fatherName = new TextField("Отчество");
        TextField category = new TextField("Категория");
        Button addWorker = new Button("Добавить техника");
        addWorker.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        addWorker.addClickListener(buttonClickEvent -> {});
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, fatherName, category, addWorker);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("500px", 2));
        // Stretch the username field over 2 columns
        formLayout.setColspan(fatherName, 2);


        add(formLayout);

    }


}