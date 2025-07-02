package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.markov.application.data.Device;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.Serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;

@Route(value = "device_defect", layout = MainLayout.class)
@PermitAll
@CssImport("themes/brigapp/styles.css")
@CssImport("./grid.css")
public class DeviceDefectView extends VerticalLayout {
    private String selectDeviceName = "";
    public static HashMap<String, Device> devices = new HashMap<>();
    public static HashMap<String, ArrayList<String>> familyDefectList = new HashMap<>();
    public ArrayList<String> presets = new ArrayList<>();
    private ComboBox<Month> monthSelect = new ComboBox<>();
    private int monthToOperations;
    private int dayToOperations;
    DatePicker tempDatePicker = new DatePicker();
    Grid<Device> grid = new Grid<>(Device.class, false);
    Grid<String> gridDefect = new Grid<>();

    DatePickerI18n russianI18n = new DatePickerI18n()
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

    public DeviceDefectView(SecurityService securityService) {
        if (!securityService.getAuthenticatedUser().getUsername().equals("admin") & ServiceTools.serviceFlag) {
            TextArea serviceMessage = new TextArea();
            serviceMessage.setMinWidth("500px");
            serviceMessage.setMaxWidth("500px");
            serviceMessage.setReadOnly(true);
            serviceMessage.setLabel("Внимание");
            serviceMessage.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
            serviceMessage.setValue("Извините, идут сервисные работы.\nПовторите попытку позже.");
            add(serviceMessage);
        } else if (securityService.getAuthenticatedUser().getUsername().equals("admin")
                || securityService.getAuthenticatedUser().getUsername().equals("tech")) {

            Tabs tabs = new Tabs();
            grid.addClassName("repoGrid");
            VerticalLayout contentArea = new VerticalLayout();
            Tab settingsTab = new Tab("Настройки");
            VerticalLayout settingsContent = createSettingsContent();
            Tab manageTab = new Tab("Учёт");
            VerticalLayout defectContent = createDefectContent();
            Tab tableDefect = new Tab("Сводка");
            VerticalLayout tableDefectContent = createTableDefectContent();
            tabs.add(tableDefect, manageTab, settingsTab);

            tabs.addSelectedChangeListener(event -> {
                contentArea.removeAll();
                if (event.getSelectedTab().equals(settingsTab)) {
                    contentArea.add(settingsContent);
                } else if (event.getSelectedTab().equals(manageTab)) {
                    contentArea.add(defectContent);
                } else {
                    contentArea.add(tableDefectContent);
                }
            });

            contentArea.add(tableDefectContent);
            add(tabs, contentArea);
        }
    }

