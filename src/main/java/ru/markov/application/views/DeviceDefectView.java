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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.markov.application.data.Device;
import ru.markov.application.service.Serial;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Route(value = "device_defect", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
@CssImport("./styles/styles.css")
public class DeviceDefectView extends VerticalLayout {
    private String selectDeviceName = "";
    public static HashMap<String, Device> devices = new HashMap<>();
    private int month;
    private int day;
    private ComboBox<Month> monthSelect = new ComboBox<>();
    Grid<Device> grid = new Grid<>(Device.class, false);
    Grid<String> gridDefect = new Grid<>();


    public DeviceDefectView() {
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

        // чтобы вкладки перерисовывали содержимое
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
        // дефолтное содержимое (учёт)
        contentArea.add(tableDefectContent);
        add(tabs, contentArea);
    }
        // наполнение вкладки "настройки"
    private VerticalLayout createSettingsContent() {
        // Переносим весь оригинальный код сюда
        VerticalLayout settingsContent = new VerticalLayout();

        Button addDevice = new Button("Добавить устройство");
        ComboBox<String> comboBox = new ComboBox<>("Выберите устройство");
        comboBox.setItems(devices.keySet());
        comboBox.setValue("Список устройств");

        addDevice.addClickListener(event -> {
            Dialog dialogAddDevice = new Dialog();
            TextField newDeviceField = new TextField("Введите название устройства");
            Button addButton = new Button("Добавить", e -> {
                String newDevice = newDeviceField.getValue();
                if (newDevice != null && !newDevice.trim().isEmpty()) {
                    devices.put(newDevice, new Device(newDevice));
                    dialogAddDevice.close();
                }
                comboBox.setItems(devices.keySet());
                if (!devices.isEmpty()) {
                    comboBox.setValue(devices.keySet().iterator().next());
                }
                Serial.saveDevice();
            });
            dialogAddDevice.add(new VerticalLayout(newDeviceField, addButton));
            dialogAddDevice.open();
        });

        Dialog dialogDefect = new Dialog();
        VerticalLayout itemsLayout = new VerticalLayout();
        Button addItemButton = new Button("Добавить пункт");

        addItemButton.addClickListener(e -> {
            TextField keyField = new TextField("Наименование брака");
            itemsLayout.add(keyField);
        });

        Button saveButton = new Button("Сохранить", e -> {
            selectDeviceName = comboBox.getValue();
            String selectedKey = comboBox.getValue();
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

        dialogDefect.add(itemsLayout, addItemButton, saveButton);
        Button editDefect = new Button("Редактировать список брака");
        editDefect.addClickListener(e -> {
            if (comboBox.getValue() != null && !comboBox.getValue().equals("Список устройств")) {
                itemsLayout.removeAll();
                addItemButton.click();
                dialogDefect.open();
            }
        });


        gridDefect.addColumn(key -> key).setHeader("Наименование брака");
        gridDefect.addComponentColumn(key -> {
            Button deleteButton = new Button("Удалить");
            deleteButton.addClickListener(click -> {
                String selectedKey = comboBox.getValue(); // текущий выбранный ключ в ComboBox
                if (selectedKey != null && devices.containsKey(selectedKey)) {
                    devices.get(selectedKey).deviceMap.remove(key);
                    gridDefect.setItems(devices.get(selectedKey).deviceMap.keySet());
                }
            });
            return deleteButton;
        }).setHeader("");

        comboBox.addValueChangeListener(event -> {
            String selectedKey = event.getValue();
            if (selectedKey != null && devices.containsKey(selectedKey)) {
                gridDefect.setItems(devices.get(selectedKey).deviceMap.keySet());
            } else {
                gridDefect.setItems(Collections.emptyList());
            }
        });

        settingsContent.add(addDevice, comboBox, editDefect, gridDefect);
        return settingsContent;
    }
        // наполнение вкладки "учёт"
    private VerticalLayout createDefectContent() {
        VerticalLayout defectContent = new VerticalLayout();
        DatePicker datePicker = new DatePicker("Выберите дату");
        defectContent.add(datePicker);

        datePicker.addValueChangeListener(event -> {
            LocalDate selectedDate = event.getValue();
            if (selectedDate != null) {
                openDeviceSelectionDialog(selectedDate);
                month = selectedDate.getMonthValue();
                day = selectedDate.getDayOfMonth();
            }
        });
        return defectContent;
    }
        // наполнение вкладки "сводка"
    private VerticalLayout createTableDefectContent() {
        VerticalLayout tableDefectLayout = new VerticalLayout();
        initializeMonthSelector();
        configureGrid(grid);
        tableDefectLayout.add(monthSelect, grid);
        updateGrid(grid);
        grid.setItems(devices.values());
        return tableDefectLayout;
    }

    private void initializeMonthSelector() {
        monthSelect.setItems(Month.values());
        monthSelect.setLabel("Выберите месяц");
        monthSelect.addValueChangeListener(e -> updateGrid(grid));
    }

    private void configureGrid(Grid<Device> grid) {
        grid.addClassName("my-custom-grid");
        grid.addColumn(Device::getDeviceName)
                .setHeader("Устройство")
                .setFlexGrow(0)
                .setAutoWidth(true);

        grid.setItems(devices.values());
    }

    private void updateGrid(Grid<Device> grid) {
        Month selectedMonth = monthSelect.getValue();
        if (selectedMonth == null)
            selectedMonth = LocalDate.now().getMonth();
        int monthValue = selectedMonth.getValue();

        List<Integer> days = getDaysInMonth(selectedMonth);

        List<Grid.Column<Device>> columnsToRemove = new ArrayList<>(grid.getColumns().subList(1, grid.getColumns().size()));
        columnsToRemove.forEach(grid::removeColumn);

        days.forEach(day -> {
            grid.addComponentColumn(device -> {
                        int count = 0;
                        if (device.totalPartMap != null) {
                            Map<Integer, Integer> monthMap = device.totalPartMap.get(monthValue);
                            if (monthMap != null) {
                                Integer val = monthMap.get(day);
                                if (val != null) {
                                    count = val;
                                }
                            }
                        }
                        if (count != 0) {
                            Icon content = VaadinIcon.CHECK.create();
                            content.getElement().getThemeList().add("badge success");
                            content.getStyle().set("cursor", "pointer");
                            content.addClickListener(e -> showReportDialog(device, monthValue, day));
                            return content;
                        } else {
                            return new Span("");
                        }
                    })
                    .setHeader(day.toString())
                    .setAutoWidth(true)
                    .setFlexGrow(0);
        });
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

        // Количество в партии на выбранный день
        Integer count = device.totalPartMap
                .get(month)
                .get(day);
        Span countSpan = new Span("Количество в партии: " + count);
        layout.add(countSpan);

        // Заголовок для дефектов
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
        Button downloadReport = new Button("Скачать отчёт");
        StreamResource resource = createExcelResource(device, month, day);
        Anchor downloadLink = new Anchor(resource, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(downloadReport);

        layout.add(closeButton, downloadLink);
        dialog.add(layout);
        dialog.open();
    }

    private List<Integer> getDaysInMonth(Month month) {
        return IntStream.rangeClosed(1, month.length(Year.now().isLeap()))
                .boxed()
                .collect(Collectors.toList());
    }

        // выбор из списка устройств
    private void openDeviceSelectionDialog(LocalDate date) {
        Dialog deviceDialog = new Dialog();
        deviceDialog.setWidth("300px");

        // ListBox для выбора устройства
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

        // окно ввода количества брака
    private void openFormDialog(String deviceName) {
        Dialog formDialog = new Dialog();
        formDialog.setWidth("450px");
        formDialog.setMinHeight("600px");
        FormLayout formLayout = new FormLayout();
        IntegerField partTotalField = new IntegerField("Партия шт.");
        partTotalField.setValue(0);
        ComboBox<String> lineComboBox = new ComboBox<>();
        lineComboBox.setItems("Линия 1" , "Линия 2", "Линия 3", "Линия 4");
        lineComboBox.setValue("Линия 1");
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd/MM/yyyy");  // задаем формат отображения даты
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
        for (String key : devices.get(deviceName).deviceMap.keySet()) {   // строки во временную мапу по ключам из device
            IntegerField field = new IntegerField(key);
            field.setValue(0);
            parameterFields.put(key, field);
            formLayout.add(field);
        }

        Button saveButton = new Button("Сохранить", event -> {
            int batch = partTotalField.getValue();
            Map<String, Integer> params = new HashMap<>();
            parameterFields.forEach((k, v) -> params.put(k, v.getValue()));
            devices.get(deviceName).totalPartMap.get(month).put(day, batch); //прописываем общее количество устройств партии в мапу Device.totalPartMap
            devices.get(deviceName).lineMap.get(month).put(day, lineComboBox.getValue()); // прописываем линию для брака
            params.forEach((defect, volume) -> devices.get(deviceName).deviceMap.get(defect).get(month).put(day, volume)); // прописываем брак по пунктам, месяцу и дню в мапу Device.deviceMap
            devices.get(deviceName).lineMap.get(month).put(day, lineComboBox.getValue());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            devices.get(deviceName).startPartDate.get(month).put(day, startDate.getValue().format(formatter));
            devices.get(deviceName).finishPartDate.get(month).put(day, finishDate.getValue().format(formatter));
            Serial.saveDevice();
            formDialog.close();
        });

        formDialog.add(formLayout, saveButton);
        formDialog.open();
    }

    // Создание StreamResource для отчета
    private StreamResource createExcelResource(Device device, int month, int day) {
        return new StreamResource("Отчёт по " + device.getDeviceName() + ".xlsx", () -> {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Отчет");

                // Стили для ячеек
                CellStyle yellowStyle = workbook.createCellStyle();
                yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                // Получаем данные
                String deviceName = device.getDeviceName();
                String line = device.lineMap.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                String startDate = device.startPartDate.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                String finishDate = device.finishPartDate.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                int totalPart = device.totalPartMap.getOrDefault(month, new HashMap<>()).getOrDefault(day, 0);

                // Собираем все виды брака
                Set<String> defects = device.deviceMap.entrySet().stream()
                        .filter(entry -> {
                            // Получаем карту месяцев
                            HashMap<Integer, HashMap<Integer, Integer>> monthsMap = entry.getValue();

                            // Считаем сумму по всем месяцам и дням
                            int totalCount = monthsMap.values().stream()
                                    .flatMap(daysMap -> daysMap.values().stream())
                                    .mapToInt(Integer::intValue)
                                    .sum();

                            // Фильтруем по сумме > 0
                            return totalCount != 0;
                        })
                        .map(Map.Entry::getKey)  // берем ключ (название брака)
                        .collect(Collectors.toSet());

                // Считаем общее количество брака
                int defectTotal = 0;
                Map<String, Integer> defectCounts = new LinkedHashMap<>();
                for (String defect : defects) {
                    int count = device.deviceMap.get(defect)
                            .getOrDefault(month, new HashMap<>())
                            .getOrDefault(day, 0);
                    defectCounts.put(defect, count);
                    defectTotal += count;
                }

                // Считаем процент брака
                double defectPercent = totalPart > 0 ? (double) defectTotal / totalPart * 100 : 0;

                int rowNum = 0;

                // 1. Линия и диапазон дат
                Row row1 = sheet.createRow(rowNum++);
                row1.createCell(0).setCellValue(line);
                row1.createCell(1).setCellValue(deviceName);

                Row row2 = sheet.createRow(rowNum++);
                row2.createCell(0).setCellValue(startDate + " - " + finishDate);

                // 2. Партия
                Row row3 = sheet.createRow(rowNum++);
                row3.createCell(0).setCellValue("Партия (шт)");
                row3.createCell(1).setCellValue(totalPart);

                // 3. Брак по видам
                for (String defect : defects) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(defect);
                    row.createCell(1).setCellValue(defectCounts.get(defect));
                }

                // 4. Итого (жёлтая строка)
                Row rowItogo = sheet.createRow(rowNum++);
                Cell cellItogo = rowItogo.createCell(0);
                cellItogo.setCellValue("итого");
                cellItogo.setCellStyle(yellowStyle);

                Cell cellTotalDefect = rowItogo.createCell(1);
                cellTotalDefect.setCellValue(defectTotal);
                cellTotalDefect.setCellStyle(yellowStyle);

                // 5. Процент брака (жёлтая строка)
                Row rowPercent = sheet.createRow(rowNum++);
                rowPercent.createCell(0).setCellValue("процент брака");
                rowPercent.createCell(1).setCellValue(String.format("%.2f%%", defectPercent));

                // Авто ширина
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                workbook.write(bos);
                workbook.close();

                return new ByteArrayInputStream(bos.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
