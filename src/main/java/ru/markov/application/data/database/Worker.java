package ru.markov.application.data.database;

import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;

public class Worker {

    public Worker(String firstName, String lastName, String fatherName, String category) {
    }

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String fatherName;
    private String category;


}

