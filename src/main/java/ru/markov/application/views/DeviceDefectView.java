package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.Device;
import ru.markov.application.service.Serial;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Route(value = "device_defect", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class DeviceDefectView extends VerticalLayout {
    public static HashMap<String, Device> devices = new HashMap<>();

    public DeviceDefectView() {
        Tabs tabs = new Tabs();
        VerticalLayout contentArea = new VerticalLayout();
        Tab settingsTab = new Tab("Настройки");
        VerticalLayout settingsContent = createSettingsContent();
        Tab emptyTab = new Tab("Учёт");
        VerticalLayout defectContent = createDefectContent();
        tabs.add(emptyTab, settingsTab);

        // чтобы вкладки перерисовывали содержимое
        tabs.addSelectedChangeListener(event -> {
            contentArea.removeAll();
            if (event.getSelectedTab().equals(settingsTab)) {
                contentArea.add(settingsContent);
            } else {
                contentArea.add(defectContent);
            }
        });
        // дефолтное содержимое (учёт)
        contentArea.add(defectContent);
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

        // ... остальной код из оригинального конструктора ...
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
                            devices.get(selectedKey).deviceMap.put(key, new HashMap<>(12));
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
            }
        });
        return defectContent;
    }

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

    private void openFormDialog(String deviceName) {
        Dialog formDialog = new Dialog();
        formDialog.setWidth("400px");

        FormLayout formLayout = new FormLayout();

        TextField partTotalField = new TextField("Партия шт.");
        formLayout.add(partTotalField);

        // строки по ключам из device
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

            // Пример вывода в консоль
            System.out.println("Устройство: " + deviceName);
            System.out.println("Партия шт.: " + batch);
            params.forEach((k, v) -> System.out.println(k + ": " + v));

            formDialog.close();
        });

        formDialog.add(formLayout, saveButton);
        formDialog.open();
    }
}
