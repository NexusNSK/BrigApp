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
    private final HashMap<Integer, HashMap<Integer, Integer>> workTimeMassive = new HashMap<>(12);
    //             <номер месяца : мапа <номер дня : часы>>
    private final WorkerStatus workerStatus = WorkerStatus.NOTHING;

    public void initWorkTimeMap() {
        if (workTimeMassive.isEmpty()) {
            // System.out.println("Создаю карту учета времени работника: " + getFullName());
            for (int i = 0; i <= 12; i++) {
                workTimeMassive.put(i, new HashMap<>(31));
                for (int j = 0; j <= 31; j++) {
                    workTimeMassive.get(i).put(j, 0);
                    //   System.out.println("Создаю " + (i) + " месяц, " + (j) + " число...");
                }
            }
            System.out.println("Создание карты учета времемни завершено!");
        }
    }

    public void setWorkTime(int hours) {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), hours);
    }

    /*public void setWorkTime(String hours) {
        workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .put(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth(), Integer.parseInt(hours));
    }*/

    public int getWorkTime() {
        return workTimeMassive.get(TimeAdapter.workTimeDatePicker.getValue().getMonthValue())
                .get(TimeAdapter.workTimeDatePicker.getValue().getDayOfMonth());

    }

    /*public void setCategory(Category category) {
        this.category = category;
    }*/

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

   /* public void setPost(Post post) {
        this.post = post;
    }*/

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

    public String getDistrict() {
        return switch (district) {
            case MOUNTING -> "Бригада монтажники";
            case BUILDING -> "Бригада сборщики";
            case TECH -> "Бригада техники";
        };
    }

    public void setDistrict(String district) {
        switch (district) {
            case ("Бригада монтажники") -> this.district = District.MOUNTING;
            case ("Бригада сборщики") -> this.district = District.BUILDING;
            case ("Бригада техники") -> this.district = District.TECH;
        }
    }

    /*public void setDistrict(District district) {
        this.district = district;
    }*/

    public Worker(String lastName, String firstName, String patronymic, District district, Post post, Category category) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.district = district;
        this.post = post;
        this.category = category;
        initWorkTimeMap();
    }

    public Worker(String lastName, String firstName, String patronymic, String district, String post, String category) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        setDistrict(district);
        setPost(post);
        setCategory(category);
        initWorkTimeMap();

    }

    public String getFullName() {
        return lastName + " " + firstName + " " + patronymic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /*public void setWorkerStatus(WorkerStatus status) {
        this.workerStatus = status;
    }*/

    /*public void setWorkerStatus(String status) {
        switch (status) {
            case ("Работает") -> {
                this.workerStatus = WorkerStatus.WORK;
                setWorkTime(8);
            }
            case ("Больничный") -> {
                this.workerStatus = WorkerStatus.HOSPITAL;
                setWorkTime(0);
            }
            case ("Отпуск") -> {
                this.workerStatus = WorkerStatus.HOLIDAY;
                setWorkTime(0);
            }
            case ("Не определено") -> {
                this.workerStatus = WorkerStatus.NOTHING;
                setWorkTime(0);
            }
        }

    }*/

    public String getWorkerStatus() {
        return switch (workerStatus) {
            case WORK -> "Работает";
            case HOSPITAL -> "Больничный";
            case HOLIDAY -> "Отпуск";
            case NOTHING -> "Не определено";
        };
    }
}


