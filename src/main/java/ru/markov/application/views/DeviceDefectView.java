package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.Device;
import ru.markov.application.service.Serial;

import java.util.*;

@Route(value = "device_defect", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class DeviceDefectView extends VerticalLayout {
  public static HashMap<String, Device> devices = new HashMap<>();

    public DeviceDefectView() {
        Button addDevice = new Button("Добавить устройство");
        ComboBox<String> comboBox = new ComboBox<>("Выберите устройство");
        comboBox.setItems(devices.keySet());
        comboBox.setValue("Список устройств");
        addDevice.addClickListener(event -> {
                // Открываем диалог для ввода нового устройства
                Dialog dialogAddDevice = new Dialog();
                TextField newDeviceField = new TextField("Введите название устройства");
                Button addButton = new Button("Добавить", e -> {
                    String newDevice = newDeviceField.getValue();
                    if (newDevice != null && !newDevice.trim().isEmpty()) {
                        devices.put(newDevice, new Device(newDevice));
                        dialogAddDevice.close();
                    }
                    comboBox.setItems(devices.keySet());
                    comboBox.setValue(devices.keySet().iterator().next());
                    Serial.saveDevice();
                });
                VerticalLayout layout = new VerticalLayout(newDeviceField, addButton);
                dialogAddDevice.add(layout);
                dialogAddDevice.open();
            }
        );

        //
        Dialog dialogDefect = new Dialog();
        VerticalLayout itemsLayout = new VerticalLayout();
        Button addItemButton = new Button("Добавить пункт");

        addItemButton.addClickListener(e -> {
            TextField keyField = new TextField("Наименование брака");
            itemsLayout.add(keyField);
        });

        Button saveButton = new Button("Сохранить", e -> {
            String selectedKey = comboBox.getValue();
            if (selectedKey != null) {
                int autoValue = 0;
                for (Component comp : itemsLayout.getChildren().toList()) {
                    if (comp instanceof TextField) {
                        TextField keyField = (TextField) comp;
                        String key = keyField.getValue();
                        if (key != null && !key.isEmpty()) {
                            devices.get(selectedKey).deviceMap.put(key,new HashMap<>(12));
                        }
                    }
                }
                System.out.println(devices.get(selectedKey).deviceMap.toString());
                dialogDefect.close();
                Serial.saveDevice();
            }
        });

        dialogDefect.add(itemsLayout, addItemButton, saveButton);
        Button editDefect = new Button("Редактировать список брака");
        editDefect.addClickListener(e -> {

            if (comboBox.getValue() != null) {
                itemsLayout.removeAll();
                addItemButton.click(); // добавляем одну пустую строку
                dialogDefect.open();
        }
            });
//        comboBox.addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                itemsLayout.removeAll();
//                addItemButton.click(); // добавляем одну пустую строку
//                dialogDefect.open();
//            }
//        });
        //

        Grid<String> grid = new Grid<>();
        grid.addColumn(key -> key).setHeader("Наименование брака");

// При выборе элемента в ComboBox обновляем Grid, показывая выбранный ключ
        comboBox.addValueChangeListener(event -> {
            String selectedKey = event.getValue();
            if (selectedKey != null) {
                grid.setItems(devices.get(selectedKey).deviceMap.keySet()); // отображаем выбранный ключ
            } else {
                grid.setItems(); // пустой список, если ничего не выбрано
            }
        });
add(addDevice, comboBox, editDefect, grid);
    }
}
