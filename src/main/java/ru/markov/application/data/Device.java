package ru.markov.application.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Device implements Serializable, Comparable<Device>{
    @Serial
    private static final long serialVersionUID = 1L;

    public String getDeviceName() {
        return deviceName;
    }

    private final String deviceName;

    public HashMap<String, HashMap<Integer,HashMap<Integer, Integer>>> deviceMap = new HashMap<>();
    //      HashMap<Брак,   HashMap<Месяц,  HashMap<День, количество>>>
    //      Обращаемся браку по ключу, указываем ключ месяца, ключ дня, получаем количество брака в конкретный день/партию
    public HashMap<Integer,HashMap<Integer, Integer>> totalPartMap = new HashMap<>();
    //     HashMap<Месяц,  HashMap<День, сколько в партии>>
    public HashMap<Integer, HashMap<Integer, String>> lineMap = new HashMap<>();
    //     HashMap<Месяц.  HashMap<День, Линия>>
    public HashMap<Integer, HashMap<Integer, String>> startPartDate = new HashMap<>();
    //     HashMap<Месяц.  HashMap<День, Дата начало партии>>
    public HashMap<Integer, HashMap<Integer, String>> finishPartDate = new HashMap<>();
    //     HashMap<Месяц.  HashMap<День, Дата окончания партии>>

    public Device(String deviceName, ArrayList<String> parts) {
        this.deviceName = deviceName;
        initMapWithPreset(parts);
        initOtherMap();
    }
    public Device(String deviceName) {
        this.deviceName = deviceName;
        initOtherMap();
    }

    @Override
    public int compareTo(Device o) {
        return this.deviceName.compareTo(o.deviceName);
    }

    @Override
    public String toString() {
        return deviceName;
    }
    public void deleteDefect(String defectToDelete){
        deviceMap.remove(defectToDelete);
    }

    public void initMapWithDefect(String defect) {
        operateToInitMap(defect);
    }

    private void operateToInitMap(String defect) {
        deviceMap.put(defect, new HashMap<>()); // добавляем в мапу дефект и мапу с месяцами
        for (int month = 1; month <= 12; month++) {
            deviceMap.get(defect).put(month, new HashMap<>()); // добавляем 12 месяцев в мапу месяцев
            for (int day = 1; day <=31; day++) {
                deviceMap.get(defect).get(month).put(day, 0); // добавляем каждому месяцу 31 день с дефолтным значением брака 0
            }
        }
    }

    public void initMapWithPreset(ArrayList<String> preset) {
        preset.forEach(this::operateToInitMap);
    }


    public void initOtherMap(){
        for (int month = 1; month <= 12; month++) {
            totalPartMap.put(month, new HashMap<>());
            lineMap.put(month, new HashMap<>());
            startPartDate.put(month, new HashMap<>());
            finishPartDate.put(month, new HashMap<>());
            for (int day = 1; day <= 31 ; day++) {
                totalPartMap.get(month).put(day, 0);
                lineMap.get(month).put(day, "");
                startPartDate.get(month).put(day,"");
                finishPartDate.get(month).put(day, "");
            }
        }
        System.out.println("Total Part Map для " + deviceName + " инициализирована.");
    }
    @Override
    public int hashCode() {
        return deviceName.hashCode();
    }

    public void printNestedMap(Map<?, ?> map, int indent) {
        String indentStr = " ".repeat(indent);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.print(indentStr + entry.getKey() + " : ");
            if (entry.getValue() instanceof Map) {
                System.out.println();
                // Рекурсивно вызываем для вложенной Map с увеличенным отступом
                printNestedMap((Map<?, ?>) entry.getValue(), indent + 4);
            } else {
                // Выводим значение, если это не Map
                System.out.println(entry.getValue());
            }
        }
    }

}
