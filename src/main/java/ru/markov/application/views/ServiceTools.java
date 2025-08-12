package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import ru.markov.application.data.Device;
import ru.markov.application.data.Worker;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.Serial;
import ru.markov.application.service.UploadRussianI18N;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;


@Route(value = "service", layout = MainLayout.class)
@PageTitle("BrigApp א Сервисный раздел")
@PermitAll
public class ServiceTools extends VerticalLayout {
    public static boolean serviceFlag = false;

    public ServiceTools(SecurityService securityService) {
        if (!securityService.getAuthenticatedUser().getUsername().equals("admin") & serviceFlag) {
            TextArea serviceMessage = new TextArea();
            serviceMessage.setMinWidth("500px");
            serviceMessage.setMaxWidth("500px");
            serviceMessage.setReadOnly(true);
            serviceMessage.setLabel("Внимание");
            serviceMessage.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
            serviceMessage.setValue("Извините, идут сервисные работы.\nПовторите попытку позже.");
            add(serviceMessage);
        } else {
            if (securityService.getAuthenticatedUser().getUsername().equals("admin")
                    || securityService.getAuthenticatedUser().getUsername().equals("tech")) {
                //Загрузка графика волны на сервер из браузера
                Button serviceButton = new Button();
                if (serviceFlag) {
                    serviceButton.setText("Выключить сервисное обслуживание");
                }else serviceButton.setText("Включить сервисное обслуживание");

                serviceButton.addClickListener(event -> {
                    if (serviceFlag) {
                        serviceFlag = false;
                        serviceButton.setText("Включить сервисное обслуживание");
                    }else {
                        serviceFlag = true;
                        serviceButton.setText("Выключить сервисное обслуживание");
                    }
                });

                FileBuffer fileBufferForPlan = new FileBuffer();
                Upload singleFileUploadPlan = new Upload(fileBufferForPlan);
                Upload singleFileUploadPlanOtp = new Upload(fileBufferForPlan);
                int maxFileSizeInBytesPlan = 50 * 1024 * 1024; // 50MB
                singleFileUploadPlan.setMaxFileSize(maxFileSizeInBytesPlan);
                UploadRussianI18N localizationPlan = new UploadRussianI18N("plan");
                UploadRussianI18N localizationPlanOtp = new UploadRussianI18N("otpusk");
                singleFileUploadPlan.setI18n(localizationPlan);
                singleFileUploadPlanOtp.setI18n(localizationPlanOtp);
                singleFileUploadPlan.setAcceptedFileTypes(".png");
                singleFileUploadPlanOtp.setAcceptedFileTypes(".png");


                TextArea instructAreaPlan = new TextArea();
                instructAreaPlan.setMinWidth("600px");
                instructAreaPlan.setMaxWidth("2000px");
                instructAreaPlan.setReadOnly(true);
                instructAreaPlan.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
                instructAreaPlan.setValue("""
                        Выберите файл с текущим планом.\s
                        Он будет отображаться в гостевом режиме\s
                        Файл должен иметь расширение .png, имя файла может быть любым
                        """);

                singleFileUploadPlan.addSucceededListener(event -> {
                    FileData savedFileData = fileBufferForPlan.getFileData();
                    String absolutePath = savedFileData.getFile().getAbsolutePath();
                    System.out.printf("Файл сохранён по пути: %s%n", absolutePath);
                    try {
                        FileUtils.deleteQuietly(new File("src/main/resources/images/volna.png"));
                        FileUtils.moveFile(new File(absolutePath), new File("src/main/resources/images/volna.png"));

                    } catch (IOException e) {
                        System.out.println("Во время перемещения файла возникла непредвиденная ошибка. Попробуйте ещё раз, либо замените файл вручную.");
                    }
                });
                singleFileUploadPlan.addFileRejectedListener(event -> {
                    String errorMessage = event.getErrorMessage();

                    Notification notification = Notification.show(errorMessage, 5000,
                            Notification.Position.MIDDLE);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                });

                //Загрузка плана отпусков
                TextArea instructOtpAreaPlan = new TextArea();
                instructOtpAreaPlan.setMinWidth("600px");
                instructOtpAreaPlan.setMaxWidth("2000px");
                instructOtpAreaPlan.setReadOnly(true);
                instructOtpAreaPlan.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
                instructOtpAreaPlan.setValue("""
                        Выберите файл с текущим планом отпусков.\s
                        Он будет отображаться во вкладке "Отпуска"\s
                        Файл должен иметь расширение .png, имя файла может быть любым
                        """);

                singleFileUploadPlanOtp.addSucceededListener(event -> {
                    FileData savedFileData = fileBufferForPlan.getFileData();
                    String absolutePath = savedFileData.getFile().getAbsolutePath();
                    System.out.printf("Файл сохранён по пути: %s%n", absolutePath);
                    try {
                        FileUtils.deleteQuietly(new File("src/main/resources/images/otpusk.png"));
                        FileUtils.moveFile(new File(absolutePath), new File("src/main/resources/images/otpusk.png"));

                    } catch (IOException e) {
                        System.out.println("Во время перемещения файла возникла непредвиденная ошибка. Попробуйте ещё раз, либо замените файл вручную.");
                    }
                });
                singleFileUploadPlanOtp.addFileRejectedListener(event -> {
                    String errorMessage = event.getErrorMessage();

                    Notification notification = Notification.show(errorMessage, 5000,
                            Notification.Position.MIDDLE);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                });

                //Загрузка файла worker list на сервер из браузера

                FileBuffer fileBuffer = new FileBuffer();
                Upload singleFileUpload = new Upload(fileBuffer);
                int maxFileSizeInBytes = 50 * 1024 * 1024; // 50MB
                singleFileUpload.setMaxFileSize(maxFileSizeInBytes);
                UploadRussianI18N localization = new UploadRussianI18N("backup");
                singleFileUpload.setI18n(localization);
                singleFileUpload.setAcceptedFileTypes(".bin");


                TextArea instructArea = new TextArea();
                instructArea.setMinWidth("600px");
                instructArea.setMaxWidth("2000px");
                instructArea.setReadOnly(true);
                instructArea.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
                instructArea.setValue("""
                        Выберите файл с бэкапом для восстановления данных.\s
                        Бэкапы хранятся на сервере по пути /root/Backup worker list/\s
                        Бэкап должен иметь расширение .bin, имя файла может быть любым
                        """);

                singleFileUpload.addSucceededListener(event -> {
                    FileData savedFileData = fileBuffer.getFileData();
                    String absolutePath = savedFileData.getFile().getAbsolutePath();
                    System.out.printf("Файл сохранён по пути: %s%n", absolutePath);
                    try {
                        FileUtils.deleteQuietly(new File("worker list.bin"));
                        FileUtils.moveFile(new File(absolutePath), new File("worker list.bin"));
                    } catch (IOException e) {
                        System.out.println("Во время перемещения файла возникла непредвиденная ошибка. Попробуйте ещё раз, либо замените файл вручную.");
                    }
                    try {
                        BrigEdit.workerList.clear();
                        Serial.load();
                    } catch (ClassNotFoundException e) {
                        System.out.println("Во время загрузки данных из файла произошла ошибка");
                    }
                    BrigEdit.initSplitDistrictWorkersList();
                });
                singleFileUpload.addFileRejectedListener(event -> {
                    String errorMessage = event.getErrorMessage();

                    Notification notification = Notification.show(errorMessage, 5000,
                            Notification.Position.MIDDLE);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                });

                //эта кнопка затирает табели и пересоздает их с нуля для всех сотрудников
                Button eraseAllTime = new Button("Стереть табели всем сотрудникам", new Icon(VaadinIcon.ERASER));
                eraseAllTime.addClickListener(event -> eraseAllWorkTimes());

                //Тут реализована возможность скачать worker list.bin в браузере
                Anchor download = new Anchor(new StreamResource(
                        "worker list (" + LocalDateTime.now().getDayOfMonth() + " " + LocalDateTime.now().getMonth() + ").bin",
                        () -> {
                            try {
                                return new FileInputStream("worker list.bin");
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }), "");
                download.getElement().setAttribute("download", true);
                download.add(new Button("Выгрузить \"worker list.bin\"", new Icon(VaadinIcon.DOWNLOAD_ALT)));

                Button initRangeMap = new Button("Первая инициализация rangeMap");
                initRangeMap.setIcon(new Icon(VaadinIcon.OPTIONS));
                initRangeMap.addClickListener(event -> {
                    for (HashMap.Entry<String, Device> entry : DeviceDefectView.devices.entrySet()) {
                        String key = entry.getKey();
                        Device device = entry.getValue();
                        device.initRangeMap();
                    }
                });


                //добавляем кнопки в интерфейс приложения
                add(initRangeMap, serviceButton, eraseAllTime, download, instructArea, singleFileUpload, instructAreaPlan, singleFileUploadPlan, instructOtpAreaPlan, singleFileUploadPlanOtp);

            } else {
                Notification notification = Notification.show("У вас нет доступа к этой странице");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
            }
        }
    }


        //метод, в котором вся логика для затирки всех табелей
        public static void eraseAllWorkTimes () {
            System.out.println("Начата процедура очистки табелей");
            for (Worker w : BrigEdit.workerList) {
                w.eraseAllMassive();
                w.initWorkTimeMap();
                w.initWorkerStatusMap();
            }
            Notification erase = Notification.show("Табели сотрудников были стёрты");
            erase.setPosition(Notification.Position.MIDDLE);
            erase.addThemeVariants(NotificationVariant.LUMO_WARNING);
        }
    }



