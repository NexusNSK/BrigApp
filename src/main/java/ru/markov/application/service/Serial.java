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
        try {
            System.out.println("...Ищу данные о составе бригады...\n / | \\");
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            BrigEdit.workerList = (List<Worker>) ois.readObject();
            System.out.println("...Состав бригады был успешно загружен из основного файла...");
            ois.close();
        } catch (IOException e) {
            System.out.println("...Основной файл с данными о составе бригады пуст или не найден...");
            System.out.println("...Произвожу попытку загрузки из резервного файла...");
            try {
                final String fullFilename = workDir + File.separator + filename;
                System.out.println("...Ищу данные о составе бригады...\n / | \\");
                FileInputStream fisReserv = new FileInputStream(fullFilename);
                ObjectInputStream oisReserv = new ObjectInputStream(fisReserv);
                BrigEdit.workerList = (List<Worker>) oisReserv.readObject();
                System.out.println("...Состав бригады был успешно загружен из резервного файла...");
                oisReserv.close();
            } catch (IOException ex) {
                System.out.println("...Резервный файл с данными о составе бригады пуст или не найден...");
            }
        }
    }

    public static void loadDevice() throws ClassNotFoundException {
        try {
            System.out.println("...Ищу данные о списке устройств...\n / | \\");
            FileInputStream fis = new FileInputStream(workDir + File.separator + defect_devise_file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DeviceDefectView.devices = (HashMap<String, Device>) ois.readObject();
            System.out.println("...Список устройств был успешно загружен из основного файла...");
            ois.close();
            System.out.println("Загружено устройств: " + DeviceDefectView.devices.size());
        } catch (IOException e) {
            DeviceDefectView.devices = new HashMap<>();
            System.out.println("Файл \"device.bin\" не был загружен");
        }
    }

    public static void savePreset(){
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

    public static void loadPreset(){
        try {
            System.out.println("...Ищу данные о пресетах брака...\n / | \\");
            FileInputStream fis = new FileInputStream(workDir + File.separator + defect_presets);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DeviceDefectView.familyDefectList = (HashMap<String, ArrayList<String>>) ois.readObject();
            System.out.println("...Список пресетов был успешно загружен из основного файла...");
            ois.close();
            System.out.println("Загружено пресетов: " + DeviceDefectView.familyDefectList.size());
        } catch (IOException e) {
            DeviceDefectView.familyDefectList = new HashMap<>();
            System.out.println("Файл \"defect presets.srl\" не был загружен");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
