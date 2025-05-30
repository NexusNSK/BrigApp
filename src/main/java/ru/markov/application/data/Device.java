package ru.markov.application.data;

import java.io.Serializable;
import java.util.HashMap;

public class Device implements Serializable, Comparable<Device>{
    private String deviceName;

    private HashMap<String, HashMap<Integer,HashMap<Integer, Integer>>> deviceMap = new HashMap<>();
    //      HashMap<Брак,   HashMap<Месяц,  HashMap<День, количество>>>
    //      Обращаемся браку по ключу, указываем ключ месяца, ключ дня, получаем количество брака в конкретный день/партию

    public Device(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }


    @Override
    public int compareTo(Device o) {
        return this.deviceName.compareTo(o.deviceName);
    }

    @Override
    public String toString() {
        return deviceName;
    }
}
