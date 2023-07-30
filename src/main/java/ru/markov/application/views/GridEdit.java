package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.*;

import java.util.ArrayList;
import java.util.List;

@Route(value = "grid_workers", layout = MainLayout.class)
@PermitAll
public class GridEdit extends Div {

    //в этой коллекции хранятся сохраняемые сотрудники, используется для загрузки данных при старте приложения
    public static List<Worker> workerList = new ArrayList<>();


    public GridEdit() {
        TextField firstNameT = new TextField("Имя"); //поле ввода имени при добавлении сотрудника
        TextField lastNameT = new TextField("Фамилия"); //поле ввода фамилии при добавлении сотрудника
        TextField fatherNameT = new TextField("Отчество"); //поле ввода отчества при добавлении сотрудника
        ComboBox<String> districtBox = new ComboBox<>("Участок"); //поле выбора участка (волна, сборка, техники)
        districtBox.setAllowCustomValue(true);
        districtBox.setItems("Бригада монтажники", "Бригада сборщики", "Бригада техники");
        ComboBox<String> postBox = new ComboBox<>("Должность"); //поле выбора должности (бригадир, монтажник, сборщик, техник)
        postBox.setAllowCustomValue(true);
        postBox.setItems("Бригадир монтажников", "Бригадир сборщиков", "Бригадир техников", "Монтажник", "Сборщик","Техник");
        ComboBox<String> categoryBox = new ComboBox<>("Категория");//поле выборп категории (1, 2, 3. испытательный срок)
        categoryBox.setAllowCustomValue(true);
        categoryBox.setItems("Бригадир", "1", "2", "3", "Испытательный срок");


        Grid<Worker> grid = new Grid<>(Worker.class, false); //основная таблица с сотрудниками
        grid.setMinHeight("1000px");

        Editor<Worker> editor = grid.getEditor();
        grid.setItems(workerList);

        //блок добавления сотрудника во временный список без глобального сохранения
        Button addWorker = new Button("Добавить сотрудника", new Icon(VaadinIcon.AUTOMATION));
        addWorker.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
        addWorker.addClickListener(buttonClickEvent -> {
            if (!(firstNameT.getValue().equals(""))
                    && !(lastNameT.getValue().equals(""))
                    && !(fatherNameT.getValue().equals(""))
                    && !(districtBox.getValue() == null)
                    && !(postBox.getValue() == null)
                    && !(categoryBox.getValue() == null)) {
                workerList.add(new Worker(firstNameT.getValue(),
                        lastNameT.getValue(),
                        fatherNameT.getValue(),
                        districtBox.getValue(),
                        postBox.getValue(),
                        categoryBox.getValue()));
                Notification notification = Notification
                        .show("Сотрудник был добавлен!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.MIDDLE);
                firstNameT.clear();
                lastNameT.clear();
                fatherNameT.clear();
                districtBox.clear();
                postBox.clear();
                categoryBox.clear();
                grid.getDataProvider().refreshAll();
            } else {
                Notification notification = Notification
                        .show("Сотрудник не был добавлен!\nНеобходимо заполнить все поля.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
            }
        });

        //блок глобального сохранения состояния бригады
        Button saveWorkers = new Button("Сохранить изменения", new Icon(VaadinIcon.HANDSHAKE));
        saveWorkers.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        saveWorkers.addClickListener(buttonClickEvent -> {
            Serial.save();
            Notification brigNote = Notification.show("Состав бригады сохранён");
            brigNote.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            brigNote.setPosition(Notification.Position.MIDDLE);
        });

        //объявление формы, отвечающей за вывод инструкции
        TextArea instructArea = new TextArea();
        instructArea.setMinWidth("500px");
        instructArea.setMaxWidth("1000px");
        instructArea.setReadOnly(true);
        instructArea.setLabel("Подсказки по добавлению и редактированию списка");
        instructArea.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());

        //объявление формы, отвечающей за добавление сотрудников и сохранения бригады
        FormLayout formToAddWorkers = new FormLayout();
        formToAddWorkers.add(lastNameT, firstNameT, fatherNameT, districtBox, postBox, categoryBox, addWorker, saveWorkers);
        formToAddWorkers.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        formToAddWorkers.setColspan(fatherNameT, 2);
        formToAddWorkers.setColspan(saveWorkers, 2);
        formToAddWorkers.setMaxWidth("500px");
        add(formToAddWorkers);

        HorizontalLayout topHead = new HorizontalLayout();
        topHead.add(instructArea);


        //объявление полей для таблицы со списком соттудников
        ValidationName firstNameValid = new ValidationName();
        ValidationName lastNameValid = new ValidationName();
        ValidationName fatherNameValid = new ValidationName();
        ValidationName districtValid = new ValidationName();
        ValidationName postValid = new ValidationName();
        ValidationName categoryValid = new ValidationName();

        //добавление столбцов
        Grid.Column<Worker> lastNameColumn = grid
                .addColumn(Worker::getLastName)
                .setHeader("Фамилия")
                .setAutoWidth(true)
                .setFlexGrow(1);
        Grid.Column<Worker> firstNameColumn = grid
                .addColumn(Worker::getFirstName)
                .setHeader("Имя")
                .setAutoWidth(true)
                .setFlexGrow(1);
        Grid.Column<Worker> fatherNameColumn = grid
                .addColumn(Worker::getFatherName)
                .setHeader("Отчество")
                .setAutoWidth(true)
                .setFlexGrow(1);
        Grid.Column<Worker> districtColumn = grid
                .addColumn(Worker::getDistrict)
                .setHeader("Участок")
                .setAutoWidth(true)
                .setFlexGrow(1);
        Grid.Column<Worker> postColumn = grid
                .addColumn(Worker::getPost)
                .setHeader("Должность")
                .setAutoWidth(true)
                .setFlexGrow(1);
        Grid.Column<Worker> categoryColumn = grid
                .addColumn(Worker::getCategory)
                .setHeader("Категория")
                .setAutoWidth(true)
                .setFlexGrow(1);

        //столбец для изменения сотрудников в таблице. После изменения нужно глобально сохранить
        // состояние бригады через кнопку "Сохранить состав бригады" (saveButton)
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
                .withStatusLabel(lastNameValid)
                .bind(Worker::getLastName, Worker::setLastName);
        lastNameColumn.setEditorComponent(lastNameField);

        //при изменении участка
        ComboBox<String> districtEditCol = new ComboBox<>();
        districtEditCol.setItems("Бригада монтажники", "Бригада сборщики", "Бригада техники");
        districtEditCol.setWidthFull();
        binder.forField(districtEditCol)
                .asRequired("Участок не может быть пустым")
                .withStatusLabel(districtValid)
                .bind(Worker::getDistrict , Worker::setDistrict);
        districtColumn.setEditorComponent(districtEditCol);

        //при изменении должности
        ComboBox<String> postEditCol = new ComboBox<>();
        postEditCol.setItems("Бригадир монтажников", "Бригадир сборщиков", "Бригадир техников", "Монтажник", "Сборщик","Техник");
        postEditCol.setWidthFull();
        binder.forField(postEditCol)
                .asRequired("Должность не может быть пустой")
                .withStatusLabel(postValid)
                .bind(Worker::getPost, Worker::setPost);
        postColumn.setEditorComponent(postEditCol);

        //при изменении категории
        ComboBox<String> categoryEditCol = new ComboBox<>();
        categoryEditCol.setItems("Бригадир", "1", "2", "3", "Испытательный срок");
        categoryEditCol.setWidthFull();
        binder.forField(categoryEditCol)
                .asRequired("Категория не может быть пустой")
                .withStatusLabel(categoryValid)
                .bind(Worker::getCategory, Worker::setCategory);
        categoryColumn.setEditorComponent(categoryEditCol);

        //при изменении имени
        TextField firstNameField = new TextField();
        firstNameField.setWidthFull();
        binder.forField(firstNameField).asRequired("Имя не может быть пустым")
                .withStatusLabel(firstNameValid)
                .bind(Worker::getFirstName, Worker::setFirstName);
        firstNameColumn.setEditorComponent(firstNameField);

        //при изменении отчества
        TextField fatherNameField = new TextField();
        fatherNameField.setWidthFull();
        binder.forField(fatherNameField).asRequired("Отчество не может быть пустым")
                .withStatusLabel(fatherNameValid)
                .bind(Worker::getFatherName, Worker::setFatherName);
        fatherNameColumn.setEditorComponent(fatherNameField);


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
            firstNameValid.setText("");
            lastNameValid.setText("");
            fatherNameValid.setText("");
        });

        //getThemeList().clear();
        add(topHead, grid, firstNameValid, lastNameValid, fatherNameValid);
    }

}


