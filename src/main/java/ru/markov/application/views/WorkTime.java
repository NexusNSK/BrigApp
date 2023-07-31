package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Serial;
import ru.markov.application.data.Worker;

import java.time.LocalDate;

@PermitAll
@Route(value = "worktime", layout = MainLayout.class)


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
        save.addClickListener(buttonClickEvent -> {
            Serial.save();
            System.out.println("Рабочее время было записано");
        });

        Grid.Column<Worker> fullNameColumn = workTimeGrid
                .addColumn(Worker::getFullName)
                .setHeader("ФИО сотрудника")
                .setAutoWidth(true)
                .setFlexGrow(1);
        Grid.Column<Worker> workTimeColumn = workTimeGrid
                .addColumn(Worker::getWorkTime)
                .setHeader("Время")
                .setAutoWidth(true)
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


        add(workTimeDatePicker, save, workTimeGrid);
    }
}
//нужно добавить редактирование столбцов через статическое неизменяемое поле, управляемое кнопками + -
// перегрузить метод мапы под значение integer parse int
//worktime раздел недоступен под юзером и ломает доступ всем, разобраться



