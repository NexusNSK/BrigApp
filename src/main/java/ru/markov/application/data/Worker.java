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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public List<Calendar> getHoliday() {
        return holiday;
    }

    public void setHoliday(List<Calendar> holiday) {
        this.holiday = holiday;
    }

    public boolean isWork() {
        return isWork;
    }

    public void setWork(boolean work) {
        isWork = work;
    }

    public boolean isHospital() {
        return isHospital;
    }

    public void setHospital(boolean hospital) {
        isHospital = hospital;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

}


