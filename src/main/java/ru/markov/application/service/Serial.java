package ru.markov.application.service;

import ru.markov.application.data.Worker;
import ru.markov.application.views.GridEdit;

import java.io.*;
import java.util.List;

public class Serial {
    private static final String filename = "worker list.bin";

    private static final String workDir = System.getProperty("user.home");


    public static void save() {
        try {
            final String fullFilename = workDir + File.separator + filename;
            FileOutputStream fos = new FileOutputStream(fullFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(GridEdit.workerList);
            oos.close();
            System.out.println("Файл .bin был записан в резервную директорию " + workDir);
            fos = new FileOutputStream("worker list.bin");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(GridEdit.workerList);
            oos.close();
            System.out.println("Файл .bin был записан в основрую директорию ");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
/*
    public static void saveMigration(){
        try{final String fullFilename = workDir + File.separator + "migration.bin";
            FileOutputStream fos = new FileOutputStream(fullFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(WhoNext.people);
            oos.close();
            System.out.println("Файл migration.bin был записан в резервную директорию " + workDir);
            fos = new FileOutputStream("migration.bin");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(WhoNext.people);
            oos.close();
            System.out.println("Файл migration.bin был записан в основрую директорию ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMigration() throws ClassNotFoundException {
        try {
            System.out.println("...Ищу данные о миграции техников по линиям...\n / | \\");
            FileInputStream fis = new FileInputStream("migration.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            WhoNext.people = (ArrayList<Worker>) ois.readObject();
            System.out.println("...Список миграции был успешно загружен из основного файла...");
            ois.close();
        } catch (IOException e) {
            System.out.println("...Основной файл с данными о миграции бригады пуст или не найден...");
            System.out.println("...Произвожу попытку загрузки из резервного файла...");
            try {
                final String fullFilename = workDir + File.separator + "migration.bin";
                System.out.println("...Ищу данные о миграции техников по линиям...\n / | \\");
                FileInputStream fisReserv = new FileInputStream(fullFilename);
                ObjectInputStream oisReserv = new ObjectInputStream(fisReserv);
                WhoNext.people = (ArrayList<Worker>) oisReserv.readObject();
                System.out.println("...Список миграции был успешно загружен из резервного файла...");
                oisReserv.close();
            } catch (IOException ex) {
                System.out.println("...Резервный файл с данными о миграции бригады пуст или не найден...");
            }
        }
    }
    */

    public static void load() {
        try {
            System.out.println("...Ищу данные о составе бригады...\n / | \\");
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GridEdit.workerList = (List<Worker>) ois.readObject();
            System.out.println("...Состав бригады был успешно загружен из основного файла...");
            ois.close();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("...Основной файл с данными о составе бригады пуст или не найден...");
            System.out.println("...Произвожу попытку загрузки из резервного файла...");
            try {
                final String fullFilename = workDir + File.separator + filename;
                System.out.println("...Ищу данные о составе бригады...\n / | \\");
                FileInputStream fisReserv = new FileInputStream(fullFilename);
                ObjectInputStream oisReserv = new ObjectInputStream(fisReserv);
                GridEdit.workerList = (List<Worker>) oisReserv.readObject();
                System.out.println("...Состав бригады был успешно загружен из резервного файла...");
                oisReserv.close();
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("...Резервный файл с данными о составе бригады пуст или не найден...");
            }
        }
    }
}
