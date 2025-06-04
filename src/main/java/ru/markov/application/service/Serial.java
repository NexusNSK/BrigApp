package ru.markov.application.service;

import ru.markov.application.data.Device;
import ru.markov.application.data.Worker;
import ru.markov.application.views.BrigEdit;
import ru.markov.application.views.DeviceDefectView;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Serial {
    private static final String filename = "worker list.bin";
    private static final String defect_devise_file = "device.bin";

    private static final String workDir = System.getProperty("user.home");


    public static void save() {
        try {
            final String fullFilename = workDir + File.separator + filename;
            FileOutputStream fos = new FileOutputStream(fullFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(BrigEdit.workerList);
            oos.close();
            //System.out.println("Файл .bin был записан в резервную директорию " + workDir);
            fos = new FileOutputStream("worker list.bin");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(BrigEdit.workerList);
            oos.close();
            //System.out.println("Файл .bin был записан в основную директорию ");
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
            DeviceDefectView.devices = new HashMap<String, Device>();
            System.out.println("Файл \"device.bin\" не был загружен");
        }
    }
}
