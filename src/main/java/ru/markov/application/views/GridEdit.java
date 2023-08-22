package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.*;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.ConveyLine;
import ru.markov.application.service.Serial;
import com.vaadin.flow.component.dialog.Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** GridEdit.java класс реализует интерфейс веб страницы для управления составом бригад.
 * Состав хранится в публичном статичной коллекции GridEdit.workerList, что позволяет обратиться
 * к ней из любой точки программы для взаимодействия с данными.
 * GridEdit.workerList является источником данных для публичных статических коллеций
 * GridEdit.mountList, GridEdit.builderList и GridEdit.builderList,
 * которые хранят в себе отсортированные по enum District.java (Участок) объекты Worker.
 * Коллекция сериализуется когда наступает одно из событий:
 * 1. Worker был успешно добавлен. При этом используется конструктор вида
 * (String lastName, String firstName, String patronymic, String district, String post, String category),
 * и нажата кнопка "Сохранить изменения" (Button saveWorkers |188 строка|).
 * 2. Данные Worker были валидно изменены и сохранены нажатием кнопки "Сохранить" (Button saveButton |379 строка|).
 * В момент запуска приложения проихсодит десериализация из worker list.bin в корне приложения.
 * Объект Worker можно удалять из GridEdit.workerList путём нажатия кнопки "Удалить" (Button dialogDeleteButton |286 строка|.
 */

@Route(value = "grid_edit", layout = MainLayout.class)
@PageTitle("BrigApp א Редактор бригады")
@RolesAllowed("ADMIN")
@UIScope

public class GridEdit extends Div {

    //в этой коллекции хранятся сохраняемые сотрудники, используется для загрузки данных при старте приложения
    public static List<Worker> workerList = new ArrayList<>();
    public static HashMap<ConveyLine, List<Worker>> mountMap = new HashMap<>();
    public static HashMap<ConveyLine, List<Worker>> builderMap = new HashMap<>();
    public static List<Worker> techList = new ArrayList<>();


    public void initSplitDistrictWorkersList(){
        mountMap.clear();
        builderMap.clear();
        techList.clear();
        startInitSplitMap(mountMap);
        startInitSplitMap(builderMap);
        for (Worker w : workerList) {
            switch (w.getDistrictToString()){
                case "Бригада монтажники" -> {
                    switch (w.getLine()){
                        case "1": mountMap.get(ConveyLine.LINE_1).add(w);
                        case "2": mountMap.get(ConveyLine.LINE_2).add(w);
                        case "3": mountMap.get(ConveyLine.LINE_3).add(w);
                        case "4": mountMap.get(ConveyLine.LINE_4).add(w);
                        default: mountMap.get(ConveyLine.COMMON).add(w);
                    }
                }
                case "Бригада сборщики" -> {
                    switch (w.getLine()){
                        case "1": builderMap.get(ConveyLine.LINE_1).add(w);
                        case "2": builderMap.get(ConveyLine.LINE_2).add(w);
                        case "3": builderMap.get(ConveyLine.LINE_3).add(w);
                        case "4": builderMap.get(ConveyLine.LINE_4).add(w);
                        default: builderMap.get(ConveyLine.COMMON).add(w);
                    }
                }
                case "Бригада техники" -> techList.add(w);
            }
        }
    }

    private void startInitSplitMap(HashMap<ConveyLine, List<Worker>> map) {
        map.put(ConveyLine.COMMON, new ArrayList<>());
        map.put(ConveyLine.LINE_1, new ArrayList<>());
        map.put(ConveyLine.LINE_2, new ArrayList<>());
        map.put(ConveyLine.LINE_3, new ArrayList<>());
        map.put(ConveyLine.LINE_4, new ArrayList<>());
    }



    public GridEdit(SecurityService securityService) {

        Grid<Worker> grid = new Grid<>(Worker.class, false); //основная таблица с сотрудниками
        grid.setItems(workerList);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        initSplitDistrictWorkersList();
        TextField firstNameT = new TextField("Имя"); //поле ввода имени при добавлении сотрудника
        TextField lastNameT = new TextField("Фамилия"); //поле ввода фамилии при добавлении сотрудника
        TextField fatherNameT = new TextField("Отчество"); //поле ввода отчества при добавлении сотрудника
        ComboBox<String> districtBox = new ComboBox<>("Участок"); //поле выбора участка (волна, сборка, техники)
        ComboBox<String> lineBox = new ComboBox<>("Линия");
        lineBox.setItems("1", "2", "3", "4", "Не распределено");
        districtBox.setAllowCustomValue(true);
        districtBox.setItems(
                "Бригада монтажники",
                "Бригада сборщики",
                "Бригада техники"
        );
        ComboBox<String> postBox = new ComboBox<>("Должность"); //поле выбора должности (бригадир, монтажник, сборщик, техник)
        postBox.setAllowCustomValue(true);
        postBox.setItems(
                "Бригадир монтажников",
                "Бригадир сборщиков",
                "Бригадир техников",
                "Монтажник",
                "Сборщик",
                "Техник"
        );

        grid.setMinHeight("1000px");

        Editor<Worker> editor = grid.getEditor();



        //блок добавления сотрудника во временный список без глобального сохранения
        Button addWorker = new Button("Добавить сотрудника", new Icon(VaadinIcon.AUTOMATION));
        addWorker.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
        addWorker.addClickListener(buttonClickEvent -> {
            if (!(firstNameT.getValue().equals(""))
                    && !(lastNameT.getValue().equals(""))
                    && !(fatherNameT.getValue().equals(""))
                    && !(districtBox.getValue() == null)
                    && !(postBox.getValue() == null)) {
                workerList.add(new Worker(
                        lastNameT.getValue(),
                        firstNameT.getValue(),
                        fatherNameT.getValue(),
                        lineBox.getValue(),
                        districtBox.getValue(),
                        postBox.getValue()));
                Notification notification = Notification
                        .show("Сотрудник был добавлен!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.MIDDLE);
                firstNameT.clear();
                lastNameT.clear();
                fatherNameT.clear();
                districtBox.clear();
                postBox.clear();
                initSplitDistrictWorkersList(); //
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
        instructArea.setMaxWidth("1500px");
        instructArea.setReadOnly(true);
        instructArea.setLabel("Подсказки по добавлению и редактированию списка");
        instructArea.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
        instructArea.setValue("ВСЕ поля должны быть заполнены. Поля с выпадающими списками " +
                "должны иметь значения, которые предлагает программа. После заполнения нажать \"Добавить сотрудника\". Проверить, что сотрудник" +
                "есть в списке и нажать \"Сохранить изменения\"");

        //объявление формы, отвечающей за добавление сотрудников и сохранения бригады
        FormLayout formToAddWorkers = new FormLayout();
        formToAddWorkers.add(lastNameT, firstNameT, fatherNameT, lineBox, districtBox, postBox, addWorker, saveWorkers, instructArea);
        formToAddWorkers.setResponsiveSteps(
                new FormLayout.ResponsiveStep("600px", 3),
                new FormLayout.ResponsiveStep("1500px", 6));
        formToAddWorkers.setColspan(instructArea, 4);
        formToAddWorkers.setMaxWidth("1700px");
        add(formToAddWorkers);

        HorizontalLayout topHead = new HorizontalLayout();


        //объявление полей для таблицы со списком соттудников
        ValidationName firstNameValid = new ValidationName();
        ValidationName lastNameValid = new ValidationName();
        ValidationName fatherNameValid = new ValidationName();
        ValidationName districtValid = new ValidationName();
        ValidationName postValid = new ValidationName();
        ValidationName lineValid = new ValidationName();

        //добавление столбцов
        Grid.Column<Worker> lastNameColumn = grid
                .addColumn(Worker::getLastName)
                .setTextAlign(ColumnTextAlign.START)
                .setHeader("Фамилия")
                .setSortable(true)
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);
        Grid.Column<Worker> firstNameColumn = grid
                .addColumn(Worker::getFirstName)
                .setTextAlign(ColumnTextAlign.START)
                .setHeader("Имя")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);
        Grid.Column<Worker> fatherNameColumn = grid
                .addColumn(Worker::getPatronymic)
                .setTextAlign(ColumnTextAlign.START)
                .setHeader("Отчество")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);
        Grid.Column<Worker> lineColumn = grid
                .addColumn(Worker::getLine)
                .setHeader("Линия")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);
        Grid.Column<Worker> districtColumn = grid
                .addColumn(Worker::getDistrictToString)
                .setHeader("Участок")
                .setSortable(true)
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);
        Grid.Column<Worker> postColumn = grid
                .addColumn(Worker::getPost)
                .setHeader("Должность")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);


        //столбец для изменения сотрудников в таблице. После изменения нужно глобально сохранить
        // состояние бригады через кнопку "Сохранить состав бригады" (saveButton)
        Grid.Column<Worker> editColumn = grid.addComponentColumn(worker -> {
            Button editButton = new Button("Изменить", new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(worker);
            });
            return editButton;
        }).setWidth("120px").setFlexGrow(1);

        Grid.Column<Worker> deleteColumn = grid.addComponentColumn(worker -> {
            Dialog dialog = new Dialog();
            Button deleteButton = new Button("", new Icon(VaadinIcon.TRASH), d -> dialog.open());

            dialog.setHeaderTitle(
                    String.format("Удалить сотрудника \"%s\"?", worker.getFullName()));
            dialog.add("Вы уверены, что хотите удалить сотрудника из списка бригады?");

            Button dialogDeleteButton = new Button("Удалить", new Icon(VaadinIcon.TRASH), (t) ->{
                workerList.remove(worker);
                initSplitDistrictWorkersList();
                dialog.close();
                grid.getDataProvider().refreshAll();
            });
            dialogDeleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_ERROR);
            deleteButton.getStyle().set("margin-right", "auto");
         //   dialog.getFooter().add(deleteButton);

            Button cancelButton = new Button("Отмена", (t) -> dialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(cancelButton);
            dialog.getFooter().add(dialogDeleteButton);

            return deleteButton;
        }).setWidth("120px").setFlexGrow(1);
        deleteColumn.setVisible(true);

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

        //при изменении линии
        ComboBox<String> lineEditCol = new ComboBox<>();
        lineEditCol.setItems("Не распределено", "1", "2", "3", "4");
        lineEditCol.setWidthFull();
        binder.forField(lineEditCol)
                .withStatusLabel(lineValid)
                .bind(Worker::getLine, Worker::setLine);
        lineColumn.setEditorComponent(lineEditCol);

        //при изменении участка
        ComboBox<String> districtEditCol = new ComboBox<>();
        districtEditCol.setItems(
                "Бригада монтажники",
                "Бригада сборщики",
                "Бригада техники"
        );
        districtEditCol.setWidthFull();
        binder.forField(districtEditCol)
                .asRequired("Участок не может быть пустым")
                .withStatusLabel(districtValid)
                .bind(Worker::getDistrictToString, Worker::setDistrict);
        districtColumn.setEditorComponent(districtEditCol);

        //при изменении должности
        ComboBox<String> postEditCol = new ComboBox<>();
        postEditCol.setItems(
                "Бригадир монтажников",
                "Бригадир сборщиков",
                "Бригадир техников",
                "Монтажник",
                "Сборщик",
                "Техник"
        );
        postEditCol.setWidthFull();
        binder.forField(postEditCol)
                .asRequired("Должность не может быть пустой")
                .withStatusLabel(postValid)
                .bind(Worker::getPost, Worker::setPost);
        postColumn.setEditorComponent(postEditCol);

        //при изменении категории
        ComboBox<String> categoryEditCol = new ComboBox<>();
        categoryEditCol.setItems(
                "Бригадир",
                "1", "2", "3",
                "Испытательный срок"
        );

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
                .bind(Worker::getPatronymic, Worker::setPatronymic);
        fatherNameColumn.setEditorComponent(fatherNameField);


        //объявление и конфигурация кнопки дял сохранения (или отмены) изменений в данных сотрудника
        Button saveButton = new Button("Сохранить", e -> {
                editor.save();
        initSplitDistrictWorkersList();}
        );
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

        //скрываем доступ к полям и кнопкам для user
        if (!(securityService.getAuthenticatedUser().getUsername().equals("admin"))){
            addWorker.setEnabled(false);
            saveWorkers.setEnabled(false);
            editColumn.setVisible(false);
            deleteColumn.setVisible(false);
        }

        //getThemeList().clear();
        add(topHead, grid, firstNameValid, lastNameValid, fatherNameValid);
    }

}


