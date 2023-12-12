package ru.markov.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.Worker;
import ru.markov.application.service.ConveyLine;
import ru.markov.application.service.Serial;
import java.util.ArrayList;


@Route(value ="who_next", layout = MainLayout.class)
@RolesAllowed("TECH")
public class WhoNext extends VerticalLayout {
    private Worker draggedWorker;
    public static ArrayList<Worker> people = new ArrayList<>();
    static ArrayList<Worker> people1 = new ArrayList<>();
    static ArrayList<Worker> people2 = new ArrayList<>();
    static ArrayList<Worker> people3 = new ArrayList<>();
    static ArrayList<Worker> people4 = new ArrayList<>();
    public WhoNext() {

        Button save = new Button("Глобальное сохранение", new Icon(VaadinIcon.SAFE));
        save.addClickListener(event -> Serial.save());

        Button saveList = new Button("Сохранить список миграции", new Icon(VaadinIcon.FILE));
        //saveList.addClickListener(event -> Serial.saveMigration());
        Button loadList = new Button("Загрузить список миграции", new Icon(VaadinIcon.AIRPLANE));
        /*loadList.addClickListener(event -> {
            try {
                Serial.loadMigration();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });*/

        Div buttons = new Div(save, saveList, loadList);
        Grid<Worker> grid1Line = new Grid<>(Worker.class, false);
        grid1Line.addColumn(Worker::getFullName).setHeader("Линия 1");
        setGridStyles(grid1Line);
        Grid<Worker> grid2Line = new Grid<>(Worker.class, false);
        grid2Line.addColumn(Worker::getFullName).setHeader("Линия 2");
        setGridStyles(grid2Line);
        Grid<Worker> grid3Line = new Grid<>(Worker.class, false);
        grid3Line.addColumn(Worker::getFullName).setHeader("Линия 3");
        setGridStyles(grid3Line);
        Grid<Worker> grid4Line = new Grid<>(Worker.class, false);
        grid4Line.addColumn(Worker::getFullName).setHeader("Линия 4");
        setGridStyles(grid4Line);

        GridListDataView<Worker> dataView1 = grid1Line.setItems(people1);
        GridListDataView<Worker> dataView2 = grid2Line.setItems(people2);
        GridListDataView<Worker> dataView3 = grid3Line.setItems(people3);
        GridListDataView<Worker> dataView4 = grid4Line.setItems(people4);

        grid1Line.setDropMode(GridDropMode.ON_GRID);
        grid1Line.setRowsDraggable(true);
        grid1Line.addDragStartListener(this::handleDragStart);
        grid1Line.addDropListener(e -> {
            dataView3.removeItem(draggedWorker);
            dataView4.removeItem(draggedWorker);
            dataView2.removeItem(draggedWorker);
            grid1Line.getDataProvider().refreshAll();
            grid2Line.getDataProvider().refreshAll();
            grid3Line.getDataProvider().refreshAll();
            grid4Line.getDataProvider().refreshAll();
            addAndRemoveWorker(draggedWorker, people3, people4, people2, people1);
        });
        grid1Line.addDragEndListener(this::handleDragEnd);

        grid2Line.setDropMode(GridDropMode.ON_GRID);
        grid2Line.setRowsDraggable(true);
        grid2Line.addDragStartListener(this::handleDragStart);
        grid2Line.addDropListener(e -> {
            dataView3.removeItem(draggedWorker);
            dataView1.removeItem(draggedWorker);
            dataView4.removeItem(draggedWorker);
            grid1Line.getDataProvider().refreshAll();
            grid2Line.getDataProvider().refreshAll();
            grid3Line.getDataProvider().refreshAll();
            grid4Line.getDataProvider().refreshAll();
            addAndRemoveWorker(draggedWorker, people3, people4, people1, people2);
        });
        grid2Line.addDragEndListener(this::handleDragEnd);

        grid3Line.setDropMode(GridDropMode.ON_GRID);
        grid3Line.setRowsDraggable(true);
        grid3Line.addDragStartListener(this::handleDragStart);
        grid3Line.addDropListener(e -> {
            dataView4.removeItem(draggedWorker);
            dataView2.removeItem(draggedWorker);
            dataView1.removeItem(draggedWorker);
            grid1Line.getDataProvider().refreshAll();
            grid2Line.getDataProvider().refreshAll();
            grid3Line.getDataProvider().refreshAll();
            grid4Line.getDataProvider().refreshAll();
            addAndRemoveWorker(draggedWorker, people4, people2, people1, people3);
        });
        grid3Line.addDragEndListener(this::handleDragEnd);

        grid4Line.setDropMode(GridDropMode.ON_GRID);
        grid4Line.setRowsDraggable(true);
        grid4Line.addDragStartListener(this::handleDragStart);
        grid4Line.addDropListener(e -> {
            dataView1.removeItem(draggedWorker);
            dataView2.removeItem(draggedWorker);
            dataView3.removeItem(draggedWorker);
            grid1Line.getDataProvider().refreshAll();
            grid2Line.getDataProvider().refreshAll();
            grid3Line.getDataProvider().refreshAll();
            grid4Line.getDataProvider().refreshAll();
            addAndRemoveWorker(draggedWorker, people1, people2, people3, people4);
        });
        grid4Line.addDragEndListener(this::handleDragEnd);

        Div container = new Div(grid1Line, grid2Line, grid3Line, grid4Line);
        setContainerStyles(container);

        add(buttons, container);
    }

