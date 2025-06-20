package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.ValidationName;
import ru.markov.application.data.Worker;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.Consumer;


@PermitAll
@Route(value = "worktime", layout = MainLayout.class)
@PageTitle("BrigApp א Учёт времени")
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class WorkTime extends Div {

    public DatePicker workTimeDatePicker = new DatePicker();
    private String dayOfWeek = LocalDate.now().getDayOfWeek().toString();
    public WorkTime(SecurityService securityService) {

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

            BrigEdit.initSplitDistrictWorkersList();
            String username = securityService.getAuthenticatedUser().getUsername();
            DatePicker.DatePickerI18n ruPicker = new DatePicker.DatePickerI18n()
                    .setWeekdays(Arrays.asList(
                            "Воскресенье", "Понедельник", "Вторник", "Среда",
                            "Четверг", "Пятница", "Суббота"))
                    .setWeekdaysShort(Arrays.asList("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"))
                    .setMonthNames(Arrays.asList(
                            "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"))
                    .setToday("Сегодня")
                    .setCancel("Отмена")
                    .setFirstDayOfWeek(1);
            workTimeDatePicker.setI18n(ruPicker);
            workTimeDatePicker.setValue(LocalDate.now());
            Grid<Worker> workTimeGrid = new Grid<>(Worker.class, false);
            workTimeGrid.addClassName("work-time-grid");


            initPage(workTimeGrid); //метод служит для предотвращения бага отображения, когда по LocalDate.now() в таблице были не актуальные данные

            workTimeGrid.setWidthFull();
            workTimeGrid.setMinHeight("500px");
            workTimeGrid.setHeight("800px");
            setItemForGrid(username, workTimeGrid);


            Editor<Worker> editor = workTimeGrid.getEditor();
            Binder<Worker> binder = new Binder<>(Worker.class);
            editor.setBinder(binder);
            editor.setBuffered(false);

            workTimeDatePicker.addClientValidatedEventListener(clientValidatedEvent -> {
                TimeAdapter.workTimeDatePicker.setValue(workTimeDatePicker.getValue());
                workTimeGrid.getDataProvider().refreshAll();
            });

            Button save = new Button("Записать время");
            save.addClickListener(buttonClickEvent -> {
                Serial.save();
                //System.out.println("Рабочее время было записано");
                workTimeGrid.getDataProvider().refreshAll();
            });

            Button likeYesterday = new Button("\"Как вчера\"");
            likeYesterday.addClickListener(buttonClickEvent -> {
                switch (username) {
                    case "volna1" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_1)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeYesterday();
                                    w.setWorkerStatusMassiveLikeYesterday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);

                                }
                            }
                        }
                    }
                    case "volna2" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_2)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeYesterday();
                                    w.setWorkerStatusMassiveLikeYesterday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);

                                }
                            }
                        }
                    }
                    case "volna3" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_3)) { if (w.checkTimeAndStatus()){
                                w.setWorkTimeLikeYesterday();
                                w.setWorkerStatusMassiveLikeYesterday();
                            }
                            else {
                                Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                notification.setPosition(Notification.Position.MIDDLE);
                                notification.setDuration(9000);

                            }
                            }
                        }
                    }
                    case "volna4" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_4)) { if (w.checkTimeAndStatus()){
                                w.setWorkTimeLikeYesterday();
                                w.setWorkerStatusMassiveLikeYesterday();
                            }
                            else {
                                Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                notification.setPosition(Notification.Position.MIDDLE);
                                notification.setDuration(9000);

                            }
                            }
                        }
                    }
                    case "sborka1" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_1)) { if (w.checkTimeAndStatus()){
                                w.setWorkTimeLikeYesterday();
                                w.setWorkerStatusMassiveLikeYesterday();
                            }
                            else {
                                Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                notification.setPosition(Notification.Position.MIDDLE);
                                notification.setDuration(9000);

                            }
                            }
                        }
                    }
                    case "sborka2" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_2)) { if (w.checkTimeAndStatus()){
                                w.setWorkTimeLikeYesterday();
                                w.setWorkerStatusMassiveLikeYesterday();
                            }
                            else {
                                Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                notification.setPosition(Notification.Position.MIDDLE);
                                notification.setDuration(9000);

                            }
                            }
                        }
                    }
                    case "sborka3" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_3)) { if (w.checkTimeAndStatus()){
                                w.setWorkTimeLikeYesterday();
                                w.setWorkerStatusMassiveLikeYesterday();
                            }
                            else {
                                Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                notification.setPosition(Notification.Position.MIDDLE);
                                notification.setDuration(9000);

                            }
                            }
                        }
                    }
                    case "sborka4" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_4)) { if (w.checkTimeAndStatus()){
                                w.setWorkTimeLikeYesterday();
                                w.setWorkerStatusMassiveLikeYesterday();
                            }
                            else {
                                Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                notification.setPosition(Notification.Position.MIDDLE);
                                notification.setDuration(9000);

                            }
                            }
                        }
                    }
                    case "tech" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.TECH) || w.getDistrict().equals(District.LAB1) || w.getDistrict().equals(District.LAB2) || w.getDistrict().equals(District.LAB5)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeYesterday();
                                    w.setWorkerStatusMassiveLikeYesterday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                }
                workTimeGrid.getDataProvider().refreshAll();
            });
            Button likeFriday = new Button("\"Как в пятницу\"");
            likeFriday.addClickListener(buttonClickEvent -> {
                switch (username) {
                    case "volna1" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_1)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "volna2" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_2)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "volna3" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_3)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "volna4" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.MOUNTING) && w.getLine().equals(ConveyLine.LINE_4)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "sborka1" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_1)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "sborka2" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_2)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "sborka3" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_3)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "sborka4" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.BUILDING) && w.getLine().equals(ConveyLine.LINE_4)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                    case "tech" -> {
                        for (Worker w : BrigEdit.workerList) {
                            if (w.getDistrict().equals(District.TECH) || w.getDistrict().equals(District.LAB1) || w.getDistrict().equals(District.LAB2) || w.getDistrict().equals(District.LAB5)) {
                                if (w.checkTimeAndStatus()){
                                    w.setWorkTimeLikeFriday();
                                    w.setWorkerStatusMassiveLikeFriday();
                                }
                                else {
                                    Notification notification = Notification.show("У работника "+w.getFullNameWithInitials()+"сегодня уже проставлено время, либо статус.\nПроверьте актуальность данных");
                                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                                    notification.setPosition(Notification.Position.MIDDLE);
                                    notification.setDuration(9000);
                                }
                            }
                        }
                    }
                }
                workTimeGrid.getDataProvider().refreshAll();
            });
            if (dayOfWeek.equals("MONDAY")) {
                likeYesterday.setEnabled(false);
                likeFriday.setEnabled(true);
            } else {
                likeYesterday.setEnabled(true);
                likeFriday.setEnabled(false);
            }

            Grid.Column<Worker> fullNameColumn = workTimeGrid
                    .addColumn(Worker::getFullNameWithInitials).setTextAlign(ColumnTextAlign.START)
                    .setSortProperty("lastName")
                    .setHeader("ФИО сотрудника")
                    .setAutoWidth(false)
                    .setResizable(true)
                    .setSortable(true)
                    .setWidth("170px")
                    .setFlexGrow(0);

            switch (securityService.getAuthenticatedUser().getUsername()) {
                case "admin" -> fullNameColumn.setFooter("Сотрудников: " + BrigEdit.workerList.size());
                case "volna1" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.mountMap.get(ConveyLine.LINE_1).size());
                case "volna2" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.mountMap.get(ConveyLine.LINE_2).size());
                case "volna3" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.mountMap.get(ConveyLine.LINE_3).size());
                case "volna4" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.mountMap.get(ConveyLine.LINE_4).size());
                case "sborka1" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.builderMap.get(ConveyLine.LINE_1).size());
                case "sborka2" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.builderMap.get(ConveyLine.LINE_2).size());
                case "sborka3" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.builderMap.get(ConveyLine.LINE_3).size());
                case "sborka4" ->
                        fullNameColumn.setFooter("Сотрудников: " + BrigEdit.builderMap.get(ConveyLine.LINE_4).size());
                case "tech" -> fullNameColumn.setFooter("Сотрудников: " + BrigEdit.techListUPC.size());
            }


            Grid.Column<Worker> lineColumn = workTimeGrid.
                    addColumn(Worker::getLineToString)
                    .setHeader("Линия")
                    .setAutoWidth(false)
                    .setResizable(true)
                    .setWidth("100px")
                    .setFlexGrow(1);
            if (!(username.equals("admin"))) lineColumn.setVisible(false);

            Grid.Column<Worker> districtColumn = workTimeGrid
                    .addColumn(Worker::getDistrictToString)
                    .setHeader("Участок")
                    .setAutoWidth(false)
                    .setResizable(true)
                    .setWidth("200px")
                    .setFlexGrow(1);


            Grid.Column<Worker> workTimeColumn = workTimeGrid
                    .addColumn(Worker::getWorkTime)
                    .setHeader("Время")
                    .setAutoWidth(false)
                    .setResizable(false)
                    .setWidth("115px")
                    .setFlexGrow(0);

            if (!(username.equals("admin"))) {
                districtColumn.setVisible(false);
                workTimeColumn.setWidth("115px");
            }


            Grid.Column<Worker> workerStatusColumn = workTimeGrid.
                    addColumn(Worker::getWorkerStatusMassive)
                    .setHeader("Статус")
                    .setAutoWidth(false)
                    .setResizable(true)
                    .setWidth("150px")
                    .setFlexGrow(1);

            //добавляем фильтры
            if (username.equals("admin")) {
                try {
                    HeaderRow headerRow = workTimeGrid.appendHeaderRow();
                    GridListDataView<Worker> dataView = workTimeGrid.setItems(BrigEdit.workerList);
                    PersonFilter personFilter = new WorkTime.PersonFilter(dataView);

                    headerRow.getCell(lineColumn).setComponent(createFilterHeader(personFilter::setLine));
                    headerRow.getCell(districtColumn).setComponent(createFilterHeader(personFilter::setDistrict));
                    headerRow.getCell(fullNameColumn).setComponent(createFilterHeader(personFilter::setFullName));
                    headerRow.getCell(workTimeColumn).setComponent(createFilterHeader(personFilter::setTime));
                    headerRow.getCell(workerStatusColumn).setComponent(createFilterHeader(personFilter::setStatus));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


            IntegerField setTimeEdit = new IntegerField();
            ValidationName timeValid = new ValidationName();
            setTimeEdit.setValue(8);
            setTimeEdit.setWidth("100px");
            setTimeEdit.setStepButtonsVisible(true);
            setTimeEdit.setMin(1);
            setTimeEdit.setMax(20);
            addCloseHandler(setTimeEdit, editor);
            binder.forField(setTimeEdit)
                    .asRequired()
                    .withStatusLabel(timeValid)
                    .bind(Worker::getWorkTime, Worker::setWorkTime);
            workTimeColumn.setEditorComponent(setTimeEdit);

            RadioButtonGroup<String> statusEditColumn = new RadioButtonGroup<>();
            ValidationName statusValid = new ValidationName();
            statusEditColumn.setItems("10",
                    "9", "8", "0",
                    "БОЛ",
                    "ОТП",
                    "ОТГ", "АДМ");
            statusEditColumn.setWidthFull();
            addCloseHandler(statusEditColumn, editor);
            binder.forField(statusEditColumn)
                    .asRequired()
                    .withStatusLabel(statusValid)
                    .bind(Worker::getWorkerStatusMassive, Worker::setWorkerStatusMassive);
            workerStatusColumn.setEditorComponent(statusEditColumn);


            workTimeGrid.addItemClickListener(e -> {
                editor.editItem(e.getItem());
                Component editorComponent = e.getColumn().getEditorComponent();
                if (editorComponent instanceof Focusable) {
                    ((Focusable<?>) editorComponent).focus();
                }
                workTimeGrid.getDataProvider().refreshAll();
            });


            editor.addCancelListener(e -> {
                timeValid.setText("");
                workTimeGrid.getDataProvider().refreshAll();
            });

            workTimeGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

            workTimeGrid.setPartNameGenerator(person -> {
                switch (person.getWorkerStatus()) {
                    case WORK -> {
                        return "work";
                    }
                    case HOSPITAL -> {
                        return "bol";
                    }
                    case HOLIDAY -> {
                        return "otpusk";
                    }
                    case NOTHING -> {
                        return "nothing";
                    }
                    case ADMINOTP -> {
                        return "administrat";
                    }
                    case OTRABOTKA -> {
                        return "otrabotka";
                    }
                    case PERERABOTKA -> {
                        return "pererabotka";
                    }
                }
                return null;
            });

            add(workTimeDatePicker, save, likeYesterday, likeFriday, workTimeGrid);
        }
    }

    public void setItemForGrid(String sc, Grid<Worker> grid) {
        switch (sc) {
            case "admin", "owner" -> grid.setItems(BrigEdit.workerList);
            case "volna1" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_1));
            case "volna2" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_2));
            case "volna3" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_3));
            case "volna4" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_4));
            case "sborka1" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_1));
            case "sborka2" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_2));
            case "sborka3" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_3));
            case "sborka4" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_4));
            case "tech" -> grid.setItems(BrigEdit.allTech);
        }

    }

    private static void addCloseHandler(Component textField, Editor<Worker> editor) {
        textField.getElement()
                .addEventListener("keydown", e -> editor.cancel())
                .setFilter("event.code === 'Escape'");
    }

    private static Component createFilterHeader(Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }

    private static class PersonFilter {
        private final GridListDataView<Worker> dataView;
        private String fullName;
        private String line;
        private String district;
        private String time;
        private String status;

        public PersonFilter(GridListDataView<Worker> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::matchesField);
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
            this.dataView.refreshAll();
        }

        public void setTime(String time) {
            this.time = time;
            this.dataView.refreshAll();
        }

        public void setStatus(String status) {
            this.status = status;
            this.dataView.refreshAll();
        }

        public void setLine(String line) {
            this.line = line;
            this.dataView.refreshAll();
        }

        public void setDistrict(String district) {
            this.district = district;
            this.dataView.refreshAll();
        }

        public boolean matchesField(Worker worker) {
            boolean matchesLine = matches(worker.getLineToString(), line);
            boolean matchesDistrict = matches(worker.getDistrictToString(), district);
            boolean matchesFullName = matches(worker.getFullName(), fullName);
            boolean matchesTime = matches(String.valueOf(worker.getWorkTime()), time);
            boolean matchesStatus = matches(worker.getWorkerStatusMassive(), status);


            return matchesLine && matchesDistrict && matchesFullName && matchesTime && matchesStatus;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }
    }

    private void initPage(Grid<Worker> grid) {
        TimeAdapter.workTimeDatePicker.setValue(workTimeDatePicker.getValue());
        grid.getDataProvider().refreshAll();
    }
}




