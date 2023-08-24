package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.poi.AllBrigRepoPOI;
import ru.markov.application.poi.BuildRepoPOI;
import ru.markov.application.poi.MountRepoPOI;
import ru.markov.application.poi.TechRepoPOI;

import java.io.*;
import java.time.LocalDate;


@Route(value = "reports", layout = MainLayout.class)
@PermitAll
@UIScope
@PageTitle("BrigApp א Отчёты")

public class Reports extends Div {
    public static DatePicker datePickerForRepo = new DatePicker(LocalDate.now());
    public static int month;

    public Reports() {
        GridEdit.initSplitDistrictWorkersList();
        ComboBox<String> selectReport = new ComboBox<>("Выбор бригады для отчета");
        ComboBox<String> selectMonth = new ComboBox<>("Месяц");

        selectMonth.setItems("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Откябрь", "Ноябрь", "Декабрь");
        selectReport.setAllowCustomValue(true);

        selectReport.setMinWidth("220px");
        selectReport.setItems("Бригада техники", "Бригада сборщики", "Бригада монтажники", "Все бригады");

        Button currentReport = new Button("Сформировать отчет за выбранный месяц", new Icon(VaadinIcon.FILE_TEXT));
        currentReport.setIconAfterText(true);
        currentReport.setHeightFull();
        currentReport.addClickListener(event -> {
            try {
                switch (selectMonth.getValue()) {
                    case "Январь" -> month = 1;
                    case "Февраль" -> month = 2;
                    case "Март" -> month = 3;
                    case "Апрель" -> month = 4;
                    case "Май" -> month = 5;
                    case "Июнь" -> month = 6;
                    case "Июль" -> month = 7;
                    case "Август" -> month = 8;
                    case "Сентябрь" -> month = 9;
                    case "Откябрь" -> month = 10;
                    case "Ноябрь" -> month = 11;
                    case "Декабрь" -> month = 12;
                }
                switch (selectReport.getValue()) {
                    case "Бригада монтажники" -> {
                        new MountRepoPOI();
                    }
                    case "Бригада сборщики" -> {
                        new BuildRepoPOI();
                    }
                    case "Бригада техники" -> {
                        new TechRepoPOI();
                    }
                    case "Все бригады" -> {
                        new AllBrigRepoPOI();
                    }
                }
                Notification n = Notification.show("Отчёт был создан. \nТеперь можно скачать файл!");
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.setPosition(Notification.Position.MIDDLE);
        } catch(NullPointerException npe){
            Notification error = Notification.show("Необходимо выбрать бригаду и отчётный месяц!");
            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            error.setPosition(Notification.Position.MIDDLE);
        } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String fileName = "График " + selectReport.getValue() + " за " + selectMonth.getValue() + " " + datePickerForRepo.getValue().getYear() + ".xlsx";
        Anchor download = new Anchor(new StreamResource(fileName, () -> {
            try {
                return new FileInputStream("Template.xlsx");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }), "");
        download.getElement().setAttribute("download", true);
        download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        add(download);
    });


    add(selectReport, selectMonth, currentReport);

}





}
