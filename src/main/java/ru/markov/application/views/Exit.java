package ru.markov.application.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Route(value = "exit", layout = MainLayout.class)
@PageTitle("BrigApp א Отпуска")
@PermitAll

public class Exit extends VerticalLayout {
    public Exit() throws IOException {
        add(loadPlanOtp(Files.readAllBytes(Path.of("src/main/resources/images/otpusk.png")), "plan"));
            }
    public Image loadPlanOtp(byte[] bytes, String alt){
        StreamResource sr = new StreamResource("otp", new InputStreamFactory() {
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
