package ru.markov.application.views;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
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
    public BrigEditor() {

        Grid<Worker> workerGrid = new Grid<>(Worker.class, false);
        workerGrid.addColumn(Worker::getFullName).setHeader("Сотрудник");
        workerGrid.setItems(workerList);
        workerGrid.setWidth("700px");

        TextField firstName = new TextField("Имя");
        TextField lastName = new TextField("Фамилия");
        TextField fatherName = new TextField("Отчество");

        Button addWorker = new Button("Добавить техника");
        addWorker.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        addWorker.addClickListener(buttonClickEvent -> {
            workerList.add(new Worker(firstName.getValue(), lastName.getValue(), fatherName.getValue()));
            workerGrid.getDataProvider().refreshAll();
            firstName.clear(); lastName.clear(); fatherName.clear();
        });

        Button saveWorkers = new Button("Сохранить состав бригады");
        saveWorkers.addThemeVariants(ButtonVariant.LUMO_ICON);
        saveWorkers.addClickListener(buttonClickEvent -> {
            Seria.save();
        });


        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, fatherName, addWorker, saveWorkers);
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));
        formLayout.setColspan(fatherName, 2);
        formLayout.setMaxWidth("500px");


        add(formLayout);
        add(workerGrid);

    }



}