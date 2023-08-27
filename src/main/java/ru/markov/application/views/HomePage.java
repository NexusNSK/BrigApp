package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

@Route(value = "homepage", layout = MainLayout.class)
@PermitAll
public class HomePage extends VerticalLayout {

    public HomePage(){
        add(new Button(new Icon(VaadinIcon.ABACUS)));
    }
}
