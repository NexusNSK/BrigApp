package ru.markov.application.poi;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.data.Device;
import ru.markov.application.security.SecurityService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public void showReportDialog(Device device, int month, int day, SecurityService securityService) {
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
        Button correctReport = new Button();
        correctReport.setText("Внести корректировки");
        correctReport.setIcon(new Icon(VaadinIcon.REPLY));
        correctReport.addClassNames("yellow-button");
        correctReport.addClickListener(e ->{
            monthToOperations = monthSelect.getValue().getValue();
            dayToOperations = day;
            openFormDialog(device.getDeviceName());
        });
        if (securityService.getAuthenticatedUser().getUsername().equals("admin")
                ||securityService.getAuthenticatedUser().getUsername().equals("tech")) {
            reportDialogLayout.add(downloadLink, correctReport, closeButton);
        } else {
            reportDialogLayout.add(downloadLink, closeButton);
        }
        dialog.add(reportDialogLayout);
        dialog.open();
    }

    public StreamResource createExcelResource(Device device, int month, int day) {
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
}