    private void handleDragStart(GridDragStartEvent<Worker> e) {
        draggedWorker = e.getDraggedItems().get(0);
    }

    private void handleDragEnd(GridDragEndEvent<Worker> e) {
        draggedWorker = null;
    }

    private static void setGridStyles(Grid<Worker> grid) {
        grid.getStyle().set("width", "300px").set("height", "800px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }

    private static void addAndRemoveWorker(Worker draggedWorker,
                                           ArrayList<Worker> targetList1,
                                           ArrayList<Worker> targetList2,
                                           ArrayList<Worker> targetList3,
                                           ArrayList<Worker> destinyList){
        destinyList.add(draggedWorker);
        if (destinyList.equals(people1)) {
            draggedWorker.setLine("1");
            targetList1.removeIf(w -> w.equals(draggedWorker));
            targetList2.removeIf(w -> w.equals(draggedWorker));
            targetList3.removeIf(w -> w.equals(draggedWorker));
        }
        if (destinyList.equals(people2)) {
            draggedWorker.setLine("2");
            targetList1.removeIf(w -> w.equals(draggedWorker));
            targetList2.removeIf(w -> w.equals(draggedWorker));
            targetList3.removeIf(w -> w.equals(draggedWorker));
        }
        if (destinyList.equals(people3)) {
            draggedWorker.setLine("3");
            targetList1.removeIf(w -> w.equals(draggedWorker));
            targetList2.removeIf(w -> w.equals(draggedWorker));
            targetList3.removeIf(w -> w.equals(draggedWorker));
        }
        if (destinyList.equals(people4)) {
            draggedWorker.setLine("4");
            targetList1.removeIf(w -> w.equals(draggedWorker));
            targetList2.removeIf(w -> w.equals(draggedWorker));
            targetList3.removeIf(w -> w.equals(draggedWorker));
        }
    }
    public static void initListForWhoNext(){
        people1.clear();
        people2.clear();
        people3.clear();
        people4.clear();
        for (Worker w : people){
            if (w.getLine().equals(ConveyLine.LINE_1)&&!(w.getPost().equals("Бригадир техников"))) people1.add(w);
            if (w.getLine().equals(ConveyLine.LINE_2)&&!(w.getPost().equals("Бригадир техников"))) people2.add(w);
            if (w.getLine().equals(ConveyLine.LINE_3)&&!(w.getPost().equals("Бригадир техников"))) people3.add(w);
            if (w.getLine().equals(ConveyLine.LINE_4)&&!(w.getPost().equals("Бригадир техников"))) people4.add(w);
        }
    }

}

