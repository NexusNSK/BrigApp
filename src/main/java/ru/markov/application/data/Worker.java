package ru.markov.application.data;

import org.springframework.context.annotation.ComponentScan;
import ru.markov.application.service.*;

import java.io.Serializable;
import java.util.*;

@ComponentScan

public class Worker implements Serializable {
    private String firstName;
    private String lastName;
    private String patronymic; //отчество
    private District district;
    private Post post;
    private Category category;
    private final HashMap<Integer, HashMap<Integer, WorkerStatus>> workerStatusMassive = new HashMap<>(12);
    private final HashMap<Integer, HashMap<Integer, Integer>> workTimeMassive = new HashMap<>(12);
    //             <номер месяца : мапа <номер дня : часы>>

    public void initWorkerStatusMap(){
        if (workerStatusMassive.isEmpty()){
            for (int i = 0; i <= 12; i++){
                workerStatusMassive.put(i, new HashMap<>(31));
                for (int j = 0; j <= 32; j++){
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
    public int getWorkTime() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());
    }
    public int getWorkTimeToPOI(int day){
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(day);
    }
    public void setCategory(String category) {
        switch (category) {
            case ("1") -> this.category = Category.ONE;
            case ("2") -> this.category = Category.TWO;
            case ("3") -> this.category = Category.THREE;
            case ("Бригадир") -> this.category = Category.BRIG;
            case ("Испытательный срок") -> this.category = Category.IC;
        }
    }
    public String getCategory() {
        String cat = "";
        switch (category) {
            case ONE -> cat = "1";
            case TWO -> cat = "2";
            case THREE -> cat = "3";
            case BRIG -> cat = "Бригадир";
            case IC -> cat = "Испытательный срок";
        }
        return cat;
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
    public District getDistrict(){
        return this.district;
    }
    public void setDistrict(String district) {
        switch (district) {
            case ("Бригада монтажники") -> this.district = District.MOUNTING;
            case ("Бригада сборщики") -> this.district = District.BUILDING;
            case ("Бригада техники") -> this.district = District.TECH;
        }
    }
    public Worker(String lastName, String firstName, String patronymic, String district, String post, String category) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        setDistrict(district);
        setPost(post);
        setCategory(category);
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
        switch (status){
            case ("Работает (полный день)") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
                setWorkTime(8);
            }
            case ("Работает (нестандартное время)") -> workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                    .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.WORK);
            case ("Больничный") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOSPITAL);
                setWorkTime(0);
            }
            case ("Отпуск") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.HOLIDAY);
                setWorkTime(0);
            }
            case ("Не определено") -> {
                workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                        .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), WorkerStatus.NOTHING);
                setWorkTime(0);
            }

    }
    }
    public String getWorkerStatusMassive(){
        return switch (workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth())) {
            case WORK -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "Не определено";
        };
    }
    public String getWorkerStatusAtDay(int day){
        return switch (workerStatusMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue()).get(day)){
            case WORK -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "Не определено";
        };
    }
}

