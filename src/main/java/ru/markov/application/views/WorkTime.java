package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabVariant;
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
import java.util.List;
import java.util.Objects;

@PermitAll
@Route(value = "worktime", layout = MainLayout.class)
@PageTitle("BrigApp א Учёт времени")
@UIScope
public class WorkTime extends Div {
    public DatePicker workTimeDatePicker = new DatePicker();

    public WorkTime(SecurityService securityService) {
        String username = securityService.getAuthenticatedUser().getUsername();
        System.out.println(username);
        workTimeDatePicker.setValue(LocalDate.now());
        Grid<Worker> workTimeGrid = new Grid<>(Worker.class, false);
        workTimeGrid.setMinHeight("800px");
        setItemforGrid(username, workTimeGrid);



        Editor<Worker> editor = workTimeGrid.getEditor();
        Binder<Worker> binder = new Binder<>(Worker.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        workTimeDatePicker.addClientValidatedEventListener(clientValidatedEvent ->{
            TimeAdapter.workTimeDatePicker.setValue(workTimeDatePicker.getValue());
                workTimeGrid.getDataProvider().refreshAll();
        });
        Button save = new Button("Записать время");
        save.addClickListener(buttonClickEvent -> {
            Serial.save();
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
        fullNameColumn.setFooter("Сотрудников: " + GridEdit.workerList.size());

        Grid.Column<Worker> workTimeColumn = workTimeGrid
                .addColumn(Worker::getWorkTime)
                .setHeader("Время")
                .setAutoWidth(false)
                .setResizable(true)
                .setWidth("200px")
                .setFlexGrow(1);

        Grid.Column<Worker> workerStatusColumn = workTimeGrid.
                addColumn(Worker::getWorkerStatusMassive)
                .setHeader("Статус")
                .setAutoWidth(false)
                .setResizable(true)
                .setWidth("200px")
                .setFlexGrow(1);

        Grid.Column<Worker> editColumn = workTimeGrid.addComponentColumn(worker -> {
            Button editButton = new Button("Изменить", new Icon(VaadinIcon.EDIT));
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                workTimeGrid.getEditor().editItem(worker);
            });
            return editButton;
        }).setWidth("120px").setFlexGrow(1);


        IntegerField setTimeEdit = new IntegerField();
        ValidationName timeValid = new ValidationName();
        setTimeEdit.setValue(8);
        setTimeEdit.setStepButtonsVisible(true);
        setTimeEdit.setMin(0);
        setTimeEdit.setMax(12);
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
        setTimeEdit.setEnabled(!Objects.equals(statusEditColumn.getValue(), "Работает"));
        statusEditColumn.setWidthFull();
        binder.forField(statusEditColumn)
                .asRequired()
                .withStatusLabel(statusValid)
                .bind(Worker::getWorkerStatusMassive, Worker::setWorkerStatusMassive);
        workerStatusColumn.setEditorComponent(statusEditColumn);


        Button saveButton = new Button("Сохранить", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
        editor.addCancelListener(e -> timeValid.setText(""));

        add(workTimeDatePicker, save, workTimeGrid);
    }

    public void setItemforGrid(String sc, Grid <Worker> grid){
        switch (sc) {
            case "admin" -> grid.setItems(GridEdit.workerList);
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
}




