package ru.markov.application.views;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Worker;

@PermitAll
@Route(value = "brigmanage", layout = MainLayout.class)
@PageTitle("Управление бригадой | BrigApp")
public class BrigManage extends Div {

    public BrigManage() {


        Grid<Worker> workerGrid = new Grid<>(Worker.class, false);
        workerGrid.addColumn(Worker::getFullName).setHeader("Сотрудники");
        workerGrid.addColumn(Worker::getCategory).setHeader("Категория");
        workerGrid.addColumn(Worker::getBirthday).setHeader("День рождения");
        workerGrid.addColumn(Worker::getHoliday).setHeader("Отпуск");
        workerGrid.addColumn(Worker::isHospital).setHeader("Больничный");
        workerGrid.setItems(BrigEditor.workerList);
        add(workerGrid);

    }
}