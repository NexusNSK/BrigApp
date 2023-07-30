package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "Учёт времени", layout = MainLayout.class)


public class WorkTime extends Div {
    public WorkTime() {
        DatePicker workTimeDatePicker = new DatePicker();
        Button save = new Button("save");


    }
}