    // --- Вкладка "Настройки" ---
    private VerticalLayout createSettingsContent() {
        VerticalLayout settingsContent = new VerticalLayout();

        Button addDevice = new Button("Добавить устройство");
        ComboBox<String> comboBoxSelectDevice = new ComboBox<>("Выберите устройство");
        comboBoxSelectDevice.setItems(devices.keySet());
        comboBoxSelectDevice.setValue("Список устройств");
        addDevice.addClickListener(event -> {
            Dialog dialogAddDevice = new Dialog();
            TextField newDeviceField = new TextField("Введите название устройства");
            newDeviceField.setWidth("100%");
            dialogAddDevice.open();
            Button addButton = new Button("Добавить", e -> {
                String newDevice = newDeviceField.getValue();
                if (newDevice != null && !newDevice.trim().isEmpty()) {
                    devices.put(newDevice, new Device(newDevice, presets));
                    dialogAddDevice.close();
                    if (!devices.isEmpty()) comboBoxSelectDevice.setValue(devices.get(newDevice).getDeviceName());
                } else {
                    Notification warning = Notification.show("Необходимо ввести название устройства.");
                    warning.setPosition(Notification.Position.MIDDLE);
                    warning.addThemeVariants(NotificationVariant.LUMO_WARNING);
                }
                comboBoxSelectDevice.setItems(devices.keySet());
                Serial.saveDevice();
                comboBoxSelectDevice.setValue(newDeviceField.getValue());
            });
            ComboBox<String> familyDefect = new ComboBox<>();
            if (familyDefectList.isEmpty()) {
                familyDefect.setPlaceholder("Нет пресетов брака");
            } else familyDefect.setItems(familyDefectList.keySet());
            familyDefect.addValueChangeListener(changeEvent -> {
                presets = familyDefectList.get(familyDefect.getValue());
            });
            HorizontalLayout horLay = new HorizontalLayout();
            horLay.add(addButton, familyDefect);
            dialogAddDevice.add(new VerticalLayout(newDeviceField, horLay));
        });

        Dialog dialogDefect = new Dialog();
        VerticalLayout itemsLayout = new VerticalLayout();
        Button addItemButton = new Button("Добавить пункт");

        addItemButton.addClickListener(e -> {
            TextField keyField = new TextField("Наименование брака");
            itemsLayout.add(keyField);
        });

        Button saveButton = new Button("Сохранить", e -> {
            selectDeviceName = comboBoxSelectDevice.getValue();
            String selectedKey = comboBoxSelectDevice.getValue();
            if (selectedKey != null && !selectedKey.equals("Список устройств")) {
                for (Component comp : itemsLayout.getChildren().toList()) {
                    if (comp instanceof TextField keyField) {
                        String key = keyField.getValue();
                        if (key != null && !key.isEmpty()) {
                            devices.get(selectedKey).initMapWithDefect(key);
                        }
                    }
                }
                dialogDefect.close();
                Serial.saveDevice();
            }
            gridDefect.setItems(devices.get(selectedKey).deviceMap.keySet());
        });

        saveButton.setIcon(new Icon(VaadinIcon.CURLY_BRACKETS));
        saveButton.setClassName("green-button");
        dialogDefect.add(itemsLayout, addItemButton, saveButton);
        Button editDefect = new Button("Редактировать список брака");
        editDefect.setIcon(new Icon(VaadinIcon.OPTIONS));
        editDefect.addClickListener(e -> {
            if (comboBoxSelectDevice.getValue() != null && !comboBoxSelectDevice.getValue().equals("Список устройств")) {
                itemsLayout.removeAll();
                addItemButton.click();
                dialogDefect.open();
            }
        });

        gridDefect.addColumn(key -> key).setHeader("Наименование брака");
        gridDefect.addComponentColumn(key -> {
            Button deleteButton = new Button("Удалить");
            deleteButton.addClickListener(click -> {
                String selectedKey = comboBoxSelectDevice.getValue();
                if (selectedKey != null && devices.containsKey(selectedKey)) {
                    devices.get(selectedKey).deviceMap.remove(key);
                    gridDefect.setItems(devices.get(selectedKey).deviceMap.keySet());
                }
            });
            deleteButton.setClassName("red-button");
            return deleteButton;
        }).setHeader("");

        comboBoxSelectDevice.addValueChangeListener(event -> {
            String selectedKey = event.getValue();
            if (selectedKey != null && devices.containsKey(selectedKey)) {
                gridDefect.setItems(devices.get(selectedKey).deviceMap.keySet());
            } else {
                gridDefect.setItems(Collections.emptyList());
            }
        });
        Button editDefectPreset = new Button("Настроить пересеты");
        editDefectPreset.setIcon(new Icon(VaadinIcon.EDIT));
        editDefectPreset.addClickListener(click -> {
            openFormEditPresets();
        });

        HorizontalLayout editButtonLayout = new HorizontalLayout();
        editButtonLayout.add(editDefect, editDefectPreset);
        settingsContent.add(addDevice, comboBoxSelectDevice, editButtonLayout, gridDefect);
        return settingsContent;
    }

