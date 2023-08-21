package ru.markov.application.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.markov.application.data.Worker;
import ru.markov.application.service.ConveyLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(value = "line_editor", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class LineEditor extends Div {
    public static HashMap<ConveyLine, List<Worker>> mountSplitLine = new HashMap<>();
    public static HashMap<ConveyLine, List<Worker>> builderSplitLine = new HashMap<>();
private Worker draggedItem;
    public LineEditor() {
        initHashMap(mountSplitLine);
        initHashMap(builderSplitLine);
        Grid<Worker> commonGrid = setupGrid();
        Grid<Worker> line1 = setupGrid();

        GridListDataView<Worker> dataView1 = commonGrid.setItems(mountSplitLine.get(ConveyLine.COMMON));
        GridListDataView<Worker> dataView2 = line1.setItems(mountSplitLine.get(ConveyLine.LINE_1));

        commonGrid.setDropMode(GridDropMode.ON_GRID);
        commonGrid.setRowsDraggable(true);
        commonGrid.addDragStartListener(this::handleDragStart);
        commonGrid.addDropListener(e -> {
            dataView2.removeItem(draggedItem);
            dataView1.addItem(draggedItem);
        });
        commonGrid.addDragEndListener(this::handleDragEnd);

        line1.setDropMode(GridDropMode.ON_GRID);
        line1.setRowsDraggable(true);
        line1.addDragStartListener(this::handleDragStart);
        line1.addDropListener(e -> {
            dataView1.removeItem(draggedItem);
            dataView2.addItem(draggedItem);
        });
        line1.addDragEndListener(this::handleDragEnd);

        Div container = new Div(commonGrid, line1);
        setContainerStyles(container);

        commonGrid.addColumn(Worker::getFullName);
        commonGrid.addColumn(Worker::getPost);
        add(commonGrid, container);
    }

    private static Grid<Worker> setupGrid() {
        Grid<Worker> grid = new Grid<>(Worker.class, false);
        grid.addColumn(Worker::getFullName).setHeader("ФИО");
        grid.addColumn(Worker::getPost).setHeader("Должность");
        setGridStyles(grid);

        return grid;
    }

    private static void setGridStyles(Grid<Worker> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private void handleDragStart(GridDragStartEvent<Worker> e) {
        draggedItem = e.getDraggedItems().get(0);
    }
    private void handleDragEnd(GridDragEndEvent<Worker> e) {
        draggedItem = null;
    }
    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }
    private void initHashMap(HashMap<ConveyLine,List<Worker>> hm){
        hm.put(ConveyLine.COMMON, new ArrayList<>());
        hm.put(ConveyLine.LINE_1, new ArrayList<>());
        hm.put(ConveyLine.LINE_2, new ArrayList<>());
        hm.put(ConveyLine.LINE_3, new ArrayList<>());
        hm.put(ConveyLine.LINE_4, new ArrayList<>());
        }

}

