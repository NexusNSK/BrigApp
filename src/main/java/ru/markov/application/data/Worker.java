package ru.markov.application.data;

import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.context.annotation.ComponentScan;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@ComponentScan

public class Worker implements Serializable {
    private String firstName;
    private String lastName;
    private String fatherName;
    private String category;
    private Calendar birthday = new GregorianCalendar();
    private List<Calendar> holiday = new ArrayList<>();
    private boolean isWork;
    private boolean isHospital;
    private boolean isHoliday;

    public Worker(String firstName, String lastName, String fatherName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        Calendar beginHoliday = new GregorianCalendar(2020, 00, 01);
        Calendar endHoliday = new GregorianCalendar(beginHoliday.get(Calendar.YEAR), beginHoliday.get(Calendar.MONTH), beginHoliday.get(Calendar.DAY_OF_MONTH)+14);
        holiday.add(beginHoliday);
        holiday.add(endHoliday);


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
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBirthday(){
        if (this.birthday==null){
            return "Не определено";
        }else{
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String s = df.format(birthday.getTime());
            return s;
        }
    }
    public void setBirthday(String date_){
        String[] stringToDate = date_.split("-");
        String year = stringToDate[2];
        String mount = stringToDate[1];
        String day = stringToDate[0];
        birthday.set(Integer.parseInt(year), Integer.parseInt(mount)-1, Integer.parseInt(day));
    }

    public String getHolidays(){ //3 отпуска, или 2, или 1, или хз.. думать
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String begin = df.format(holiday.get(0).getTime());
        String end = df.format(holiday.get(1).getTime());
        return "c "+ begin + "\nпо " + end;
    }

    public void setHolidays(String begin){

    }

}


