package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Worker;
import ru.markov.application.data.ValidationName;

@Route(value = "grid_workers", layout = MainLayout.class)
@PermitAll
public class GridEdit extends VerticalLayout {

    public GridEdit() {
        ValidationName firstName = new ValidationName();
        ValidationName lastName = new ValidationName();
        ValidationName fatherName = new ValidationName();


        Grid<Worker> grid = new Grid<>(Worker.class, false);
        Editor<Worker> editor = grid.getEditor();

        Grid.Column<Worker> lastNameColumn = grid
                .addColumn(Worker::getLastName).setHeader("Фамилия")
                .setAutoWidth(true).setFlexGrow(1);
        Grid.Column<Worker> firstNameColumn = grid.addColumn(Worker::getFirstName)
                .setHeader("Имя").setAutoWidth(true).setFlexGrow(1);
        Grid.Column<Worker> fatherNameColumn = grid.addColumn(Worker::getFatherName)
                .setHeader("Отчество").setAutoWidth(true).setFlexGrow(1);

        Grid.Column<Worker> editColumn = grid.addComponentColumn(worker -> {
            Button editButton = new Button("Изменить");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(worker);
            });
            return editButton;
        }).setWidth("120px").setFlexGrow(1);

        Binder<Worker> binder = new Binder<>(Worker.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField lastNameField = new TextField();
        lastNameField.setWidthFull();
        binder.forField(lastNameField)
                .asRequired("Фамилия не может быть пустой")
                .withStatusLabel(lastName)
                .bind(Worker::getLastName, Worker::setLastName);
        lastNameColumn.setEditorComponent(lastNameField);

        TextField firstNameField = new TextField();
        firstNameField.setWidthFull();
        binder.forField(firstNameField).asRequired("Имя не может быть пустым")
                .withStatusLabel(firstName)
                .bind(Worker::getFirstName, Worker::setFirstName);
        firstNameColumn.setEditorComponent(firstNameField);

        TextField fatherNameField = new TextField();
        fatherNameField.setWidthFull();
        binder.forField(fatherNameField).asRequired("Отчество не может быть пустым")
                .withStatusLabel(fatherName)
                .bind(Worker::getFatherName, Worker::setFatherName);
        fatherNameColumn.setEditorComponent(fatherNameField);

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
        });
        grid.setItems(BrigEditor.workerList);

        getThemeList().clear();
        add(grid, firstName, lastName, fatherName);
    }

}