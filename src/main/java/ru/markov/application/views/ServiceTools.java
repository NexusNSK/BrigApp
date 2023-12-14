package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.io.FileUtils;
import ru.markov.application.data.Worker;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.JsonConverter;
import ru.markov.application.service.Serial;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Route(value = "service", layout = MainLayout.class)
@PageTitle("BrigApp א Сервичный раздел")
@PermitAll
public class ServiceTools extends VerticalLayout {
    public ServiceTools(SecurityService securityService) {
        if (securityService.getAuthenticatedUser().getUsername().equals("admin")) {

            //Загрузка файла worker list на сервер из браузера

            FileBuffer fileBuffer = new FileBuffer();
            Upload singleFileUpload = new Upload(fileBuffer);
            int maxFileSizeInBytes = 50 * 1024 * 1024; // 50MB
            singleFileUpload.setMaxFileSize(maxFileSizeInBytes);

            TextArea instructArea = new TextArea();
            instructArea.setMinWidth("500px");
            instructArea.setMaxWidth("1500px");
            instructArea.setReadOnly(true);
            instructArea.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
            instructArea.setValue("Выберите файл с бэкапом для восстановления");

            singleFileUpload.addSucceededListener(event -> {
                FileData savedFileData = fileBuffer.getFileData();
                String absolutePath = savedFileData.getFile().getAbsolutePath();
                System.out.printf("Файл сохранён по пути: %s%n", absolutePath);
                try {
                    FileUtils.moveFile(new File(absolutePath), new File("worker list.bin"));
                } catch (IOException e) {
                    System.out.println("Во время перемещения файла возникла непредвиденная ошибка. Попробуйте ещё раз, либо замените файл вручную.");
                }
                try {
                    GridEdit.workerList.clear();
                    Serial.load();
                } catch (ClassNotFoundException e) {
                    System.out.println("Во время загрузки данных из файла произошла ошибка");
                }
                GridEdit.initSplitDistrictWorkersList();
            });
            singleFileUpload.addFileRejectedListener(event -> {
                String errorMessage = event.getErrorMessage();

                Notification notification = Notification.show(errorMessage, 5000,
                        Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);});


            //Добавлена возможность выгрузки json всех работников с учетом их текущих табелей (на сервер)
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

            //эта кнопка затирает табели и пересоздает их с нуля для всех сотрудников
            Button eraseAllTime = new Button("Стереть табели всем сотрудникам", new Icon(VaadinIcon.ERASER));
            eraseAllTime.addClickListener(event -> eraseAllWorkTimes());

        //Тут реализована возможность скачать worker list.bin в браузере
            Anchor download = new Anchor(new StreamResource(
                    "worker list (" + LocalDateTime.now().getDayOfMonth()+" "+ LocalDateTime.now().getMonth() + ").bin",
                    () -> {
                        try {
                            return new FileInputStream("worker list.bin");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }), "");
            download.getElement().setAttribute("download", true);
            download.add(new Button("Выгрузить \"worker list.bin\"",new Icon(VaadinIcon.DOWNLOAD_ALT)));


            //добавляем кнопки в интерфейс приложения
            add(downloadJson, eraseAllTime, download, instructArea ,singleFileUpload);

        } else {
            Notification notification = Notification.show("У вас нет доступа к этой странице");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.MIDDLE);
        }
    }


    //метод, в котором вся логика для затирки всех табелей
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


