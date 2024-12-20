package ru.markov.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.context.annotation.Lazy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@Route(value = "plan", layout = GuestLayout.class)
@AnonymousAllowed
@Lazy
public class TechGuest extends VerticalLayout {
    public TechGuest() throws IOException {
        add(loadPlan(Files.readAllBytes(Path.of("src/main/resources/images/volna.png")), "plan"));
    }

    public Image loadPlan(byte[] bytes, String alt){
        StreamResource sr = new StreamResource("isr", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(bytes);
            }
        });
        Image plan = new Image(sr, alt);
        plan.setWidth("100%");
        return plan;

    }
}




