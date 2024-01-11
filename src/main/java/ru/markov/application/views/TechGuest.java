package ru.markov.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.spire.pdf.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Route(value = "plan", layout = GuestLayout.class)
@AnonymousAllowed
public class TechGuest extends VerticalLayout {
    public TechGuest() throws IOException {

        StreamResource source = new StreamResource("volna", () ->
                getClass().getClassLoader()
                        .getResourceAsStream("images/volna.png")
        );
        Image plan = new Image(source, "volna");
        plan.setWidth("100%");
        add(plan);
    }
}




