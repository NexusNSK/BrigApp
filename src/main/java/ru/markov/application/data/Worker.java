package ru.markov.application.data;

import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.context.annotation.ComponentScan;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
@ComponentScan

public class Worker implements Serializable {
    private String firstName;
    private String lastName;
    private String fatherName;
    private int category;
    private Calendar birthday;
    private List<Calendar> holiday;
    private boolean isWork;
    private boolean isHospital;
    private boolean isHoliday;

    public Worker(String firstName, String lastName, String fatherName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
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

    public String getCategory() {
        return String.valueOf(category);
    }

    public void setCategory(String category) {
        this.category = Integer.parseInt(category);
    }


}


