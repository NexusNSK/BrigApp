package ru.markov.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.markov.application.service.*;
import ru.markov.application.views.Reports;
import ru.markov.application.views.TableView;

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
       } else if (hours > 0 && hours < 8) {workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.OTRABOTKA);}
        else if (hours == 8) {
            workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                    .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
        }
    }
    /*
    @JsonIgnore
    public void setWorkTimeForWorkInHoliday(int hours){
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), hours);
    }
    */
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
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()-1);
    }
    @JsonIgnore
    public int getWorkTimeLikeFriday() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()-3);
    }
    @JsonIgnore
    public int getWorkTimeToPOI(int day) {
        return workTimeMassive.get(Reports.month).get(day);
    }
    public int getWorkTimeToTableView1(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(1);
    }public int getWorkTimeToTableView2(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(2);
    }
    public int getWorkTimeToTableView3(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(3);
    }
    public int getWorkTimeToTableView4(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(4);
    }
    public int getWorkTimeToTableView5(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(5);
    }
    public int getWorkTimeToTableView6(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(6);
    }
    public int getWorkTimeToTableView7(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(7);
    }
    public int getWorkTimeToTableView8(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(8);
    }
    public int getWorkTimeToTableView9(){
        return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(9);
    }
    public int getWorkTimeToTableView10(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(10);}
    public int getWorkTimeToTableView11(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(11);}
    public int getWorkTimeToTableView12(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(12);}
    public int getWorkTimeToTableView13(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(13);}
    public int getWorkTimeToTableView14(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(14);}
    public int getWorkTimeToTableView15(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(15);}
    public int getWorkTimeToTableView16(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(16);}
    public int getWorkTimeToTableView17(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(17);}
    public int getWorkTimeToTableView18(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(18);}
    public int getWorkTimeToTableView19(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(19);}
    public int getWorkTimeToTableView20(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(20);}
    public int getWorkTimeToTableView21(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(21);}
    public int getWorkTimeToTableView22(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(22);}
    public int getWorkTimeToTableView23(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(23);}
    public int getWorkTimeToTableView24(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(24);}
    public int getWorkTimeToTableView25(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(25);}
    public int getWorkTimeToTableView26(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(26);}
    public int getWorkTimeToTableView27(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(27);}
    public int getWorkTimeToTableView28(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(28);}
    public int getWorkTimeToTableView29(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(29);}
    public int getWorkTimeToTableView30(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(30);}
    public int getWorkTimeToTableView31(){return workTimeMassive.get(LocalDateTime.now().getMonthValue()).get(31);}

    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
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
    @JsonIgnore
    public District getDistrict() {
        return this.district;
    }
    @JsonIgnore
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
    //public Worker(){}

    @JsonIgnore
    public String getFullName() {
        return lastName + " " + firstName + " " + patronymic;
    }
    public String getFullNameWithInitials() {
        return lastName + " " + firstName.substring(0, 1).toUpperCase() + "." + patronymic.substring(0, 1).toUpperCase() + ".";
    }

    @JsonIgnore
    public String getFirstName() {
        return this.firstName;
    }
    @JsonIgnore
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    @JsonIgnore
    public String getLastName() {
        return this.lastName;
    }
    @JsonIgnore
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @JsonIgnore
    public String getPatronymic() {
        return this.patronymic;
    }
    @JsonIgnore
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    @JsonIgnore
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
            case ("ОТП") -> //setWorkTime(0);
                    workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                            .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOLIDAY);

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
    @JsonIgnore
    public void setWorkerStatusMassiveLikeYesterday() {
        workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkerStatusLikeYesterday());
    }
    @JsonIgnore
    public void setWorkerStatusMassiveLikeFriday() {
        workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), getWorkerStatusLikeFriday());
    }
    @JsonIgnore
    private WorkerStatus getWorkerStatusLikeYesterday() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()-1);
    }
    @JsonIgnore
    private WorkerStatus getWorkerStatusLikeFriday() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth()-3);
    }

    @JsonIgnore
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

    public WorkerStatus gTwSd(int day){return (workerStatusMassive.get(LocalDateTime.now().getMonthValue()).get(day));}

    @JsonIgnore
    public WorkerStatus getWorkerStatus() {
        return workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
    }
    public WorkerStatus getWorkerStatusTable(int day) {
        return workerStatusMassive.get(LocalDateTime.now().getMonthValue())
                .get(day);
    }
    @JsonIgnore
    public String getWorkerStatusAtDay(int day) {
        return switch (workerStatusMassive.get(Reports.month).get(day)) {
            case WORK, OTRABOTKA, PERERABOTKA -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "---";
            case ADMINOTP -> "Админ. отпуск";
        };
    }
    @JsonIgnore
    public WorkerStatus getWorkerStatusAtDayToRepo(int day) {
        return workerStatusMassive.get(Reports.month).get(day);
    }

    @JsonIgnore
    public String getLineToString() {
        return switch (line) {
            case LINE_1 -> "1";
            case LINE_2 -> "2";
            case LINE_3 -> "3";
            case LINE_4 -> "4";
            default -> "---";
        };
    }
    @JsonIgnore
    public ConveyLine getLine() {
        return this.line;
    }
    @JsonIgnore
    public void setLine(String line) {
        switch (line) {
            case "1" -> this.line = ConveyLine.LINE_1;
            case "2" -> this.line = ConveyLine.LINE_2;
            case "3" -> this.line = ConveyLine.LINE_3;
            case "4" -> this.line = ConveyLine.LINE_4;
            default -> this.line = ConveyLine.COMMON;
        }
    }

    @Override
    public int compareTo(Worker o) {
        return this.getLastName().compareTo(o.getLastName());
    }
    public void eraseAllMassive(){
        workerStatusMassive.clear();
        workTimeMassive.clear();
    }
}



