package ru.markov.application.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Scope;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import ru.markov.application.data.Seria;
import ru.markov.application.data.Worker;

import java.util.ArrayList;
import java.util.List;


@SpringComponent
@Scope("prototype")
@PermitAll
@Route(value = "brigapp", layout = MainLayout.class)
@PageTitle("BrigApp")
public class BrigEditor extends Div {
    public static List<Worker>workerList = new ArrayList<>();
    private static List<Worker>tempList = new ArrayList<>();
    public BrigEditor() {

        Grid<Worker> newWorkersGrid = new Grid<>(Worker.class, false);
        newWorkersGrid.addColumn(Worker::getFullName).setHeader("Недавно добавленные сотрудники:");
        newWorkersGrid.setItems(tempList);
        newWorkersGrid.setWidth("700px");

        TextField firstName = new TextField("Имя");
        TextField lastName = new TextField("Фамилия");
        TextField fatherName = new TextField("Отчество");

        Button addWorker = new Button("Добавить техника");
        addWorker.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        addWorker.addClickListener(buttonClickEvent -> {
            if (firstName.getValue()!=""&&lastName.getValue()!=""&&fatherName.getValue()!="")
            {
                workerList.add(new Worker(firstName.getValue(), lastName.getValue(), fatherName.getValue()));
                tempList.add(new Worker(firstName.getValue(), lastName.getValue(), fatherName.getValue()));
                Notification notification = Notification
                        .show("Сотрудник был добавлен!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.MIDDLE);
                firstName.clear(); lastName.clear(); fatherName.clear();
            }else{
                Notification notification = Notification
                        .show("Сотрудник не был добавлен!\nНеобходимо заполнить все поля.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
            }
            newWorkersGrid.getDataProvider().refreshAll();


        });

        Button saveWorkers = new Button("Сохранить состав бригады");
        saveWorkers.addThemeVariants(ButtonVariant.LUMO_ICON);
        saveWorkers.addClickListener(buttonClickEvent -> {
            Seria.save();
        });


        FormLayout formLayout = new FormLayout();
        formLayout.add(lastName, firstName, fatherName, addWorker, saveWorkers);
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));
        formLayout.setColspan(fatherName, 2);
        formLayout.setMaxWidth("500px");


        add(formLayout);
        add(newWorkersGrid);

    }



}