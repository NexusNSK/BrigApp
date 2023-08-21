package ru.markov.application.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jdk.jfr.Percentage;
import ru.markov.application.data.Worker;

@Route(value = "line_editor", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class LineEditor extends Div {
    Grid<Worker> lineEditGrid = new Grid<>();
    ComboBox<String> brig = new ComboBox<>();

    public LineEditor(){
        brig.setItems("Бригада монтажники", "Бригада сборщики");
        add(brig, lineEditGrid);
    }
}
