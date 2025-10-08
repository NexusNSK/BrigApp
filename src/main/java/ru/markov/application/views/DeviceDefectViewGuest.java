package ru.markov.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.data.Device;
import ru.markov.application.service.Serial;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;

@Route(value = "device_defect_guest", layout = GuestLayout.class)
@AnonymousAllowed
@CssImport("./styles.css")
@CssImport("./grid.css")
public class DeviceDefectViewGuest extends VerticalLayout {
    public static HashMap<String, Device> devicesGuest = DeviceDefectView.devices;
    private ComboBox<Month> monthSelect = new ComboBox<>();
    Grid<Device> grid = new Grid<>(Device.class, false);
    DatePickerI18n russianI18n = new DatePickerI18n()
            .setWeekdays(Arrays.asList(
                    "Воскресенье", "Понедельник", "Вторник", "Среда",
                    "Четверг", "Пятница", "Суббота"))
            .setWeekdaysShort(Arrays.asList("Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"))
            .setMonthNames(Arrays.asList(
                    "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"))
            .setToday("Сегодня")
            .setCancel("Отмена")
            .setFirstDayOfWeek(1);

    public DeviceDefectViewGuest() {
            Tabs tabs = new Tabs();
            grid.addClassName("repoGrid");
            VerticalLayout contentArea = new VerticalLayout();
            Tab tableDefect = new Tab("Сводка");
            VerticalLayout tableDefectContent = createTableDefectContent();
            tabs.add(tableDefect);
            contentArea.add(tableDefectContent);
            add(tabs, contentArea);
        }

    // --- Вкладка "Сводка" с разделением по линиям ---
    private VerticalLayout createTableDefectContent() {
        VerticalLayout tableDefectLayout = new VerticalLayout();
        initializeMonthSelector();
        monthSelect.addValueChangeListener(event -> updateGroupedGrids(tableDefectLayout));
        tableDefectLayout.add(monthSelect);
        updateGroupedGrids(tableDefectLayout);
        tableDefectLayout.setPadding(false);
        tableDefectLayout.setSpacing(false);
        return tableDefectLayout;
    }

