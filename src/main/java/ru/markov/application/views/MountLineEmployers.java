package ru.markov.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.Worker;

import java.util.ArrayList;
import java.util.List;

@Route(value = "mounts_employers", layout = MainLayout.class)
@PageTitle("BrigApp א Работники на волне")
@RolesAllowed("BRIGMOUNT")
@UIScope
public class MountLineEmployers extends Div {
    public static List<Worker> workerAtLineMounts = new ArrayList<>();

}
