package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Serial;
import ru.markov.application.data.ValidationName;
import ru.markov.application.data.Worker;

import java.time.LocalDate;

@PermitAll
@Route(value = "worktime", layout = MainLayout.class)

@UIScope
public class WorkTime extends Div {
    public static DatePicker workTimeDatePicker = new DatePicker();
    private Button save = new Button("Записать время");

    public WorkTime() {
        workTimeDatePicker.setValue(LocalDate.now());

        Grid<Worker> workTimeGrid = new Grid<>(Worker.class, false);
        workTimeGrid.setItems(GridEdit.workerList);
        Editor<Worker> editor = workTimeGrid.getEditor();
        Binder<Worker> binder = new Binder<>(Worker.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        workTimeDatePicker.addClientValidatedEventListener(clientValidatedEvent -> workTimeGrid.getDataProvider().refreshAll());
        save.addClickListener(buttonClickEvent -> {
            Serial.save();
            System.out.println("Рабочее время было записано");
            workTimeGrid.getDataProvider().refreshAll();
        });

        Grid.Column<Worker> fullNameColumn = workTimeGrid
                .addColumn(Worker::getFullName)
                .setHeader("ФИО сотрудника")
                .setAutoWidth(false)
                .setWidth("400px")
                .setFlexGrow(0);
        Grid.Column<Worker> workTimeColumn = workTimeGrid
                .addColumn(Worker::getWorkTime)
                .setHeader("Время")
                .setAutoWidth(false)
                .setWidth("200px")
                .setFlexGrow(1);
        Grid.Column<Worker> workerStatusColumn = workTimeGrid.
                addColumn(Worker::getWorkerStatus)
                .setHeader("Статус")
                .setAutoWidth(false)
                .setWidth("200px")
                .setFlexGrow(1);

        Grid.Column<Worker> editColumn = workTimeGrid.addComponentColumn(worker -> {
            Button editButton = new Button("Изменить");
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
            timeValid.setText("");
        });

        add(workTimeDatePicker, save, workTimeGrid);
    }
}
//worktime раздел недоступен под юзером и ломает доступ всем, разобраться



