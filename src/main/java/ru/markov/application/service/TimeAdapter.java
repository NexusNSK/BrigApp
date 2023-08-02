package ru.markov.application.service;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.LocalDate;

/**
 * Класс существует для передачи значения нестатического объекта класса WorkTime.java
 * статическому объекту данного класса для последующего этого статического объекта
 * в других классах.
 * Если отображать статический объект на web-странице, то возникает исключение.
 * Объект в этом случае не может быть перерисован и вызывает ошибку в работе, которая
 * не позволяет прогрмамме штатно работать в дальнейшем.
 * <p>
 * Необходимо вызывать метод .initWorkTime() в точке входа для первоначальной инициализации
 * значений используемого объекта, в противном случае будет выброшено NullPointerException
 */
public class TimeAdapter {
    public TimeAdapter() {
        initWorkTime();
    }

    public static DatePicker workTimeDatePicker = new DatePicker();

    public static void initWorkTime() {
        workTimeDatePicker.setValue(LocalDate.now());
    }
}
