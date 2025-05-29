package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = "device_defect", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class DeviceDefectView extends VerticalLayout {
    List<String> devices = new ArrayList<>(Arrays.asList("RG-5440G-Wac","RG-1440G-Wac", "Добавить устройство"));

    public DeviceDefectView() {
        ComboBox<String> comboBox = new ComboBox<>("Выберите устройство");
        comboBox.setItems(devices);

        comboBox.addValueChangeListener(event -> {
            String selected = event.getValue();
            if ("Добавить устройство".equals(selected)) {
                // Открываем диалог для ввода нового устройства
                Dialog dialog = new Dialog();
                TextField newDeviceField = new TextField("Введите название устройства");
                Button addButton = new Button("Добавить", e -> {
                    String newDevice = newDeviceField.getValue();
                    if (newDevice != null && !newDevice.trim().isEmpty()) {
                        devices.add(newDevice);
                        // Обновляем ComboBox, исключая "Добавить устройство" на время обновления
                        List<String> updatedItems = new ArrayList<>(devices);
                        updatedItems.remove("Добавить устройство");
                        updatedItems.add("Добавить устройство");
                        comboBox.setItems(devices);
                        comboBox.setValue(newDevice); // Выбираем добавленное устройство
                        dialog.close();
                    }
                });
                VerticalLayout layout = new VerticalLayout(newDeviceField, addButton);
                dialog.add(layout);
                dialog.open();
            }
        });
add(comboBox);
    }
}
