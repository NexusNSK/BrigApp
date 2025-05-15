package ru.markov.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.markov.application.service.*;
import ru.markov.application.views.Reports;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Worker implements Serializable, Comparable<Worker> {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("patronymic")
    private String patronymic;
    @JsonProperty("district")
    private District district;
    @JsonProperty("post")
    private Post post;
    @JsonProperty("line")
    private ConveyLine line;

    @JsonProperty("workerStatusMassive")
    private final HashMap<Integer, HashMap<Integer, WorkerStatus>> workerStatusMassive = new HashMap<>(12);
    @JsonProperty("workTimeMassive")
    private final HashMap<Integer, HashMap<Integer, Integer>> workTimeMassive = new HashMap<>(12);

    //            hashmap <номер месяца : hashmap  <номер дня : часы>>
    @JsonProperty
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

    @JsonIgnore
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

    @JsonIgnore
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

    public boolean checkTimeAndStatus(){
        boolean acceptChanges = true;
        if (!workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()).equals(WorkerStatus.NOTHING)) {
            acceptChanges = false;
        }
        return acceptChanges;
    }

    @JsonIgnore
    public void setWorkTimeLikeYesterday() {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkTimeLikeYesterday());
    }

    @JsonIgnore
    public void setWorkTimeLikeFriday() {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkTimeLikeFriday());
    }

    @JsonIgnore
    public int getWorkTime() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
    }

    @JsonIgnore
    public int getWorkTimeLikeYesterday() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth() - 1);
    }

    @JsonIgnore
    public int getWorkTimeLikeFriday() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth() - 3);
    }

    @JsonIgnore
    public int getWorkTimeToPOI(int day) {
        return workTimeMassive.get(Reports.month).get(day);
    }

    public String getWorkTimeToTableView1() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(1)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(1));
            }
        }
    }

    public String getWorkTimeToTableView2() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(2)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(2));
            }
        }
    }

    public String getWorkTimeToTableView3() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(3)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(3));
            }
        }
    }

    public String getWorkTimeToTableView4() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(4)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(4));
            }
        }
    }

    public String getWorkTimeToTableView5() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(5)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(5));
            }
        }
    }

    public String getWorkTimeToTableView6() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(6)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(6));
            }
        }
    }

    public String getWorkTimeToTableView7() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(7)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(7));
            }
        }
    }

    public String getWorkTimeToTableView8() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(8)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(8));
            }
        }
    }

    public String getWorkTimeToTableView9() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(9)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(9));
            }
        }
    }

    public String getWorkTimeToTableView10() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(10)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(10));
            }
        }
    }

    public String getWorkTimeToTableView11() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(11)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(11));
            }
        }
    }

    public String getWorkTimeToTableView12() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(12)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(12));
            }
        }
    }

    public String getWorkTimeToTableView13() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(13)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(13));
            }
        }
    }

    public String getWorkTimeToTableView14() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(14)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(14));
            }
        }
    }

    public String getWorkTimeToTableView15() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(15)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(15));
            }
        }
    }

    public String getWorkTimeToTableView16() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(16)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(16));
            }
        }
    }

    public String getWorkTimeToTableView17() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(17)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(17));
            }
        }
    }

    public String getWorkTimeToTableView18() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(18)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(18));
            }
        }
    }

    public String getWorkTimeToTableView19() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(19)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(19));
            }
        }
    }

    public String getWorkTimeToTableView20() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(20)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(20));
            }
        }
    }

    public String getWorkTimeToTableView21() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(21)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(21));
            }
        }
    }

    public String getWorkTimeToTableView22() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(22)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(22));
            }
        }
    }

    public String getWorkTimeToTableView23() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(23)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(23));
            }
        }
    }

    public String getWorkTimeToTableView24() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(24)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(24));
            }
        }
    }

    public String getWorkTimeToTableView25() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(25)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(25));
            }
        }
    }

    public String getWorkTimeToTableView26() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(26)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(26));
            }
        }
    }

    public String getWorkTimeToTableView27() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(27)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(27));
            }
        }
    }

    public String getWorkTimeToTableView28() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(28)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(28));
            }
        }
    }

    public String getWorkTimeToTableView29() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(29)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(29));
            }
        }
    }

    public String getWorkTimeToTableView30() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(30)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(30));
            }
        }
    }

    public String getWorkTimeToTableView31() {
        switch (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(31)) {
            case NOTHING, ADMINOTP, HOLIDAY, HOSPITAL -> {
                return "";
            }
            default -> {
                return String.valueOf(workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(31));
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


    public void setWorkerStatusMassiveLikeFriday() {
        workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkerStatusLikeFriday());
    }


    private WorkerStatus getWorkerStatusLikeYesterday() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth() - 1);
    }


    private WorkerStatus getWorkerStatusLikeFriday() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth() - 3);
    }


    public String getWorkerStatusMassive() {
        return switch (workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth())) {
            case WORK, OTRABOTKA, PERERABOTKA -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "---";
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



