package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.data.Device;
import ru.markov.application.service.Serial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Route(value = "device_defect", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class DeviceDefectView extends VerticalLayout {

    public static HashMap<String, Device> devices = new HashMap<>();
    private int month;
    private int day;
    private ComboBox<Month> monthSelect = new ComboBox<>();
    Grid<Device> grid = new Grid<>(Device.class, false);


    public DeviceDefectView() {
        Tabs tabs = new Tabs();
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
            String selectedKey = comboBox.getValue();
            if (selectedKey != null && !selectedKey.equals("Список устройств")) {
                for (Component comp : itemsLayout.getChildren().toList()) {
                    if (comp instanceof TextField keyField) {
                        String key = keyField.getValue();
                        if (key != null && !key.isEmpty()) {
                            //devices.get(selectedKey).deviceMap.put(key, new HashMap<>(12));
                            devices.get(selectedKey).initMapWithDefect(key);
                        }
                    }
                }
                dialogDefect.close();
                Serial.saveDevice();
            }
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

        Grid<String> grid = new Grid<>();
        grid.addColumn(key -> key).setHeader("Наименование брака");

        comboBox.addValueChangeListener(event -> {
            String selectedKey = event.getValue();
            if (selectedKey != null) {
                grid.setItems(devices.get(selectedKey).deviceMap.keySet());
            } else {
                grid.setItems();
            }
        });

        settingsContent.add(addDevice, comboBox, editDefect, grid);
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

    private void configureGrid(Grid <Device> grid) {
        grid.addColumn(Device::getDeviceName).setHeader("Устройство");

        grid.setItems(devices.values());
    }

    private void updateGrid(Grid grid) {
        Month selectedMonth = monthSelect.getValue();

        if (selectedMonth == null)
            selectedMonth = LocalDate.now().getMonth();

        int monthValue = selectedMonth.getValue();
        List<Integer> days = getDaysInMonth(selectedMonth);

        List<Grid.Column<Device>> columnsToRemove = new ArrayList<>(grid.getColumns().subList(1, grid.getColumns().size()));
        columnsToRemove.forEach(grid::removeColumn);


        // Добавляем новые колонки дней
        days.forEach(day -> {
            grid.addComponentColumn(device -> {
                        Integer count = devices.get(device.toString()).totalPartMap.get(monthValue)
                                .get(day);

                        Span content = new Span(count != null ? count.toString() : "");
                        if (count != null) {
                            content.addClickListener(e -> showReportDialog(devices.get(device.toString()), monthValue, day));

                            content.getStyle().setCursor("pointer");
                            content.getStyle().setColor("var(--lumo-primary-text-color)");
                        }
                        return content;
                    }).setHeader(day.toString())
                    .setAutoWidth(true)
                    .setFlexGrow(1);
        });
    }
    private void showReportDialog(Device device, int month, int day) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setHeight("300px");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        // Заголовок с названием устройства
        H3 title = new H3("Отчет по устройству: " + device.getDeviceName());
        layout.add(title);

        // Количество в партии на выбранный день
        Integer count = device.totalPartMap
                .get(month)
                .get(day);
        Span countSpan = new Span("Количество в партии: " + count);
        layout.add(countSpan);

        // Заголовок для дефектов
        H4 defectsTitle = new H4("Брак в этот день:");
        layout.add(defectsTitle);

        // Список браков с количеством > 0
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

        // Кнопка закрытия
        Button closeButton = new Button("Закрыть", e -> dialog.close());
        Button downloadReport = new Button("Скачать отчёт", event -> generateExcelReport(device.getDeviceName(), month, day));
        layout.add(closeButton, downloadReport);
        dialog.add(layout);
        dialog.open();
    }

    private void generateExcelReport(String deviceName, int month, int day) {
        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet("Отчет по браку " + deviceName + " " + day+"."+month);

            // Заголовки
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Устройство");
            headerRow.createCell(1).setCellValue("Количество");

            // Данные
            int rowNum = 1;
            for (Device device : devices.values()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(device.getDeviceName());
                row.createCell(1).setCellValue(device.totalPartMap.get(month).get(day));
            }

            // Сохранение файла
            StreamResource resource = new StreamResource("report.xlsx", () -> {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    workbook.write(stream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new ByteArrayInputStream(stream.toByteArray());
            });

            Anchor downloadLink = new Anchor(resource, "Скачать отчет");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.setVisible(false); // скрываем ссылку, если нужно

            add(downloadLink);

// Программно кликаем по ссылке, чтобы инициировать скачивание
            downloadLink.getElement().callJsFunction("click");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        formDialog.setWidth("400px");

        FormLayout formLayout = new FormLayout();

        TextField partTotalField = new TextField("Партия шт.");
        formLayout.add(partTotalField);

        // строки во временную мапу по ключам из device
        Map<String, TextField> parameterFields = new HashMap<>();
        for (String key : devices.get(deviceName).deviceMap.keySet()) {
            TextField field = new TextField(key);
            parameterFields.put(key, field);
            formLayout.add(field);
        }

        Button saveButton = new Button("Сохранить", event -> {
            String batch = partTotalField.getValue();
            Map<String, String> params = new HashMap<>();
            parameterFields.forEach((k, v) -> params.put(k, v.getValue()));

            //прописываем общее количество устройств партии в мапу Device.totalPartMap
            devices.get(deviceName).totalPartMap.get(month).put(day, Integer.valueOf(batch));

            // прописываем брак по пунктам, месяцу и дню в мапу Device.deviceMap
            params.forEach((defect, volume) -> devices.get(deviceName).deviceMap.get(defect).get(month).put(day, Integer.valueOf(volume)));

            // вывод в консоль
            //System.out.println("Устройство: " + deviceName);
            //System.out.println("Партия шт.: " + batch);
            //params.forEach((k, v) -> System.out.println(k + ": " + v));

            // вывод в консоль сохраненного количества брака
            //devices.get(deviceName).printNestedMap(devices.get(deviceName).deviceMap, 0);
            Serial.saveDevice();
            formDialog.close();
        });

        formDialog.add(formLayout, saveButton);
        formDialog.open();
    }
}
