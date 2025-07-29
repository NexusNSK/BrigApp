package ru.markov.application.data;

import ru.markov.application.service.*;
import ru.markov.application.views.Reports;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Worker implements Serializable, Comparable<Worker> {
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
    //            hashmap <номер месяца : hashmap  <номер дня : ч  асы>>

    public void initWorkerStatusMap() {
        if (workerStatusMassive.isEmpty()) {
            for (int i = 0; i <= 12; i++) {
                workerStatusMassive.put(i, new HashMap<>(31));
                for (int j = 0; j <= 32; j++) {
                    workerStatusMassive.get(i).put(j, WorkerStatus.NOTHING);
                }
            }
            //System.out.println(getFullName() +  ": Создание карты учета статуса работника завершено!");
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
            //System.out.println(getFullName() +  ": Создание карты учета времени завершено!");
        }
    }

    public void setWorkTime(int hours) {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), hours);

        if (hours > 8) {
            workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                    .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.PERERABOTKA);
        } else if (hours > 0 && hours < 8) {
            workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                    .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.OTRABOTKA);
        } else if (hours == 8) {
            workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                    .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
        }
    }

    // проверка времени и статуса для "как вчера"
    public boolean checkTimeAndStatus(){
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()).equals(WorkerStatus.NOTHING);
    }

    // время и статус как вчера
    public void setWorkTimeLikeYesterday() {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), (Integer) getWorkTimeLikeYesterday().get(0));
        workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), (WorkerStatus) getWorkTimeLikeYesterday().get(1));
    }

    // получить время в конкретный день
    public int getWorkTime() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
    }

    public ArrayList<Object> getWorkTimeLikeYesterday() {
        ArrayList<Object> result = new ArrayList<>();
        int defaultWorkTime = 0;
        WorkerStatus defaultStatus = WorkerStatus.NOTHING;

        LocalDate date = TimeAdapter.workTimeDatePicker.getValue();
        date = date.minusDays(1);  // старт с предыдущего дня

        LocalDate yearStart = LocalDate.of(date.getYear(), 1, 1);

        while (!date.isBefore(yearStart)) {
            int month = date.getMonthValue();     // 1..12
            int day = date.getDayOfMonth();       // 1..31

            HashMap<Integer, Integer> workTimeDayMap = workTimeMassive.get(month);
            HashMap<Integer, WorkerStatus> statusDayMap = workerStatusMassive.get(month);

            if (workTimeDayMap != null && statusDayMap != null) {
                Integer workTime = workTimeDayMap.get(day);
                WorkerStatus status = statusDayMap.get(day);

                if (workTime != null && status != null) {
                    if (!(workTime == 0 && status == WorkerStatus.NOTHING)) {
                        result.add(workTime);
                        result.add(status);
                        return result;
                    }
                }
            }

            date = date.minusDays(1);  // откатываем дату ещё на один день назад
        }

        // Не нашли подходящих данных, возвращаем значения по умолчанию
        result.add(defaultWorkTime);
        result.add(defaultStatus);
        //System.out.println("Не удалось найти учетные данные за этот год.");
        return result;
    }


    public int getWorkTimeToPOI(int day) {
        return workTimeMassive.get(Reports.month).get(day);
    }

    public String getWorkTimeToTableView(int day) {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(day)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(day));
            }
        }
    }

    public int getWorkerAllTimeToTableView(){
        int timeSumm = 0;
        for (int i = 1; i<32; i++){
            timeSumm = timeSumm+workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(i);
        }
        return timeSumm;
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
            case LAB1 -> "Лаборатория 1";
            case LAB2 -> "Лаборатория 2";
            case LAB5 -> "Лаборатория 5";
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
            case ("Лаборатория 1") -> this.district = District.LAB1;
            case ("Лаборатория 2") -> this.district = District.LAB2;
            case ("Лаборатория 5") -> this.district = District.LAB5;
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

    public String getFullNameWithInitials() {
        return lastName + " " + firstName.substring(0, 1).toUpperCase() + "." + patronymic.substring(0, 1).toUpperCase() + ".";
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
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.PERERABOTKA);
                setWorkTime(10);
            }
            case ("9") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.PERERABOTKA);
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
                setWorkTime(0);
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOLIDAY);
            }
            case ("РАБ в ОТП") -> workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                    .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOLYWORK);
            case ("ОТГ") -> //setWorkTime(0);
                    workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                            .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.OTRABOTKA);
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
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth() - 1);
    }

    public String getWorkerStatusMassive() {
        return switch (workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth())) {
            case WORK, OTRABOTKA, PERERABOTKA -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY, HOLYWORK -> "Отпуск";
            case NOTHING -> " ";
            case ADMINOTP -> "Админ. отпуск";
        };
    }

    public WorkerStatus gTwSd(int day) {
        return (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(day));
    }

    public WorkerStatus getWorkerStatus() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
    }

    public WorkerStatus getWorkerStatusTable(int day) {
        return workerStatusMassive.get(LocalDateTime.now().getMonthValue())
                .get(day);
    }

    public String getWorkerStatusAtDay(int day) {
        return switch (workerStatusMassive.get(Reports.month).get(day)) {
            case WORK, OTRABOTKA, PERERABOTKA -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY, HOLYWORK -> "Отпуск";
            case NOTHING -> " ";
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
            case "1" -> this.line = ConveyLine.LINE_1;
            case "2" -> this.line = ConveyLine.LINE_2;
            case "3" -> this.line = ConveyLine.LINE_3;
            case "4" -> this.line = ConveyLine.LINE_4;
            default -> this.line = ConveyLine.COMMON;
        }
    }

    public int compareTo(Worker o) {
        return this.getLastName().compareTo(o.getLastName());
    }

    public void eraseAllMassive() {
        workerStatusMassive.clear();
        workTimeMassive.clear();
    }
}



