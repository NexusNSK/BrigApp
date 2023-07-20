package ru.markov.application.data;

import ru.markov.application.views.BrigEditor;

import java.io.*;
import java.util.List;

public class Seria {
    public static void save() {
        try {
            FileOutputStream fos = new FileOutputStream("worker list.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(BrigEditor.workerList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() throws IOException, ClassNotFoundException {
        try {
            System.out.println("...Ищу данные о составе бригады...\n / | \\");
            FileInputStream fis = new FileInputStream("worker list.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            BrigEditor.workerList = (List<Worker>) ois.readObject();
            System.out.println("...Состав бригады был успешно загружен...");
            ois.close();
        } catch (IOException e) {
            System.out.println("...Файл с данными о составе бригады пуст или не найден...");
        }
    }
}