package ru.markov.application.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;




@Route(value ="who_next", layout = GuestLayout.class)
@AnonymousAllowed
public class WhoNext extends VerticalLayout {
    public WhoNext() {


    }
}

