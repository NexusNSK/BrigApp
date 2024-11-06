package ru.markov.application.data;

import ru.markov.application.views.BrigEdit;

public class FillMap {
    public static void fillArray(){
        BrigEdit.workerList.add(new Worker("m0", "m0", "m0", "", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m1", "m1", "m1", "1", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m2", "m2", "m2", "2", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m3", "m3", "m3", "3", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m4", "m4", "m4", "4", "Бригада монтажники", "Монтажник"));

        BrigEdit.workerList.add(new Worker("s0", "s0", "s0", "", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s1", "s1", "s1", "1", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s2", "s2", "s2", "2", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s3", "s3", "s3", "3", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s4", "s4", "s4", "4", "Бригада сборщики", "Сборщик"));

        BrigEdit.workerList.add(new Worker("t0", "t0", "t0", "", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t1", "t1", "t1", "1", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t2", "t2", "t1", "2", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t3", "t3", "t1", "3", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t4", "t4", "t1", "4", "Бригада техники", "Техник"));
    }
}
