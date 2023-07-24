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

    public String isWork() {
        if (this.isWork==true){
            return "Работает";
        }else{
            return "Не работает";
        }
    }

    public void setWork(String s) {
        if (s == "1"){
        isWork = true;
        }else{
            isWork=false;
        }
    }

    public String isHospital() {
        if(this.isHospital==true){
            return "На больничном";
        }else{
            return "Не болеет";
        }
    }

    public void setHospital(boolean hospital) {
        isHospital = hospital;
    }

    public String isHoliday() {
        if(this.isHoliday==true){
            return "В отпуске";
        }else{
            return "Не в отпуске";
        }
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

}


