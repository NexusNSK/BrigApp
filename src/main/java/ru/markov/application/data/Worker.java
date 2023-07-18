package ru.markov.application.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Calendar;
import java.util.List;
@ComponentScan

public class Worker {
    private String firstName;
    private String lastName;
    private String fatherName;
    private int category;
    private List<Calendar>holidays;
    private boolean isWork;
    private boolean isHospital;

    public Worker(String firstName, String lastName, String fatherName, int category) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.category = category;
    }

    @Override
    public String toString() {
        return firstName + lastName + fatherName + category;
    }
}