    private void initializeMonthSelector() {
        monthSelect.setItems(Month.values());
        monthSelect.setItemLabelGenerator(month -> month.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("RU")));
        monthSelect.setLabel("Выберите месяц");
        monthSelect.setValue(LocalDate.now().getMonth());
    }

    private void updateGroupedGrids(VerticalLayout layout) {
        layout.removeAll();
        layout.add(monthSelect);

        Month selectedMonth = monthSelect.getValue();
        int monthValue = selectedMonth != null ? selectedMonth.getValue() : LocalDate.now().getMonthValue();

        Map<String, List<Device>> lineToDevices = new LinkedHashMap<>();
        for (Device device : filterDevicesByMonth(monthValue)) {
            HashMap<Integer, String> defaultValue = new HashMap<>();
            Map<Integer, String> dayLines = device.lineMap.getOrDefault(
                    monthValue,
                    defaultValue
            );

            // Собираем уникальные линии для устройства
            Set<String> uniqueLines = new HashSet<>();
            for (Map.Entry<Integer, String> entry : dayLines.entrySet()) {
                String line = entry.getValue();
                if (line != null && !line.isEmpty()) {
                    uniqueLines.add(line);
                }
            }
            if (uniqueLines.isEmpty()) {
                uniqueLines.add("Неизвестно");
            }
            for (String line : uniqueLines) {
                lineToDevices.computeIfAbsent(line, k -> new ArrayList<>()).add(device);
            }
        }

        // Желаемый порядок линий
        List<String> lineOrder = Arrays.asList("Линия 1", "Линия 2", "Линия 3", "Линия 4", "Неизвестно");

        // Список реально присутствующих линий в нужном порядке
        List<String> presentLines = lineOrder.stream()
                .filter(lineToDevices::containsKey)
                .collect(Collectors.toList());

        // Добавляем остальные линии (если вдруг есть нестандартные)
        lineToDevices.keySet().stream()
                .filter(line -> !presentLines.contains(line))
                .forEach(presentLines::add);

        // Выводим таблицы в нужном порядке
        for (String lineName : presentLines) {
            List<Device> devicesForLine = lineToDevices.get(lineName);
            H4 header = new H4(lineName);
            header.addClassName("line-header");
            layout.add(header);

            Grid<Device> grid = new Grid<>(Device.class, false);
            grid.addClassName("compact-grid");
            configureGrid(grid);
            addDayColumnsToGrid(grid, selectedMonth, lineName, monthValue);
            grid.setItems(devicesForLine);
            grid.setAllRowsVisible(true);
            layout.add(grid);
        }
    }


    private void configureGrid(Grid<Device> grid) {
        grid.addClassName("repoGrid");
        grid.removeAllColumns();
        grid.addColumn(Device::getDeviceName)
                .setHeader("Устройство")
                .setFlexGrow(0)
                .setAutoWidth(true)
                .addClassName("repoGrid::part(cell).first-column-cell");
        grid.setAllRowsVisible(true);
        grid.setHeightFull();
        grid.setWidthFull();
    }

    private void addDayColumnsToGrid(Grid<Device> grid, Month selectedMonth, String lineName, int monthValue) {
        List<Integer> days = getDaysInMonth(selectedMonth);

        days.forEach(day -> {
            grid.addComponentColumn(device -> {
                // Проверяем принадлежность к линии
                HashMap<Integer, String> defaultValue = new HashMap<>();
                String deviceLine = device.lineMap
                        .getOrDefault(monthValue, defaultValue)
                        .get(day);

                if (deviceLine.equals(lineName) && device.totalPartMap.get(monthValue).get(day) > 0) {
                    Icon content = VaadinIcon.CHECK.create();
                    content.getElement().getThemeList().add("badge success");
                    content.getStyle().set("cursor", "pointer");
                    content.addClickListener(e -> showReportDialog(device, monthValue, day));
                    return content;

                } else if (
                        ("Линия 1".equals(lineName) && Boolean.TRUE.equals(device.lineMapRange1.get(monthValue).get(day))) ||
                                ("Линия 2".equals(lineName) && Boolean.TRUE.equals(device.lineMapRange2.get(monthValue).get(day))) ||
                                ("Линия 3".equals(lineName) && Boolean.TRUE.equals(device.lineMapRange3.get(monthValue).get(day))) ||
                                ("Линия 4".equals(lineName) && Boolean.TRUE.equals(device.lineMapRange4.get(monthValue).get(day)))
                ) {
                    Span coloredCell = new Span("➤");
                    coloredCell.addClassName("part-range-cell");
                    return coloredCell;
                } else {
                    return new Span("");
                }
            }).setHeader(day.toString()).setAutoWidth(true).setFlexGrow(0);
        });
    }

    private List<Integer> getDaysInMonth(Month month) {
        return IntStream.rangeClosed(1, month.length(Year.now().isLeap()))
                .boxed()
                .collect(Collectors.toList());
    }

    private void showReportDialog(Device device, int month, int day) {
        int totalDefectDeviceCount = 0;
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        VerticalLayout reportDialogLayout = new VerticalLayout();
        reportDialogLayout.setPadding(true);
        reportDialogLayout.setSpacing(true);

        H3 title = new H3("Отчет по устройству " + device.getDeviceName());
        reportDialogLayout.add(title);

        H4 line = new H4(device.lineMap.get(month).get(day));
        reportDialogLayout.add(line);

        H4 startFinishPart = new H4(device.startPartDate.get(month).get(day) + " - " + device.finishPartDate.get(month).get(day)+"\n");
        reportDialogLayout.add(startFinishPart);

        Integer count = device.totalPartMap
                .get(month)
                .get(day);
        Span countSpan = new Span("Количество в партии: " + count);
        reportDialogLayout.add(countSpan);

        H4 defectsTitle = new H4("Брак в этой партии:");
        reportDialogLayout.add(defectsTitle);

        boolean hasDefects = false;
        for (Map.Entry<String, HashMap<Integer, HashMap<Integer, Integer>>> defectEntry : device.deviceMap.entrySet()) {
            String defectName = defectEntry.getKey();
            HashMap<Integer, HashMap<Integer, Integer>> monthMap = defectEntry.getValue();
            Integer defectCount = monthMap.getOrDefault(month, new HashMap<>())
                    .getOrDefault(day, 0);
            if (defectCount != null && defectCount > 0) {
                hasDefects = true;
                Span defectSpan = new Span(defectName + ": " + defectCount);
                reportDialogLayout.add(defectSpan);
                totalDefectDeviceCount += defectCount;
            }
        }
        Span totalCount = new Span("Всего брака: " + totalDefectDeviceCount);
        totalCount.addClassName("total-count");
        reportDialogLayout.add(totalCount);
        Span percentDefect = new Span("Процент брака: " + String.format("%.2f",((float) totalDefectDeviceCount/count*100)) + "%");
        percentDefect.addClassName("percent-defect");
        reportDialogLayout.add(percentDefect);
        if (!hasDefects) {
            reportDialogLayout.add(new Span("Брак не зафиксирован."));
        }

        Button closeButton = new Button("Закрыть", e -> dialog.close());
        closeButton.setClassName("red-button");
        Button downloadReport = new Button("Скачать отчёт");
        downloadReport.setClassName("green-button");
        StreamResource resource = createExcelResource(device, month, day);
        Anchor downloadLink = new Anchor(resource, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(downloadReport);
        reportDialogLayout.add(downloadLink, closeButton);
        dialog.add(reportDialogLayout);
        dialog.open();
    }

    private StreamResource createExcelResource(Device device, int month, int day) {
        String safeDeviceName = device.getDeviceName().replace('/', 'x').replace(',', 'x');
        return new StreamResource("Отчёт по " + safeDeviceName + ".xlsx", () -> {
            try (Workbook workbook = new SXSSFWorkbook();
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

                SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet("Отчет");
                sheet.trackAllColumnsForAutoSizing();

                Font boldFont = workbook.createFont();
                boldFont.setBold(true);

                CellStyle boldLeft = workbook.createCellStyle();
                boldLeft.setFont(boldFont);
                boldLeft.setAlignment(HorizontalAlignment.LEFT);
                boldLeft.setVerticalAlignment(VerticalAlignment.TOP);
                setBorders(boldLeft);
                boldLeft.setWrapText(true);

                CellStyle boldRight = workbook.createCellStyle();
                boldRight.setFont(boldFont);
                boldRight.setAlignment(HorizontalAlignment.RIGHT);
                setBorders(boldRight);

                CellStyle normalLeft = workbook.createCellStyle();
                normalLeft.setAlignment(HorizontalAlignment.LEFT);
                setBorders(normalLeft);

                CellStyle normalRight = workbook.createCellStyle();
                normalRight.setAlignment(HorizontalAlignment.RIGHT);
                setBorders(normalRight);

                CellStyle yellowBoldLeft = workbook.createCellStyle();
                yellowBoldLeft.cloneStyleFrom(boldLeft);
                yellowBoldLeft.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                yellowBoldLeft.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle yellowBoldRight = workbook.createCellStyle();
                yellowBoldRight.cloneStyleFrom(boldRight);
                yellowBoldRight.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                yellowBoldRight.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle boldCenter = workbook.createCellStyle();
                boldCenter.setFont(boldFont);
                boldCenter.setAlignment(HorizontalAlignment.CENTER);
                boldCenter.setVerticalAlignment(VerticalAlignment.CENTER);
                setBorders(boldCenter);

                String deviceName = device.getDeviceName();
                String line = device.lineMap.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                String startDate = device.startPartDate.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                String finishDate = device.finishPartDate.getOrDefault(month, new HashMap<>()).getOrDefault(day, "—");
                int totalPart = device.totalPartMap.getOrDefault(month, new HashMap<>()).getOrDefault(day, 0);

                Set<String> defects = device.deviceMap.entrySet().stream()
                        .filter(entry -> {
                            HashMap<Integer, HashMap<Integer, Integer>> monthsMap = entry.getValue();
                            HashMap<Integer, Integer> daysMap = monthsMap.get(month);
                            int count = daysMap != null ? daysMap.getOrDefault(day, 0) : 0;
                            return count > 0;
                        })
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toCollection(LinkedHashSet::new));

                int defectTotal = 0;
                Map<String, Integer> defectCounts = new LinkedHashMap<>();
                for (String defect : defects) {
                    int count = device.deviceMap.get(defect)
                            .getOrDefault(month, new HashMap<>())
                            .getOrDefault(day, 0);
                    defectCounts.put(defect, count);
                    defectTotal += count;
                }

                double defectPercent = totalPart > 0 ? (double) defectTotal / totalPart * 100 : 0;

                int rowNum = 0;

                Row row1 = sheet.createRow(rowNum++);
                Cell cellLine = row1.createCell(0);
                cellLine.setCellValue(line);
                cellLine.setCellStyle(boldLeft);

                Row row2 = sheet.createRow(rowNum++);
                Cell cellDates = row2.createCell(0);
                String dates = startDate + " - " + finishDate;
                cellDates.setCellValue(dates);
                cellDates.setCellStyle(boldLeft);

                sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                Cell cellDevice = row1.createCell(1);
                cellDevice.setCellValue(deviceName);
                cellDevice.setCellStyle(boldCenter);

                Cell cellDevice2 = row2.createCell(1);
                cellDevice2.setCellStyle(boldCenter);

                Row row3 = sheet.createRow(rowNum++);
                Cell cellPartLabel = row3.createCell(0);
                cellPartLabel.setCellValue("Партия (шт)");
                cellPartLabel.setCellStyle(boldLeft);

                Cell cellPartValue = row3.createCell(1);
                cellPartValue.setCellValue(totalPart);
                cellPartValue.setCellStyle(boldRight);


                for (String defect : defects) {
                    Row row = sheet.createRow(rowNum++);
                    Cell cellDefect = row.createCell(0);
                    cellDefect.setCellValue(defect);
                    cellDefect.setCellStyle(normalLeft);

                    Cell cellDefectCount = row.createCell(1);
                    cellDefectCount.setCellValue(defectCounts.get(defect));
                    cellDefectCount.setCellStyle(normalRight);
                }

                Row rowItogo = sheet.createRow(rowNum++);
                Cell cellItogo = rowItogo.createCell(0);
                cellItogo.setCellValue("итого");
                cellItogo.setCellStyle(yellowBoldLeft);

                Cell cellItogoValue = rowItogo.createCell(1);
                cellItogoValue.setCellValue(defectTotal);
                cellItogoValue.setCellStyle(yellowBoldRight);

                Row rowPercent = sheet.createRow(rowNum++);
                Cell cellPercentLabel = rowPercent.createCell(0);
                cellPercentLabel.setCellValue("процент брака");
                cellPercentLabel.setCellStyle(boldLeft);

                Cell cellPercentValue = rowPercent.createCell(1);
                cellPercentValue.setCellValue(String.format("%.2f%%", defectPercent));
                cellPercentValue.setCellStyle(boldRight);

                for (int col = 0; col <= 1; col++) {
                    sheet.autoSizeColumn(col);
                    int currentWidth = sheet.getColumnWidth(col);
                    sheet.setColumnWidth(col, currentWidth + 512);
                }
                sheet.setColumnWidth(1, (deviceName.length() * 256) + 512);
                workbook.write(bos);
                return new ByteArrayInputStream(bos.toByteArray());

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private void setBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    private boolean deviceHasAnyRecordInMonth(Device device, int monthValue) {
        for (String defectType : device.deviceMap.keySet()) {
            HashMap<Integer, HashMap<Integer, Integer>> monthMap = device.deviceMap.get(defectType);
            if (monthMap == null) continue;

            HashMap<Integer, Integer> dayMap = monthMap.get(monthValue);
            if (dayMap == null) continue;

            for (Integer count : dayMap.values()) {
                if (count != null && count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Collection<Device> filterDevicesByMonth(int monthValue) {
        return devicesGuest.values().stream()
                .filter(device -> deviceHasAnyRecordInMonth(device, monthValue))
                .collect(Collectors.toList());
    }
}
