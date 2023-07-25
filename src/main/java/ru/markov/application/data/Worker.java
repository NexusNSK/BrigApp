package ru.markov.application.data;

import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.context.annotation.ComponentScan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

@ComponentScan

public class Worker implements Serializable {
    private String firstName;
    private String lastName;
    private String fatherName;
    private int category;
    private Date birthday;
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

    public String getBirthday(){
        if (this.birthday==null){
            return "Не определено";
        }else{
            return this.birthday.toString();
        }
    }
    public void setBirthday(String date_){
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            this.birthday = format.parse(date_);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}


