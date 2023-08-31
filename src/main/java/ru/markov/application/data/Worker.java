package ru.markov.application.data;

import org.springframework.context.annotation.ComponentScan;
import ru.markov.application.service.*;
import ru.markov.application.views.Reports;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@ComponentScan

public class Worker implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String patronymic;
    private District district;
    private Post post;
    private ConveyLine line;

    private final HashMap<Integer, HashMap<Integer, WorkerStatus>> workerStatusMassive = new HashMap<>(12);
    private final HashMap<Integer, HashMap<Integer, Integer>> workTimeMassive = new HashMap<>(12);
    //             <номер месяца : мапа <номер дня : часы>>

    public void initWorkerStatusMap() {
        if (workerStatusMassive.isEmpty()) {
            for (int i = 0; i <= 12; i++) {
                workerStatusMassive.put(i, new HashMap<>(31));
                for (int j = 0; j <= 32; j++) {
                    workerStatusMassive.get(i).put(j, WorkerStatus.NOTHING);
                }
            }
            System.out.println("Создание карты учета статуса работника завершено!");
        }
    }

    public void initWorkTimeMap() {
        if (workTimeMassive.isEmpty()) {
            for (int i = 0; i <= 12; i++) {
                workTimeMassive.put(i, new HashMap<>(31));
                for (int j = 0; j <= 31; j++) {
                    workTimeMassive.get(i).put(j, 0);
                }
            }
            System.out.println("Создание карты учета времемни завершено!");
        }
    }

    public void setWorkTime(int hours) {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), hours);
    }

    public void setWorkTimeLikeYesterday() {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkTimeLikeYesterday());
    }

    public int getWorkTime() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
    }
    public int getWorkTimeLikeYesterday() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()-1);
    }

    public int getWorkTimeToPOI(int day) {
        return workTimeMassive.get(Reports.month).get(day);
    }

    public String getPost() {
        String ps = "";
        switch (post) {
            case BRIG_MOUNT -> ps = "Бригадир монтажников";
            case BRIG_BUILD -> ps = "Бригадир сборщиков";
            case BRIG_TECH -> ps = "Бригадир техников";
            case MOUNT -> ps = "Монтажник";
            case BUILDER -> ps = "Сборщик";
            case TECHNIC -> ps = "Техник";
        }
        return ps;
    }

    public void setPost(String post) {
        switch (post) {
            case ("Бригадир монтажников") -> this.post = Post.BRIG_MOUNT;
            case ("Бригадир сборщиков") -> this.post = Post.BRIG_BUILD;
            case ("Бригадир техников") -> this.post = Post.BRIG_TECH;
            case ("Монтажник") -> this.post = Post.MOUNT;
            case ("Сборщик") -> this.post = Post.BUILDER;
            case ("Техник") -> this.post = Post.TECHNIC;
        }
    }

    public String getDistrictToString() {
        return switch (district) {
            case MOUNTING -> "Бригада монтажники";
            case BUILDING -> "Бригада сборщики";
            case TECH -> "Бригада техники";
        };
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        switch (district) {
            case ("Бригада монтажники") -> this.district = District.MOUNTING;
            case ("Бригада сборщики") -> this.district = District.BUILDING;
            case ("Бригада техники") -> this.district = District.TECH;
        }
    }

    public Worker(String lastName, String firstName, String patronymic, String line, String district, String post) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        setLine(line);
        setDistrict(district);
        setPost(post);
        initWorkTimeMap();
        initWorkerStatusMap();
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + patronymic;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setWorkerStatusMassive(String status) {
        switch (status) {
            case ("10") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
                setWorkTime(10);
            }
            case ("9") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
                setWorkTime(9);
            }
            case ("8") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
                setWorkTime(8);
            }

            case ("БОЛ") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOSPITAL);
                setWorkTime(0);
            }
            case ("ОТП") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOLIDAY);
                setWorkTime(0);
            }
            case ("ОТГ") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.OTRABOTKA);
                setWorkTime(0);
            }
            case ("АДМ") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.ADMINOTP);
                setWorkTime(0);
            }
            case ("0") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.NOTHING);
                setWorkTime(0);
            }

        }
    }

    public void setWorkerStatusMassiveLikeYesterday() {
        workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkerStatusLikeYesterday());
    }
    private WorkerStatus getWorkerStatusLikeYesterday() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()-1);
    }

    public String getWorkerStatusMassive() {
        return switch (workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth())) {
            case WORK, OTRABOTKA -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "---";
            case ADMINOTP -> "Админ. отпуск";
        };
    }
    public WorkerStatus getWorkerStatus() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
        }


    public String getWorkerStatusAtDay(int day) {
        return switch (workerStatusMassive.get(Reports.month).get(day)) {
            case WORK, OTRABOTKA -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "---";
            case ADMINOTP -> "Админ. отпуск";
        };
    }

    public WorkerStatus getWorkerStatusAtDayToRepo(int day) {
        return workerStatusMassive.get(Reports.month).get(day);
    }

    public String getLineToString() {
        return switch (line) {
            case LINE_1 -> "1";
            case LINE_2 -> "2";
            case LINE_3 -> "3";
            case LINE_4 -> "4";
            default -> "---";
        };
    }

    public ConveyLine getLine() {
        return this.line;
    }

    public void setLine(String line) {
        switch (line) {
            default -> this.line = ConveyLine.COMMON;
            case "1" -> this.line = ConveyLine.LINE_1;
            case "2" -> this.line = ConveyLine.LINE_2;
            case "3" -> this.line = ConveyLine.LINE_3;
            case "4" -> this.line = ConveyLine.LINE_4;
        }
    }
}

