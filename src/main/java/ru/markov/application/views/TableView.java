package ru.markov.application.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Worker;
import ru.markov.application.security.SecurityService;
import ru.markov.application.service.ConveyLine;

@Route(value = "tableview", layout = MainLayout.class)
@PageTitle("BrigApp א Просмотр табеля")
@PermitAll
public class TableView extends VerticalLayout {
    public static int day = 1;
    public TableView(SecurityService securityService) {
        String username = securityService.getAuthenticatedUser().getUsername();
        //HorizontalLayout horizontalLayout = new HorizontalLayout();
        //ComboBox<String> mounts = new ComboBox<>();

        //mounts.setItems("Январь", "Февраль", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь");
        //Button accept = new Button("Применить");
            //horizontalLayout.add(mounts);
            Grid<Worker> grid = new Grid<>(Worker.class,false);
            grid.addClassName("table-view-grid");
            //grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
            setItemForGrid(username, grid);
            grid.setWidthFull();
            grid.setMinHeight("500px");
            grid.setHeight("800px");

        grid.setPartNameGenerator(person -> {
            for (int i = 1; i < 32; i++)
            switch (person.getWorkerStatusTable(i)) {
                case WORK -> {
                    return "work";
                }
                case HOSPITAL -> {
                    return "bol";
                }
                case HOLIDAY -> {
                    return "otpusk";
                }
                case NOTHING -> {
                    return "nothing";
                }
                case ADMINOTP -> {
                    return "administrat";
                }
                case OTRABOTKA -> {
                    return "otrabotka";
                }
                case PERERABOTKA -> {
                    return "pererabotka";
                }
            }
            return null;
        });

            Grid.Column<Worker> nameColumn = grid
                    .addColumn(Worker::getFullNameWithInitials)
                    .setHeader("ФИО")
                    .setAutoWidth(true);
                    grid
                        .addColumn(Worker::getWorkTimeToTableView1)
                        .setHeader(Integer.toString(1))
                        .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView2)
                .setHeader(Integer.toString(2))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView3)
                .setHeader(Integer.toString(3))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView4)
                .setHeader(Integer.toString(4))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView5)
                .setHeader(Integer.toString(5))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView6)
                .setHeader(Integer.toString(6))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView7)
                .setHeader(Integer.toString(7))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView8)
                .setHeader(Integer.toString(8))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView9)
                .setHeader(Integer.toString(9))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView10)
                .setHeader(Integer.toString(10))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView11)
                .setHeader(Integer.toString(11))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView12)
                .setHeader(Integer.toString(12))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView13)
                .setHeader(Integer.toString(13))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView14)
                .setHeader(Integer.toString(14))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView15)
                .setHeader(Integer.toString(15))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView16)
                .setHeader(Integer.toString(16))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView17)
                .setHeader(Integer.toString(17))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView18)
                .setHeader(Integer.toString(18))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView19)
                .setHeader(Integer.toString(19))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView20)
                .setHeader(Integer.toString(20))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView21)
                .setHeader(Integer.toString(21))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView22)
                .setHeader(Integer.toString(22))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView23)
                .setHeader(Integer.toString(23))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView24)
                .setHeader(Integer.toString(24))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView25)
                .setHeader(Integer.toString(25))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView26)
                .setHeader(Integer.toString(26))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView27)
                .setHeader(Integer.toString(27))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView28)
                .setHeader(Integer.toString(28))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView29)
                .setHeader(Integer.toString(29))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView30)
                .setHeader(Integer.toString(30))
                .setAutoWidth(true);
        grid
                .addColumn(Worker::getWorkTimeToTableView31)
                .setHeader(Integer.toString(31))
                .setAutoWidth(true);
            add(grid);

        }
    public void setItemForGrid(String sc, Grid<Worker> grid) {
        switch (sc) {
            case "admin", "owner" -> grid.setItems(BrigEdit.workerList);
            case "volna1" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_1));
            case "volna2" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_2));
            case "volna3" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_3));
            case "volna4" -> grid.setItems(BrigEdit.mountMap.get(ConveyLine.LINE_4));
            case "sborka1" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_1));
            case "sborka2" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_2));
            case "sborka3" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_3));
            case "sborka4" -> grid.setItems(BrigEdit.builderMap.get(ConveyLine.LINE_4));
            case "tech" -> grid.setItems(BrigEdit.allTech);
        }

    }
    }

