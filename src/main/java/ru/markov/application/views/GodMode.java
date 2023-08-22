package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;


@Route(value = "god_mode", layout = MainLayout.class)
@PageTitle("BrigApp א GOD MODE")
@RolesAllowed("OWNER")
public class GodMode extends VerticalLayout {

    public GodMode(){
        Button button = new Button("ewrwer", new Icon(VaadinIcon.PUZZLE_PIECE));
        add(button);

    }
}
