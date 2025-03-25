package ru.markov.application.data;

import ru.markov.application.views.BrigEdit;

public class FillMap {
    public static void fillArray(){
        BrigEdit.workerList.add(new Worker("m0test", "m0test", "m0test", "", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m1test", "m1test", "m1test", "1", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m2test", "m2test", "m2test", "2", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m3test", "m3test", "m3test", "3", "Бригада монтажники", "Монтажник"));
        BrigEdit.workerList.add(new Worker("m4test", "m4test", "m4test", "4", "Бригада монтажники", "Монтажник"));

        BrigEdit.workerList.add(new Worker("s0test", "s0test", "s0test", "", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s1test", "s1test", "s1test", "1", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s2test", "s2test", "s2test", "2", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s3test", "s3test", "s3test", "3", "Бригада сборщики", "Сборщик"));
        BrigEdit.workerList.add(new Worker("s4test", "s4test", "s4test", "4", "Бригада сборщики", "Сборщик"));

        BrigEdit.workerList.add(new Worker("t0test", "t0test", "t0test", "", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t1test", "t1test", "t1test", "1", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t2test", "t2test", "t1test", "2", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t3test", "t3test", "t1test", "3", "Бригада техники", "Техник"));
        BrigEdit.workerList.add(new Worker("t4test", "t4test", "t1test", "4", "Бригада техники", "Техник"));
    }
}
