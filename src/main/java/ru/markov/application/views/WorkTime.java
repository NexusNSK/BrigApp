package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.ConveyLine;
import ru.markov.application.service.Serial;
import ru.markov.application.data.ValidationName;
import ru.markov.application.data.Worker;
import ru.markov.application.service.TimeAdapter;
import java.time.LocalDate;


@PermitAll
@Route(value = "worktime", layout = MainLayout.class)
@PageTitle("BrigApp א Учёт времени")
@UIScope
public class WorkTime extends VerticalLayout {
    public DatePicker workTimeDatePicker = new DatePicker();

    public WorkTime(SecurityService securityService) {
        GridEdit.initSplitDistrictWorkersList();
        String username = securityService.getAuthenticatedUser().getUsername();

        workTimeDatePicker.setValue(LocalDate.now());
        Grid<Worker> workTimeGrid = new Grid<>(Worker.class, false);
        workTimeGrid.setMinHeight("500px");
        workTimeGrid.setWidth("800px");
        workTimeGrid.setHeight("800px");
        setItemforGrid(username, workTimeGrid);



        Editor<Worker> editor = workTimeGrid.getEditor();
        Binder<Worker> binder = new Binder<>(Worker.class);
        editor.setBinder(binder);
        editor.setBuffered(false);

        workTimeDatePicker.addClientValidatedEventListener(clientValidatedEvent ->{
            TimeAdapter.workTimeDatePicker.setValue(workTimeDatePicker.getValue());
                workTimeGrid.getDataProvider().refreshAll();
        });
        Button save = new Button("Записать время");
        save.addClickListener(buttonClickEvent -> {
            Serial.save();
            GridEdit.initSplitDistrictWorkersList();
            System.out.println("Рабочее время было записано");
            workTimeGrid.getDataProvider().refreshAll();
        });

        Grid.Column<Worker> fullNameColumn = workTimeGrid
                .addColumn(Worker::getFullName).setTextAlign(ColumnTextAlign.START)
                .setHeader("ФИО сотрудника")
                .setAutoWidth(false)
                .setResizable(true)
                .setSortable(true)
                .setWidth("400px")
                .setFlexGrow(0);
        switch (username){
            case "admin" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.workerList.size());
            case "volna1" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.mountMap.get(ConveyLine.LINE_1).size());
            case "volna2" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.mountMap.get(ConveyLine.LINE_2).size());
            case "volna3" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.mountMap.get(ConveyLine.LINE_3).size());
            case "volna4" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.mountMap.get(ConveyLine.LINE_4).size());
            case "sborka1" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.builderMap.get(ConveyLine.LINE_1).size());
            case "sborka2" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.builderMap.get(ConveyLine.LINE_2).size());
            case "sborka3" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.builderMap.get(ConveyLine.LINE_3).size());
            case "sborka4" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.builderMap.get(ConveyLine.LINE_4).size());
            case "tech" -> fullNameColumn.setFooter("Сотрудников: " + GridEdit.techList.size());
        }

        Grid.Column<Worker> workTimeColumn = workTimeGrid
                .addColumn(Worker::getWorkTime)
                .setHeader("Время")
                .setAutoWidth(false)
                .setResizable(true)
                .setWidth("100px")
                .setFlexGrow(1);

        Grid.Column<Worker> workerStatusColumn = workTimeGrid.
                addColumn(Worker::getWorkerStatusMassive)
                .setHeader("Статус")
                .setAutoWidth(false)
                .setResizable(true)
                .setWidth("200px")
                .setFlexGrow(1);


        IntegerField setTimeEdit = new IntegerField();
        ValidationName timeValid = new ValidationName();
        setTimeEdit.setValue(8);
        setTimeEdit.setStepButtonsVisible(true);
        setTimeEdit.setMin(0);
        setTimeEdit.setMax(12);
        addCloseHandler(setTimeEdit, editor);
        binder.forField(setTimeEdit)
                .asRequired()
                .withStatusLabel(timeValid)
                .bind(Worker::getWorkTime, Worker::setWorkTime);
        workTimeColumn.setEditorComponent(setTimeEdit);

        ComboBox<String> statusEditColumn = new ComboBox<>();
        ValidationName statusValid = new ValidationName();
        statusEditColumn.setItems("Работает (полный день)",
                "Работает (нестандартное время)",
                "Больничный",
                "Отпуск",
                "Не определено");
        statusEditColumn.setWidthFull();
        addCloseHandler(statusEditColumn, editor);
        binder.forField(statusEditColumn)
                .asRequired()
                .withStatusLabel(statusValid)
                .bind(Worker::getWorkerStatusMassive, Worker::setWorkerStatusMassive);
        workerStatusColumn.setEditorComponent(statusEditColumn);


        workTimeGrid.addItemDoubleClickListener(e -> {
            editor.editItem(e.getItem());

            Component editorComponent = e.getColumn().getEditorComponent();
            if (editorComponent instanceof Focusable) {
                ((Focusable<?>) editorComponent).focus();
            }
            GridEdit.initSplitDistrictWorkersList();
            System.out.println("update");
            Serial.save();
            System.out.println("save");
            workTimeGrid.getDataProvider().refreshAll();
        });



        editor.addCancelListener(e -> {
            timeValid.setText("");
            workTimeGrid.getDataProvider().refreshAll();});

        getThemeList().clear();
        getThemeList().add("spacing-l");
        add(workTimeDatePicker, save, workTimeGrid);
    }

    public void setItemforGrid(String sc, Grid <Worker> grid){
        switch (sc) {
            case "admin", "owner" -> grid.setItems(GridEdit.workerList);
            case "volna1" -> grid.setItems(GridEdit.mountMap.get(ConveyLine.LINE_1));
            case "volna2" -> grid.setItems(GridEdit.mountMap.get(ConveyLine.LINE_2));
            case "volna3" -> grid.setItems(GridEdit.mountMap.get(ConveyLine.LINE_3));
            case "volna4" -> grid.setItems(GridEdit.mountMap.get(ConveyLine.LINE_4));
            case "sborka1" -> grid.setItems(GridEdit.builderMap.get(ConveyLine.LINE_1));
            case "sborka2" -> grid.setItems(GridEdit.builderMap.get(ConveyLine.LINE_2));
            case "sborka3" -> grid.setItems(GridEdit.builderMap.get(ConveyLine.LINE_3));
            case "sborka4" -> grid.setItems(GridEdit.builderMap.get(ConveyLine.LINE_4));
            case "tech" -> grid.setItems(GridEdit.techList);
        }

    }
    private static void addCloseHandler(Component textField, Editor<Worker> editor) {
        textField.getElement()
                .addEventListener("keydown", e -> editor.cancel())
                .setFilter("event.code === 'Escape'");
    }
}




