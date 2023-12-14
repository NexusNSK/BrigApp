package ru.markov.application.data;

import ru.markov.application.views.GridEdit;

import java.io.*;
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
                    oos.writeObject(GridEdit.workerList);
                    System.out.println("Backup создан");
                    oos.close();
                    hasBackup=true;
                } catch (IOException e){
                    e.printStackTrace();
                }
            } else {
                hasBackup = false;
               try {
                   System.out.println("Следующая попытка бэкапа через 1 час");
                    Thread.sleep(60*60*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

