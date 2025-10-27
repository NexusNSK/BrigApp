package ru.markov.application.service;

import ru.markov.application.data.Device;
import ru.markov.application.data.Worker;
import ru.markov.application.views.BrigEdit;
import ru.markov.application.views.DeviceDefectView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Serial {
    private static final String filename = "worker list.bin";
    private static final String defect_devise_file = "device.bin";
    private static final String defect_presets = "defect presets.srl";

    private static final String workDir = System.getProperty("user.home");


    private static void printBoxedMessageAligned(String title, List<String> messages) {
        int maxLength = title.length();
        for (String msg : messages) {
            if (msg.length() > maxLength) {
                maxLength = msg.length();
            }
        }
        String border = "╔" + "═".repeat(maxLength + 2) + "╗";
        String bottomBorder = "╚" + "═".repeat(maxLength + 2) + "╝";

        System.out.println(border);
        System.out.println("║ " + title + " ".repeat(maxLength - title.length()) + " ║");
        System.out.println("╠" + "═".repeat(maxLength + 2) + "╣");

        for (String msg : messages) {
            System.out.println("║ " + msg + " ".repeat(maxLength - msg.length()) + " ║");
        }

        System.out.println(bottomBorder);
    }

    public static void save() {
        try {
            final String fullFilename = workDir + File.separator + filename;
            FileOutputStream fos = new FileOutputStream(fullFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(BrigEdit.workerList);
            oos.close();
            fos = new FileOutputStream("worker list.bin");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(BrigEdit.workerList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDevice() {
        try {
            final String fullFilename = workDir + File.separator + defect_devise_file;
            FileOutputStream fos = new FileOutputStream(fullFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(DeviceDefectView.devices);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() throws ClassNotFoundException {
        List<String> messages = new ArrayList<>();
        messages.add("Ищу данные о составе бригады...");
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            BrigEdit.workerList = (List<Worker>) ois.readObject();
            messages.add("Состав бригады успешно загружен из основного файла.");
            messages.add("Загружено сотрудников: " + BrigEdit.workerList.size());
            ois.close();
        } catch (IOException e) {
            messages.add("Основной файл с данными о составе бригады пуст или не найден.");
            messages.add("Произвожу попытку загрузки из резервного файла...");
            try {
                final String fullFilename = workDir + File.separator + filename;
                messages.add("Ищу данные о составе бригады...");
                FileInputStream fisReserv = new FileInputStream(fullFilename);
                ObjectInputStream oisReserv = new ObjectInputStream(fisReserv);
                BrigEdit.workerList = (List<Worker>) oisReserv.readObject();
                messages.add("Состав бригады успешно загружен из резервного файла.");
                oisReserv.close();
            } catch (IOException ex) {
                messages.add("Резервный файл с данными о составе бригады пуст или не найден.");
            }
        }
        printBoxedMessageAligned("Загрузка состава бригады", messages);
    }

    public static void loadDevice() throws ClassNotFoundException {
        List<String> messages = new ArrayList<>();
        messages.add("Ищу данные о списке устройств...");
        try {
            FileInputStream fis = new FileInputStream(workDir + File.separator + defect_devise_file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DeviceDefectView.devices = (HashMap<String, Device>) ois.readObject();
            messages.add("Список устройств был успешно загружен из основного файла.");
            ois.close();
            messages.add("Загружено устройств: " + DeviceDefectView.devices.size());
        } catch (IOException e) {
            DeviceDefectView.devices = new HashMap<>();
            messages.add("Файл \"device.bin\" не был загружен.");
        }
        printBoxedMessageAligned("Загрузка списка устройств", messages);
    }

    public static void savePreset() {
        try {
            final String fullFilename = workDir + File.separator + defect_presets;
            FileOutputStream fos = new FileOutputStream(fullFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(DeviceDefectView.familyDefectList);
            oos.close();
            fos = new FileOutputStream("defect presets.srl");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(DeviceDefectView.familyDefectList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPreset() {
        List<String> messages = new ArrayList<>();
        messages.add("Ищу данные о пресетах брака...");
        try {
            FileInputStream fis = new FileInputStream(workDir + File.separator + defect_presets);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DeviceDefectView.familyDefectList = (HashMap<String, ArrayList<String>>) ois.readObject();
            messages.add("Список пресетов был успешно загружен из основного файла.");
            ois.close();
            messages.add("Загружено пресетов: " + DeviceDefectView.familyDefectList.size());
        } catch (IOException e) {
            DeviceDefectView.familyDefectList = new HashMap<>();
            messages.add("Файл \"defect presets.srl\" не был загружен.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        printBoxedMessageAligned("Загрузка пресетов брака", messages);
    }
}