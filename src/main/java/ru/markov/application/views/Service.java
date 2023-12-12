package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Worker;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.JsonConverter;
import java.io.IOException;


@Route(value = "service", layout = MainLayout.class)
@PageTitle("BrigApp א Сервисное меню")
@PermitAll
public class Service extends VerticalLayout {

    public Service(SecurityService securityService) {
        if (securityService.getAuthenticatedUser().getUsername().equals("admin")) {
            Button downloadJson = new Button("Создать JSON работников", new Icon(VaadinIcon.PUZZLE_PIECE));
            downloadJson.addClickListener(event -> {
                try {
                    JsonConverter.toJSON(GridEdit.workerList);
                    Notification n = Notification.show("Json создан и лежит в корне приложения (на сервере)");
                    n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    n.setPosition(Notification.Position.MIDDLE);
                } catch (IOException e) {
                    Notification error = Notification.show("Что-то пошло не так");
                    error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    error.setPosition(Notification.Position.MIDDLE);
                }
            });

            Button eraseAllTime = new Button("Стереть табели всем сотрудникам", new Icon(VaadinIcon.ERASER));
            eraseAllTime.addClickListener(event -> eraseAllWorkTimes());
            add(downloadJson, eraseAllTime);

        } else {
            Notification notification = Notification.show("У вас нет доступа к этой странице");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
        }
    }

        public static void eraseAllWorkTimes() {
            System.out.println("Начата процедура очистки табелей");
            for (Worker w : GridEdit.workerList) {
                w.eraseAllMassive();
                w.initWorkTimeMap();
                w.initWorkerStatusMap();
            }
            Notification erase = Notification.show("Табели сотрудников были стёрты");
            erase.setPosition(Notification.Position.MIDDLE);
            erase.addThemeVariants(NotificationVariant.LUMO_WARNING);

        }
    }


