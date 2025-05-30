package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.Device;

import java.util.ArrayList;
import java.util.List;

@Route(value = "device_defect", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class DeviceDefectView extends VerticalLayout {
  public static List<Device> devices = new ArrayList<>();

    public DeviceDefectView() {
        Button addDevice = new Button("Добавить устройство");
        ComboBox<String> comboBox = new ComboBox<>("Выберите устройство");
        comboBox.setAllowCustomValue(true);
        comboBox.addCustomValueSetListener(event -> {
            String customValue = event.getDetail();
            devices.add(new Device(customValue));
            comboBox.setItems(devices.toString());
            comboBox.setValue(customValue);
        });
        for (int i = 0; i < devices.size(); i++) {
            comboBox.setItems(devices.get(i).toString());
        }
        addDevice.addClickListener(event -> {
                // Открываем диалог для ввода нового устройства
                Dialog dialog = new Dialog();
                TextField newDeviceField = new TextField("Введите название устройства");
                Button addButton = new Button("Добавить", e -> {
                    String newDevice = newDeviceField.getValue();
                    if (newDevice != null && !newDevice.trim().isEmpty()) {

                        devices.add(new Device(newDevice));
                        // Обновляем ComboBox, исключая "Добавить устройство" на время обновления
                        List<Device> updatedItems = new ArrayList<>(devices);
                        comboBox.setItems(updatedItems.toString());
                        comboBox.setValue(newDevice); // Выбираем добавленное устройство
                        dialog.close();
                    }
                });
                VerticalLayout layout = new VerticalLayout(newDeviceField, addButton);
                dialog.add(layout);
                dialog.open();
            }
        );
add(addDevice, comboBox);
    }
}
