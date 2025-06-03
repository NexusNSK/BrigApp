package ru.markov.application.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

public class Device implements Serializable, Comparable<Device>{
    @Serial
    private static final long serialVersionUID = 1L;

    private final String deviceName;

    public HashMap<String, HashMap<Integer,HashMap<Integer, Integer>>> deviceMap = new HashMap<>();
    //      HashMap<Брак,   HashMap<Месяц,  HashMap<День, количество>>>
    //      Обращаемся браку по ключу, указываем ключ месяца, ключ дня, получаем количество брака в конкретный день/партию
    public HashMap<Integer,HashMap<Integer, Integer>> totalPartMap = new HashMap<>();
    //     HashMap<Месяц,  HashMap<День, сколько в партии>>

    public Device(String deviceName) {
        this.deviceName = deviceName;
        initTotalPartMap();
    }

    @Override
    public int compareTo(Device o) {
        return this.deviceName.compareTo(o.deviceName);
    }

    @Override
    public String toString() {
        return deviceName;
    }

    public void initMapWithDefect(String defect) {
        deviceMap.put(defect, new HashMap<>()); // добавляем в мапу дефект и мапу с месяцами
        for (int month = 1; month <= 12; month++) {
            deviceMap.get(defect).put(month, new HashMap<>()); // добавляем 12 месяцев в мапу месяцев
            for (int day = 1; day <=31; day++) {
                deviceMap.get(defect).get(month).put(day, 0); // добавляем каждому месяцу 31 день с дефолтным значением брака 0
            }
        }
    }

    public void initTotalPartMap(){
        for (int month = 1; month <= 12; month++) {
            totalPartMap.put(month, new HashMap<>());
            for (int day = 1; day <= 31 ; day++) {
                totalPartMap.get(month).put(day, 0);
            }
        }
    }

    @Override
    public int hashCode() {
        return deviceName.hashCode();
    }
}
