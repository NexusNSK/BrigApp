package ru.markov.application.views;

import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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
            setItemForGrid(username, grid);
            grid.setClassName("work-time-grid");
            grid.setWidthFull();
            grid.setMinHeight("500px");
            grid.setHeight("800px");
            grid.addThemeVariants(
                    GridVariant.LUMO_ROW_STRIPES, // Добавляем полосатую тему
                    GridVariant.LUMO_COLUMN_BORDERS // Добавляем рамки к столбцам
            );

            Grid.Column<Worker> nameColumn = grid
                    .addColumn(Worker::getFullNameWithInitials)
                    .setHeader("ФИО")
                    .setAutoWidth(true)
                    .setFrozen(true);

            Grid.Column<Worker> d1Column = grid
                    .addColumn(Worker::getWorkTimeToTableView1)
                    .setHeader(Integer.toString(1))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(1)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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
            Grid.Column<Worker> d2Column = grid
                    .addColumn(Worker::getWorkTimeToTableView2)
                    .setHeader(Integer.toString(2))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(2)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d3Column = grid
                    .addColumn(Worker::getWorkTimeToTableView3)
                    .setHeader(Integer.toString(3))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(3)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d4Column = grid
                    .addColumn(Worker::getWorkTimeToTableView4)
                    .setHeader(Integer.toString(4))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(4)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d5Column = grid
                    .addColumn(Worker::getWorkTimeToTableView5)
                    .setHeader(Integer.toString(5))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(5)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d6Column = grid
                    .addColumn(Worker::getWorkTimeToTableView6)
                    .setHeader(Integer.toString(6))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(6)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d7Column = grid
                    .addColumn(Worker::getWorkTimeToTableView7)
                    .setHeader(Integer.toString(7))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(7)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d8Column = grid
                    .addColumn(Worker::getWorkTimeToTableView8)
                    .setHeader(Integer.toString(8))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(8)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d9Column = grid
                    .addColumn(Worker::getWorkTimeToTableView9)
                    .setHeader(Integer.toString(9))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(9)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d10Column = grid
                    .addColumn(Worker::getWorkTimeToTableView10)
                    .setHeader(Integer.toString(10))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(10)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d11Column = grid
                    .addColumn(Worker::getWorkTimeToTableView11)
                    .setHeader(Integer.toString(11))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(11)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d12Column = grid
                    .addColumn(Worker::getWorkTimeToTableView12)
                    .setHeader(Integer.toString(12))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(12)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d13Column = grid
                    .addColumn(Worker::getWorkTimeToTableView13)
                    .setHeader(Integer.toString(13))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(13)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d14Column = grid
                    .addColumn(Worker::getWorkTimeToTableView14)
                    .setHeader(Integer.toString(14))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(14)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d15Column = grid
                    .addColumn(Worker::getWorkTimeToTableView15)
                    .setHeader(Integer.toString(15))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(15)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d16Column = grid
                    .addColumn(Worker::getWorkTimeToTableView16)
                    .setHeader(Integer.toString(16))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(16)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d17Column = grid
                    .addColumn(Worker::getWorkTimeToTableView17)
                    .setHeader(Integer.toString(17))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(17)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d18Column = grid
                    .addColumn(Worker::getWorkTimeToTableView18)
                    .setHeader(Integer.toString(18))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(18)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d19Column = grid
                    .addColumn(Worker::getWorkTimeToTableView19)
                    .setHeader(Integer.toString(19))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(19)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d20Column = grid
                    .addColumn(Worker::getWorkTimeToTableView20)
                    .setHeader(Integer.toString(20))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(20)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d21Column = grid
                    .addColumn(Worker::getWorkTimeToTableView21)
                    .setHeader(Integer.toString(21))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(21)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d22Column = grid
                    .addColumn(Worker::getWorkTimeToTableView22)
                    .setHeader(Integer.toString(22))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(22)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d23Column = grid
                    .addColumn(Worker::getWorkTimeToTableView23)
                    .setHeader(Integer.toString(23))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(23)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d24Column = grid
                    .addColumn(Worker::getWorkTimeToTableView24)
                    .setHeader(Integer.toString(24))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(24)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d25Column = grid
                    .addColumn(Worker::getWorkTimeToTableView25)
                    .setHeader(Integer.toString(25))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(25)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d26Column = grid
                    .addColumn(Worker::getWorkTimeToTableView26)
                    .setHeader(Integer.toString(26))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(26)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d27Column = grid
                    .addColumn(Worker::getWorkTimeToTableView27)
                    .setHeader(Integer.toString(27))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(27)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d28Column = grid
                    .addColumn(Worker::getWorkTimeToTableView28)
                    .setHeader(Integer.toString(28))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(28)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d29Column = grid
                    .addColumn(Worker::getWorkTimeToTableView29)
                    .setHeader(Integer.toString(29))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(29)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d30Column = grid
                    .addColumn(Worker::getWorkTimeToTableView30)
                    .setHeader(Integer.toString(30))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(30)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> {
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

            Grid.Column<Worker> d31Column = grid
                    .addColumn(Worker::getWorkTimeToTableView31)
                    .setHeader(Integer.toString(31))
                    .setTextAlign(ColumnTextAlign.CENTER)
                    .setAutoWidth(false)
                    .setWidth("51px")
                    .setFlexGrow(0)
                    .setPartNameGenerator(person -> {
                        switch (person.gTwSd(31)) {
                            case WORK -> {
                                return "work";
                            }
                            case HOSPITAL -> {
                                return "bol";
                            }
                            case HOLIDAY, HOLYWORK -> { 
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

