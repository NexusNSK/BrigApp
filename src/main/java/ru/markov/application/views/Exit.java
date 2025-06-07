package ru.markov.application.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.security.SecurityService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Route(value = "exit", layout = MainLayout.class)
@PageTitle("BrigApp א Отпуска")
@PermitAll
@JavaScript("./vacation-calendar.js")
public class Exit extends VerticalLayout {
    public Exit(SecurityService securityService) throws IOException {
        VerticalLayout layout = new VerticalLayout();
        /*
        if (!securityService.getAuthenticatedUser().getUsername().equals("admin") & ServiceTools.serviceFlag) {
            TextArea serviceMessage = new TextArea();
            serviceMessage.setMinWidth("500px");
            serviceMessage.setMaxWidth("500px");
            serviceMessage.setReadOnly(true);
            serviceMessage.setLabel("Внимание");
            serviceMessage.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
            serviceMessage.setValue("Извините, идут сервисные работы.\nПовторите попытку позже.");
            add(serviceMessage);
        } else {
            add(loadPlanOtp(Files.readAllBytes(Path.of("src/main/resources/images/otpusk.png")), "plan"));
        }
    }

    public Image loadPlanOtp(byte[] bytes, String alt) {
        StreamResource sr = new StreamResource("otp", (InputStreamFactory) () -> new ByteArrayInputStream(bytes));
        Image plan = new Image(sr, alt);
        plan.setWidth("100%");
        return plan;
    }

        */
        layout.setWidthFull();
        layout.setHeight("850px");
        IFrame iframe = new IFrame("https://eltex.loc/vacation/");
        iframe.setWidthFull();
        iframe.setHeightFull();
        add(layout);
        layout.add(iframe);
    }
}
