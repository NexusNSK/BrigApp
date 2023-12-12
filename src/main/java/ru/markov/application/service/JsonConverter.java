package ru.markov.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.markov.application.data.Worker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {

    public static void toJSON(List<Worker> list) throws IOException {
        File jsonFile = new File("workers.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(jsonFile, list);
        System.out.println("В корне создан json всех работников (workers.json)");
    }
}