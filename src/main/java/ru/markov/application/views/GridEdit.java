package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Serial;
import ru.markov.application.data.Worker;
import ru.markov.application.data.ValidationName;

import java.util.ArrayList;
import java.util.List;

@Route(value = "grid_workers", layout = MainLayout.class)
@PermitAll
public class GridEdit extends VerticalLayout {

    //в этой коллекции хранятся сохраняемые сотрудники, используется для загрузки данных при старте приложения
    public static List<Worker> workerList = new ArrayList<>();


    public GridEdit() {

        TextField firstNameT = new TextField("Имя"); //поле ввода имени при добавлении сотрудника
        TextField lastNameT = new TextField("Фамилия"); //поле ввода фамилии при добавлении сотрудника
        TextField fatherNameT = new TextField("Отчество"); //поле ввода отчества при добавлении сотрудника

        Grid<Worker> grid = new Grid<>(Worker.class, false); //основная таблица с сотрудниками
        Editor<Worker> editor = grid.getEditor();
        grid.setItems(workerList);

//блок добавления сотрудника во временный список без глобального сохранения
        Button addWorker = new Button("Добавить техника");
        addWorker.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        addWorker.addClickListener(buttonClickEvent -> {
            if (!(firstNameT.getValue().equals(""))&&!(lastNameT.getValue().equals(""))&&!(fatherNameT.getValue().equals("")))
            {
                workerList.add(new Worker(firstNameT.getValue(), lastNameT.getValue(), fatherNameT.getValue()));
                Notification notification = Notification
                        .show("Сотрудник был добавлен!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.MIDDLE);
                firstNameT.clear(); lastNameT.clear(); fatherNameT.clear();
                grid.getDataProvider().refreshAll();
            }else{
                Notification notification = Notification
                        .show("Сотрудник не был добавлен!\nНеобходимо заполнить все поля.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
            }
        });


        //блок глобального сохранения состояния бригады
        Button saveWorkers = new Button("Сохранить состав бригады");
        saveWorkers.addThemeVariants(ButtonVariant.LUMO_ICON);
        saveWorkers.addClickListener(buttonClickEvent -> {
            Serial.save();
            Notification brigNote = Notification.show("Состав бригады сохранён");
            brigNote.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            brigNote.setPosition(Notification.Position.MIDDLE);
        });

//объявление формы, отвечающей за добавление сотрудников и сохранения бригады
        FormLayout formLayout = new FormLayout();
        formLayout.add(lastNameT, firstNameT, fatherNameT, addWorker, saveWorkers);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.setColspan(fatherNameT, 2);
        formLayout.setMaxWidth("500px");
        add(formLayout);

//объявление полей для таблицы со списком соттудников
        ValidationName firstName = new ValidationName();
        ValidationName lastName = new ValidationName();
        ValidationName fatherName = new ValidationName();
        ValidationName cat = new ValidationName();
//добавление столбцов
        Grid.Column<Worker> lastNameColumn = grid
                .addColumn(Worker::getLastName).setHeader("Фамилия")
                .setAutoWidth(true).setFlexGrow(1);
        Grid.Column<Worker> firstNameColumn = grid.addColumn(Worker::getFirstName)
                .setHeader("Имя").setAutoWidth(true).setFlexGrow(1);
        Grid.Column<Worker> fatherNameColumn = grid.addColumn(Worker::getFatherName)
                .setHeader("Отчество").setAutoWidth(true).setFlexGrow(1);
        Grid.Column<Worker> catColumn = grid.addColumn(Worker::getCategory)
                .setHeader("Категория").setAutoWidth(true).setFlexGrow(1);

//столбец для изменения сотрудников в таблице. После изменения нужно глобально сохранить состояние бригады через кнопку "Сохранить состав бригады" (saveButton)
        Grid.Column<Worker> editColumn = grid.addComponentColumn(worker -> {
            Button editButton = new Button("Изменить");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(worker);
            });
            return editButton;
        }).setWidth("120px").setFlexGrow(1);

        //объявление привязки изменяемого поля к объявляемому
        Binder<Worker> binder = new Binder<>(Worker.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
//при изменении фамилии
        TextField lastNameField = new TextField();
        lastNameField.setWidthFull();
        binder.forField(lastNameField)
                .asRequired("Фамилия не может быть пустой")
                .withStatusLabel(lastName)
                .bind(Worker::getLastName, Worker::setLastName);
        lastNameColumn.setEditorComponent(lastNameField);
//при изменении имени
        TextField firstNameField = new TextField();
        firstNameField.setWidthFull();
        binder.forField(firstNameField).asRequired("Имя не может быть пустым")
                .withStatusLabel(firstName)
                .bind(Worker::getFirstName, Worker::setFirstName);
        firstNameColumn.setEditorComponent(firstNameField);
//при изменении отчества
        TextField fatherNameField = new TextField();
        fatherNameField.setWidthFull();
        binder.forField(fatherNameField).asRequired("Отчество не может быть пустым")
                .withStatusLabel(fatherName)
                .bind(Worker::getFatherName, Worker::setFatherName);
        fatherNameColumn.setEditorComponent(fatherNameField);
//при изменении категории
        TextField catField = new TextField();
        catField.setWidthFull();
        binder.forField(catField).asRequired("Категория не может быть пустой")
                .withStatusLabel(cat)
                .bind(Worker::getCategory, Worker::setCategory);
        catColumn.setEditorComponent(catField);
//объявление и конфигурация кнопки дял сохранения (или отмены) изменений в данных сотрудника
        Button saveButton = new Button("Сохранить", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
        editor.addCancelListener(e -> {
            firstName.setText("");
            lastName.setText("");
            fatherName.setText("");
            cat.setText("");
        });

        getThemeList().clear();
        add(grid, firstName, lastName, fatherName, cat);
    }

}