    // --- Вкладка "Учёт" ---
    private VerticalLayout createDefectContent() {
        VerticalLayout defectContent = new VerticalLayout();
        DatePicker datePicker = new DatePicker("Выберите дату");
        datePicker.setI18n(russianI18n);
        defectContent.add(datePicker);
        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                openDeviceSelectionDialog(selectedDate);
                monthToOperations = selectedDate.getMonthValue();
                dayToOperations = selectedDate.getDayOfMonth();
            }
            tempDatePicker.setValue(datePicker.getValue());
        });
        return defectContent;
    }

    // --- Вкладка "Сводка" с разделением по линиям ---
    private VerticalLayout createTableDefectContent() {
        VerticalLayout tableDefectLayout = new VerticalLayout();
        initializeMonthSelector();
        monthSelect.addValueChangeListener(event -> updateGroupedGrids(tableDefectLayout));
        tableDefectLayout.add(monthSelect);
        updateGroupedGrids(tableDefectLayout);
        tableDefectLayout.setPadding(false);
        tableDefectLayout.setSpacing(false);
        return tableDefectLayout;
    }

    private void initializeMonthSelector() {
        monthSelect.setItems(Month.values());
        monthSelect.setItemLabelGenerator(month -> month.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU")));
        monthSelect.setLabel("Выберите месяц");
        monthSelect.setValue(LocalDate.now().getMonth());
    }

    private void updateGroupedGrids(VerticalLayout layout) {
        layout.removeAll();
        layout.add(monthSelect);

        Month selectedMonth = monthSelect.getValue();
        int monthValue = selectedMonth != null ? selectedMonth.getValue() : LocalDate.now().getMonthValue();

        Map<String, List<Device>> lineToDevices = new LinkedHashMap<>();
        for (Device device : filterDevicesByMonth(monthValue)) {
            HashMap<Integer, String> defaultValue = new HashMap<>();
            Map<Integer, String> dayLines = device.lineMap.getOrDefault(
                    monthValue,
                    defaultValue
            );

            // Собираем уникальные линии для устройства
            Set<String> uniqueLines = new HashSet<>();
            for (Map.Entry<Integer, String> entry : dayLines.entrySet()) {
                String line = entry.getValue();
                if (line != null && !line.isEmpty()) {
                    uniqueLines.add(line);
                }
            }
            if (uniqueLines.isEmpty()) {
                uniqueLines.add("Неизвестно");
            }
            for (String line : uniqueLines) {
                lineToDevices.computeIfAbsent(line, k -> new ArrayList<>()).add(device);
            }
        }

        // Желаемый порядок линий
        List<String> lineOrder = Arrays.asList("Линия 1", "Линия 2", "Линия 3", "Линия 4", "Неизвестно");

        // Список реально присутствующих линий в нужном порядке
        List<String> presentLines = lineOrder.stream()
                .filter(lineToDevices::containsKey)
                .collect(Collectors.toList());

        // Добавляем остальные линии (если вдруг есть нестандартные)
        lineToDevices.keySet().stream()
                .filter(line -> !presentLines.contains(line))
                .forEach(presentLines::add);

        // Выводим таблицы в нужном порядке
        for (String lineName : presentLines) {
            List<Device> devicesForLine = lineToDevices.get(lineName);
            H4 header = new H4(lineName);
            header.addClassName("line-header");
            layout.add(header);

            Grid<Device> grid = new Grid<>(Device.class, false);
            grid.addClassName("compact-grid");
            configureGrid(grid);
            addDayColumnsToGrid(grid, selectedMonth, lineName, monthValue);
            grid.setItems(devicesForLine);
            grid.setAllRowsVisible(true);
            layout.add(grid);
        }
    }


    private void configureGrid(Grid<Device> grid) {
        grid.addClassName("repoGrid");
        grid.removeAllColumns();
        grid.addColumn(Device::getDeviceName)
                .setHeader("Устройство")
                .setFlexGrow(0)
                .setAutoWidth(true)
                .addClassName("repoGrid::part(cell).first-column-cell");
    }

    private void addDayColumnsToGrid(Grid<Device> grid, Month selectedMonth, String lineName, int monthValue) {
        List<Integer> days = getDaysInMonth(selectedMonth);
        days.forEach(day -> {
            grid.addComponentColumn(device -> {
                // Проверяем принадлежность к линии
                HashMap<Integer, String> defaultValue = new HashMap<>();
                String deviceLine = device.lineMap
                        .getOrDefault(monthValue, defaultValue)
                        .get(day);

                if (deviceLine == null || !deviceLine.equals(lineName)) {
                    return new Span("");
                }

                int count = 0;
                for (String defectType : device.deviceMap.keySet()) {
                    HashMap<Integer, HashMap<Integer, Integer>> monthMap = device.deviceMap.get(defectType);
                    if (monthMap == null) continue;
                    HashMap<Integer, Integer> dayMap = monthMap.get(monthValue);
                    if (dayMap == null) continue;
                    Integer val = dayMap.get(day);
                    if (val != null && val > 0) {
                        count += val;
                    }
                }

                if (count > 0) {
                    Icon content = VaadinIcon.CHECK.create();
                    content.getElement().getThemeList().add("badge success");
                    content.getStyle().set("cursor", "pointer");
                    content.addClickListener(e -> showReportDialog(device, monthValue, day));
                    return content;
                } else {
                    return new Span("");
                }
            }).setHeader(day.toString()).setAutoWidth(true).setFlexGrow(0);
        });
    }

    private List<Integer> getDaysInMonth(Month month) {
        return IntStream.rangeClosed(1, month.length(Year.now().isLeap()))
                .boxed()
                .collect(Collectors.toList());
    }

    private void showReportDialog(Device device, int month, int day) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        H3 title = new H3("Отчет по устройству " + device.getDeviceName());
        layout.add(title);

        H4 line = new H4(device.lineMap.get(month).get(day));
        layout.add(line);

        H4 startFinishPart = new H4(device.startPartDate.get(month).get(day) + " - " + device.finishPartDate.get(month).get(day)+"\n");
        layout.add(startFinishPart);

        Integer count = device.totalPartMap
                .get(month)
                .get(day);
        Span countSpan = new Span("Количество в партии: " + count);
        layout.add(countSpan);

        H4 defectsTitle = new H4("Брак в этой партии:");
        layout.add(defectsTitle);

        boolean hasDefects = false;
        for (Map.Entry<String, HashMap<Integer, HashMap<Integer, Integer>>> defectEntry : device.deviceMap.entrySet()) {
            String defectName = defectEntry.getKey();
            HashMap<Integer, HashMap<Integer, Integer>> monthMap = defectEntry.getValue();
            Integer defectCount = monthMap.getOrDefault(month, new HashMap<>())
                    .getOrDefault(day, 0);
            if (defectCount != null && defectCount > 0) {
                hasDefects = true;
                Span defectSpan = new Span(defectName + ": " + defectCount);
                layout.add(defectSpan);
            }
        }
        if (!hasDefects) {
            layout.add(new Span("Брак не зафиксирован."));
        }

        Button closeButton = new Button("Закрыть", e -> dialog.close());
        closeButton.setClassName("red-button");
        Button downloadReport = new Button("Скачать отчёт");
        downloadReport.setClassName("green-button");
        StreamResource resource = createExcelResource(device, month, day);
        Anchor downloadLink = new Anchor(resource, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(downloadReport);
        Button correctReport = new Button();
        correctReport.setText("Внести корректировки");
        correctReport.setIcon(new Icon(VaadinIcon.REPLY));
        correctReport.addClassNames("yellow-button");
        correctReport.addClickListener(e ->{
            monthToOperations = monthSelect.getValue().getValue();
            dayToOperations = day;
            openFormDialog(device.getDeviceName());
        });
        layout.add(downloadLink, correctReport, closeButton);
        dialog.add(layout);
        dialog.open();
    }

    private void openDeviceSelectionDialog(LocalDate date) {
        Dialog deviceDialog = new Dialog();
        deviceDialog.setWidth("300px");

        ListBox<String> deviceListBox = new ListBox<>();
        deviceListBox.setItems(devices.keySet());

        Button selectButton = new Button("Выбрать", event -> {
            String selectedDevice = deviceListBox.getValue();
            if (selectedDevice != null) {
                deviceDialog.close();
                openFormDialog(selectedDevice);
            }
        });

        deviceDialog.add(deviceListBox, selectButton);
        deviceDialog.open();
    }

    private void openFormEditPresets() {
        Dialog presetDialog = new Dialog();
        VerticalLayout layout = new VerticalLayout();

        Button editExistPreset = new Button("Редактировать пресет");
        editExistPreset.addClickListener(clickEvent -> {
            presetDialog.close();
            openDialogEditPresets();
        });

        TextField presetName = new TextField();
        presetName.setPlaceholder("Введите пресет");
        presetName.setLabel("Название пресета");

        VerticalLayout otherFieldLayout = new VerticalLayout();

        Button addItemButton = new Button("Добавить брак");
        addItemButton.addClickListener(e -> {
            TextField keyField = new TextField("Наименование брака");
            keyField.setPlaceholder("Пустая строка - удаление пункта");
            otherFieldLayout.add(keyField);
        });

        Button saveButton = new Button("Сохранить", e -> {
            if (presetName.getValue() != null && !presetName.getValue().isEmpty()) {
                this.presets.clear();
                for (Component comp : otherFieldLayout.getChildren().toList()) {
                    if (comp instanceof TextField txt) {
                        String key = txt.getValue();
                        if (key != null && !key.isEmpty()) {
                            this.presets.add(key);
                        }
                    }
                }
                familyDefectList.put(presetName.getValue(), this.presets);
                System.out.println(this.presets.toString());
                presetDialog.close();
                Serial.savePreset();
            } else {
                Notification.show("Пожалуйста, введите название пресета", 3000, Notification.Position.MIDDLE);
            }
        });
        saveButton.setClassName("green-button");

        HorizontalLayout addSaveLayout = new HorizontalLayout(addItemButton, saveButton);
        layout.add(editExistPreset, presetName);
        presetDialog.add(layout, otherFieldLayout, addSaveLayout);
        presetDialog.open();
    }

    private void openDialogEditPresets() {
        Dialog editPresetDialog = new Dialog();
        ComboBox<String> presetComboBox = new ComboBox<>("Выберите пресет");
        VerticalLayout layout = new VerticalLayout();
        VerticalLayout editorLayout = new VerticalLayout();

        presetComboBox.setItems(familyDefectList.keySet());
        presetComboBox.addValueChangeListener(changeEvent -> {
            editorLayout.removeAll();
            String selectedPreset = presetComboBox.getValue();
            if (selectedPreset != null) {
                List<String> items = familyDefectList.get(selectedPreset);
                if (items != null) {
                    for (String item : items) {
                        TextField textField = new TextField();
                        textField.setValue(item);
                        textField.setWidthFull();
                        textField.setPlaceholder("Пустая строка - удаление пункта");
                        editorLayout.add(textField);
                    }
                }
            }
        });

        Button addRowButton = new Button("Добавить брак", event -> {
            TextField newTextField = new TextField();
            newTextField.setWidthFull();
            newTextField.setPlaceholder("Введите значение");
            editorLayout.add(newTextField);
        });

        Button saveButton = new Button("Сохранить", event -> {
            String selectedPreset = presetComboBox.getValue();
            if (selectedPreset != null) {
                ArrayList<String> newItems = new ArrayList<>();
                for (Component comp : editorLayout.getChildren().toList()) {
                    if (comp instanceof TextField) {
                        String value = ((TextField) comp).getValue().trim();
                        if (!value.isEmpty()) {
                            newItems.add(value);
                        }
                    }
                }
                familyDefectList.put(selectedPreset, newItems);
                Notification.show("Пресет успешно сохранён");
                editPresetDialog.close();
            } else {
                Notification.show("Пожалуйста, выберите пресет для редактирования", 3000, Notification.Position.MIDDLE);
            }
        });
        saveButton.setClassName("green-button");

        Button cancelButton = new Button("Отмена", event -> editPresetDialog.close());
        cancelButton.setClassName("red-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(addRowButton, saveButton, cancelButton);
        layout.add(presetComboBox, editorLayout, buttonsLayout);
        layout.setSpacing(true);
        layout.setPadding(true);

        editPresetDialog.add(layout);
        editPresetDialog.open();
    }

    private void openFormDialog(String deviceName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //String dateString = "01.01.2025";
        Dialog formDialog = new Dialog();
        formDialog.setWidth("450px");
        formDialog.setMinHeight("600px");
        FormLayout formLayout = new FormLayout();
        IntegerField partTotalField = new IntegerField("Партия шт.");
        ComboBox<String> lineComboBox = new ComboBox<>();
        lineComboBox.setItems("Линия 1", "Линия 2", "Линия 3", "Линия 4");
        lineComboBox.setValue("Линия 1");
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd.MM.yyyy");
        DatePicker startDate = new DatePicker("Начало партии");
        startDate.setI18n(i18n);
        DatePicker finishDate = new DatePicker("Конец партии");
        finishDate.setI18n(i18n);
        startDate
                .addValueChangeListener(e -> finishDate.setMin(e.getValue()));
        finishDate.addValueChangeListener(
                e -> startDate.setMax(e.getValue()));
        formLayout.add(partTotalField, lineComboBox);
        formLayout.add(new HorizontalLayout(startDate, finishDate));
        Map<String, IntegerField> parameterFields = new HashMap<>();

        if (devices.get(deviceName).totalPartMap.get(monthToOperations).get(dayToOperations) != 0) {
            partTotalField.setValue(devices.get(deviceName).totalPartMap.get(monthToOperations).get(dayToOperations));
            lineComboBox.setValue(devices.get(deviceName).lineMap.get(monthToOperations).get(dayToOperations));
            startDate.setValue(LocalDate.parse(devices.get(deviceName).startPartDate.get(monthToOperations).get(dayToOperations), formatter));
            finishDate.setValue(LocalDate.parse(devices.get(deviceName).finishPartDate.get(monthToOperations).get(dayToOperations), formatter));

            for (String key : devices.get(deviceName).deviceMap.keySet()) {
                IntegerField field = new IntegerField(key);
                field.setValue(devices.get(deviceName).deviceMap.get(key).get(monthToOperations).get(dayToOperations));
                parameterFields.put(key, field);
                formLayout.add(field);
            }
        } else {
            partTotalField.setPlaceholder("0");
            lineComboBox.setValue("Линия 1");
            finishDate.setValue(tempDatePicker.getValue());
            finishDate.setReadOnly(true);
            for (String key : devices.get(deviceName).deviceMap.keySet()) {
                IntegerField field = new IntegerField(key);
                field.setPlaceholder("0");
                parameterFields.put(key, field);
                formLayout.add(field);
            }
        }

        Button saveButton = new Button("Сохранить", event -> {
            int batch = partTotalField.getValue();
            Map<String, Integer> params = new HashMap<>();
            parameterFields.forEach((k, v) -> params.put(k, v.getValue()));
            devices.get(deviceName).totalPartMap.get(monthToOperations).put(dayToOperations, batch);
            devices.get(deviceName).lineMap.get(monthToOperations).put(dayToOperations, lineComboBox.getValue());
            params.forEach((defect, volume) -> devices.get(deviceName).deviceMap.get(defect).get(monthToOperations).put(dayToOperations, volume));
            devices.get(deviceName).lineMap.get(monthToOperations).put(dayToOperations, lineComboBox.getValue());
            devices.get(deviceName).startPartDate.get(monthToOperations).put(dayToOperations, startDate.getValue().format(formatter));
            devices.get(deviceName).finishPartDate.get(monthToOperations).put(dayToOperations, finishDate.getValue().format(formatter));
            Serial.saveDevice();
            formDialog.close();
        });
        saveButton.setClassName("green-button");
        formDialog.add(formLayout, saveButton);
        formDialog.open();
    }

    private StreamResource createExcelResource(Device device, int month, int day) {
        String safeDeviceName = device.getDeviceName().replace('/', 'x').replace(',', 'x');
        return new StreamResource("Отчёт по " + safeDeviceName + ".xlsx", () -> {
            try (Workbook workbook = new XSSFWorkbook();
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                Sheet sheet = workbook.createSheet("Отчет");
                sheet.autoSizeColumn(1);

                Font boldFont = workbook.createFont();
                boldFont.setBold(true);

                CellStyle boldLeft = workbook.createCellStyle();
                boldLeft.setFont(boldFont);
                boldLeft.setAlignment(HorizontalAlignment.LEFT);
                boldLeft.setVerticalAlignment(VerticalAlignment.TOP);
                setBorders(boldLeft);
                boldLeft.setWrapText(true);

                CellStyle boldRight = workbook.createCellStyle();
                boldRight.setFont(boldFont);
                boldRight.setAlignment(HorizontalAlignment.RIGHT);
                setBorders(boldRight);

                CellStyle normalLeft = workbook.createCellStyle();
                normalLeft.setAlignment(HorizontalAlignment.LEFT);
                setBorders(normalLeft);

                CellStyle normalRight = workbook.createCellStyle();
                normalRight.setAlignment(HorizontalAlignment.RIGHT);
                setBorders(normalRight);

                CellStyle yellowBoldLeft = workbook.createCellStyle();
                yellowBoldLeft.cloneStyleFrom(boldLeft);
                yellowBoldLeft.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                yellowBoldLeft.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle yellowBoldRight = workbook.createCellStyle();
                yellowBoldRight.cloneStyleFrom(boldRight);
                yellowBoldRight.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                yellowBoldRight.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle boldCenter = workbook.createCellStyle();
                boldCenter.setFont(boldFont);
                boldCenter.setAlignment(HorizontalAlignment.CENTER);
                boldCenter.setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(boldCenter);

                String deviceName = device.getDeviceName();
                String line = device.lineMap.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                String startDate = device.startPartDate.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                String finishDate = device.finishPartDate.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                int totalPart = device.totalPartMap.getOrDefault(month, new HashMap<>()).getOrDefault(day, 0);

                Set<String> defects = device.deviceMap.entrySet().stream()
                        .filter(entry -> {
                            HashMap<Integer, HashMap<Integer, Integer>> monthsMap = entry.getValue();
                            HashMap<Integer, Integer> daysMap = monthsMap.get(month);
                            int count = daysMap != null ? daysMap.getOrDefault(day, 0) : 0;
                            return count > 0;
                        })
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                int defectTotal = 0;
                Map<String, Integer> defectCounts = new LinkedHashMap<>();
                for (String defect : defects) {
                    int count = device.deviceMap.get(defect)
                            .getOrDefault(month, new HashMap<>())
                            .getOrDefault(day, 0);
                    defectCounts.put(defect, count);
                    defectTotal += count;
                }

                double defectPercent = totalPart > 0 ? (double) defectTotal / totalPart * 100 : 0;

                int rowNum = 0;

                Row row1 = sheet.createRow(rowNum++);
                Cell cellLine = row1.createCell(0);
                cellLine.setCellValue(line);
                cellLine.setCellStyle(boldLeft);

                Row row2 = sheet.createRow(rowNum++);
                Cell cellDates = row2.createCell(0);
                String dates = startDate + " - " + finishDate;
                cellDates.setCellValue(dates);
                cellDates.setCellStyle(boldLeft);

                sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                Cell cellDevice = row1.createCell(1);
                cellDevice.setCellValue(deviceName);
                cellDevice.setCellStyle(boldCenter);

                Cell cellDevice2 = row2.createCell(1);
                cellDevice2.setCellStyle(boldCenter);

                Row row3 = sheet.createRow(rowNum++);
                Cell cellPartLabel = row3.createCell(0);
                cellPartLabel.setCellValue("Партия (шт)");
                cellPartLabel.setCellStyle(boldLeft);

                Cell cellPartValue = row3.createCell(1);
                cellPartValue.setCellValue(totalPart);
                cellPartValue.setCellStyle(boldRight);

                for (String defect : defects) {
                    Row row = sheet.createRow(rowNum++);
                    Cell cellDefect = row.createCell(0);
                    cellDefect.setCellValue(defect);
                    cellDefect.setCellStyle(normalLeft);

                    Cell cellDefectCount = row.createCell(1);
                    cellDefectCount.setCellValue(defectCounts.get(defect));
                    cellDefectCount.setCellStyle(normalRight);
                }

                Row rowItogo = sheet.createRow(rowNum++);
                Cell cellItogo = rowItogo.createCell(0);
                cellItogo.setCellValue("итого");
                cellItogo.setCellStyle(yellowBoldLeft);

                Cell cellItogoValue = rowItogo.createCell(1);
                cellItogoValue.setCellValue(defectTotal);
                cellItogoValue.setCellStyle(yellowBoldRight);

                Row rowPercent = sheet.createRow(rowNum++);
                Cell cellPercentLabel = rowPercent.createCell(0);
                cellPercentLabel.setCellValue("процент брака");
                cellPercentLabel.setCellStyle(boldLeft);

                Cell cellPercentValue = rowPercent.createCell(1);
                cellPercentValue.setCellValue(String.format("%.2f%%", defectPercent));
                cellPercentValue.setCellStyle(boldRight);

                for (int col = 0; col <= 1; col++) {
                    sheet.autoSizeColumn(col);
                    int currentWidth = sheet.getColumnWidth(col);
                    sheet.setColumnWidth(col, currentWidth + 512);
                }

                workbook.write(bos);
                return new ByteArrayInputStream(bos.toByteArray());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private boolean deviceHasAnyRecordInMonth(Device device, int monthValue) {
        for (String defectType : device.deviceMap.keySet()) {
            HashMap<Integer, HashMap<Integer, Integer>> monthMap = device.deviceMap.get(defectType);
            if (monthMap == null) continue;

            HashMap<Integer, Integer> dayMap = monthMap.get(monthValue);
            if (dayMap == null) continue;

            for (Integer count : dayMap.values()) {
                if (count != null && count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Collection<Device> filterDevicesByMonth(int monthValue) {
        return devices.values().stream()
                .filter(device -> deviceHasAnyRecordInMonth(device, monthValue))
                .collect(Collectors.toList());
    }
}
