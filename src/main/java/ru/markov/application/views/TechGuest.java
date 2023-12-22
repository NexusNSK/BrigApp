package ru.markov.application.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;



@Route(value = "visitor", layout = GuestLayout.class)
@AnonymousAllowed
public class TechGuest extends VerticalLayout {
    public TechGuest(){
        TextField textField = new TextField();
        textField.setValue("Здарова, Меченый! Ты бы ещё консервных банок насобирал...");
        textField.setWidthFull();

        add(textField);
    }

}


