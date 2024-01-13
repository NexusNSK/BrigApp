package ru.markov.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Route(value = "plan", layout = GuestLayout.class)
@AnonymousAllowed
public class TechGuest extends VerticalLayout {
    public TechGuest(){
        Image plan = new Image(loadPlan(), "volna.png");
        plan.setWidth("100%");
        add(plan);
    }
    public StreamResource loadPlan(){
        return new StreamResource("volna", () ->
                getClass().getClassLoader()
                        .getResourceAsStream("images/volna.png")
        );
    }
}




