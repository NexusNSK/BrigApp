package ru.markov.application.data;

import org.springframework.context.annotation.ComponentScan;
import ru.markov.application.views.WorkTime;

import java.io.Serializable;
import java.util.HashMap;

@ComponentScan

public class Worker implements Serializable {
    private String firstName;
    private String lastName;
    private String fatherName;
    private District district;
    private Post post;
    private Category category;
    private HashMap<Integer, Integer> workTime = new HashMap<>(31);

    public void initWorkTimeMap() {
        if (workTime.isEmpty()) {
            System.out.println("Создаю карту учета времени работника: " + getFullName());
            for (int i = 1; i <= 31; i++) {
                workTime.put(i, 0);
            }
        } else {
            System.out.println("В карте учета времени работника есть данные...");
        }
    }

    public void setWorkTime(int hours) {
        workTime.put(WorkTime.workTimeDatePicker.getValue().getDayOfMonth(), hours);
    }

    public int getWorkTime() {
        int day = WorkTime.workTimeDatePicker.getValue().getDayOfMonth();
        return workTime.get(day);
    }


    public void setCategory(Category category) {
        this.category = category;
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

    public void setPost(Post post) {
        this.post = post;
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

    public String getDistrict() {
        String ds = "";
        switch (district) {
            case MOUNTING -> ds = "Бригада монтажники";
            case BUILDING -> ds = "Бригада сборщики";
            case TECH -> ds = "Бригада техники";
        }
        return ds;
    }

    public void setDistrict(String district) {
        switch (district) {
            case ("Бригада монтажники") -> this.district = District.MOUNTING;
            case ("Бригада сборщики") -> this.district = District.BUILDING;
            case ("Бригада техники") -> this.district = District.TECH;
        }
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Worker(String lastName, String firstName, String fatherName, District district, Post post, Category category) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.fatherName = fatherName;
        this.district = district;
        this.post = post;
        this.category = category;
        initWorkTimeMap();
    }

    public Worker(String lastName, String firstName, String fatherName, String district, String post, String category) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.fatherName = fatherName;
        setDistrict(district);
        setPost(post);
        setCategory(category);
        initWorkTimeMap();
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + fatherName;
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

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

}


