package ru.markov.application.data;

import org.springframework.context.annotation.Bean;
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
    private List<Calendar> holidays;
    private boolean isWork;
    private boolean isHospital;

    public Worker(String firstName, String lastName, String fatherName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
    }

    public String getFullName() {
        return firstName + " " + lastName + " " + fatherName;
    }


    @Override
    public String toString() {
        return lastName + firstName + fatherName;
    }
}

