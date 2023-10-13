package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.service.JsonConverter;
import java.io.IOException;


@Route(value = "god_mode", layout = MainLayout.class)
@PageTitle("BrigApp א GOD MODE")
@RolesAllowed("OWNER, ADMIN")
public class GodMode extends VerticalLayout {

    public GodMode() {
        Button button = new Button("Выгрузить JSON работников", new Icon(VaadinIcon.PUZZLE_PIECE));
        button.addClickListener(event -> {
            try {
                JsonConverter.toJSON(GridEdit.workerList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button sortList = new Button("Сортировать списки", new Icon(VaadinIcon.ABACUS));



        add(button, sortList);

    }

}

