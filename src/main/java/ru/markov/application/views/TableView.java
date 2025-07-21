package ru.markov.application.views;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import ru.markov.application.data.Worker;
import ru.markov.application.security.SecurityService;
import java.time.YearMonth;
import java.time.LocalDateTime;

@Route(value = "tableview", layout = MainLayout.class)
@PageTitle("BrigApp א Просмотр табеля")
@PermitAll
public class TableView extends Div {

    public TableView(SecurityService securityService) {
        if (!securityService.getAuthenticatedUser().getUsername().equals("admin") & ServiceTools.serviceFlag) {
            TextArea serviceMessage = new TextArea();
            serviceMessage.setMinWidth("500px");
            serviceMessage.setMaxWidth("500px");
            serviceMessage.setReadOnly(true);
            serviceMessage.setLabel("Внимание");
            serviceMessage.setPrefixComponent(VaadinIcon.QUESTION_CIRCLE.create());
            serviceMessage.setValue("Извините, идут сервисные работы.\nПовторите попытку позже.");
            add(serviceMessage);
        } else {
            String username = securityService.getAuthenticatedUser().getUsername();
            Grid<Worker> grid = new Grid<>(Worker.class, false);
            grid.setAllRowsVisible(true);
            setItemForGrid(username, grid);
            grid.setClassName("work-time-grid");
            grid.setWidthFull();
            grid.setMinHeight("500px");
            grid.setHeightFull();
            grid.addThemeVariants(
                    GridVariant.LUMO_ROW_STRIPES, // Добавляем полосатую тему
                    GridVariant.LUMO_COLUMN_BORDERS // Добавляем рамки к столбцам
            );

            Grid.Column<Worker> nameColumn = grid
                    .addColumn(Worker::getFullNameWithInitials)
                    .setHeader("ФИО")
                    .setAutoWidth(true)
                    .setFrozen(true);

            YearMonth ym = YearMonth.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth());
            int daysInMonth = ym.lengthOfMonth();

            for (int day = 1; day <= daysInMonth; day++) {
                int countDay = day;
                grid.addColumn(worker -> worker.getWorkTimeToTableView(countDay))
                        .setHeader(Integer.toString(countDay))
                        .setTextAlign(ColumnTextAlign.CENTER)
                        .setAutoWidth(false)
                        .setWidth("51px")
                        .setFlexGrow(0)
                        .setPartNameGenerator(worker -> {
                            return switch (worker.gTwSd(countDay)) {
                                case WORK -> "work";
                                case HOSPITAL -> "bol";
                                case HOLIDAY, HOLYWORK -> "otpusk";
                                case NOTHING -> "nothing";
                                case ADMINOTP -> "administrat";
                                case OTRABOTKA -> "otrabotka";
                                case PERERABOTKA -> "pererabotka";
                            };
                        });
            }

            Grid.Column<Worker> d32Column = grid
                    .addColumn(Worker::getWorkerAllTimeToTableView)
                    .setHeader("Итого")
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("100px")
                    .setFlexGrow(0);
            add(grid);
        }
    }

    public void setItemForGrid(String sc, Grid<Worker> grid) {
        WorkTime.switchItemForGrid(sc, grid);
    }
}

