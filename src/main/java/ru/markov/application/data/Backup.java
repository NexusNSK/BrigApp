package ru.markov.application.data;

import ru.markov.application.views.BrigEdit;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class Backup extends Thread {
    public Backup(){
        try {
            Path backup = Files.createDirectories(Path.of(System.getProperty("user.home")+ File.separator+"Backup worker list"));
            System.out.println("Директория Backup worker list была создана");
        } catch (IOException e) {
            System.out.println("Невозможно создать директорию");
        }
    }
    private static final String filename = "worker list("+LocalDateTime.now().getDayOfMonth()+" "+LocalDateTime.now().getMonth()+").bin";
    private static final String workDir = System.getProperty("user.home");
    boolean hasBackup = false;
    @Override
    public void run() {

        while (true) {
            if (LocalDateTime.now().getHour() == 0&!hasBackup) {
                try {
                    final String fullFilename = workDir + File.separator +"Backup worker list"+File.separator+ filename;
                    FileOutputStream fos = new FileOutputStream(fullFilename);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(BrigEdit.workerList);
                    System.out.println("Backup создан");
                    oos.close();
                    hasBackup=true;
                    try (FileChannel src = new FileInputStream(fullFilename).getChannel();
                         FileChannel dest = new FileOutputStream("D:\\" + filename).getChannel()) {
                        dest.transferFrom(src, 0, src.size());
                        System.out.println("Копирование файла на внешний ресурс выполнено");
                    }
                } catch (IOException e){
                    System.out.println("ошибка при создании бэкапа");
                }
            } else {
                hasBackup = false;
               try {
                    Thread.sleep(60*60*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

