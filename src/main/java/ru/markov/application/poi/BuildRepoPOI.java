package ru.markov.application.poi;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.service.ConveyLine;
import ru.markov.application.views.GridEdit;
import ru.markov.application.views.Reports;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class BuildRepoPOI {
    private final SXSSFWorkbook buildBook = new SXSSFWorkbook();
    public void setAroundBorder(Cell cell) {
        CellStyle style = buildBook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(style);
    }
    public void setAroundBorderCenterAlignment(Cell cell) {
        CellStyle style = buildBook.createCellStyle();
        Font bold = buildBook.createFont();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(bold);
        cell.setCellStyle(style);
    }
    public void setAroundBorderCenterAlignmentTotal(Cell cell) {
        CellStyle style = buildBook.createCellStyle();
        Font bold = buildBook.createFont();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(bold);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }
    public void setStatusCellColorBuild(int workerIndex, int day, Cell cell, ConveyLine line) {
        CellStyle workStatusCell = buildBook.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = buildBook.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = buildBook.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = buildBook.createCellStyle();
        nothingStatusCell.setBorderBottom(BorderStyle.THIN);
        nothingStatusCell.setBorderLeft(BorderStyle.THIN);
        nothingStatusCell.setBorderRight(BorderStyle.THIN);
        nothingStatusCell.setBorderTop(BorderStyle.THIN);

        workStatusCell.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        workStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        holidayStatusCell.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        holidayStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        hospitalStatusCell.setFillForegroundColor(IndexedColors.RED.getIndex());
        hospitalStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        nothingStatusCell.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        nothingStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        switch (GridEdit.builderMap.get(line).get(workerIndex).getWorkerStatusAtDay(day)) {
            case ("Работает") -> cell.setCellStyle(workStatusCell);
            case ("Больничный") -> cell.setCellStyle(hospitalStatusCell);
            case ("Отпуск") -> cell.setCellStyle(holidayStatusCell);
            case ("Не определено") -> cell.setCellStyle(nothingStatusCell);
        }
    }
    public void initSheetBuild(Sheet sheet) {
        int totalSize = GridEdit.builderMap.get(ConveyLine.COMMON).size()
                + GridEdit.builderMap.get(ConveyLine.LINE_1).size()
                + GridEdit.builderMap.get(ConveyLine.LINE_2).size()
                + GridEdit.builderMap.get(ConveyLine.LINE_3).size()
                + GridEdit.builderMap.get(ConveyLine.LINE_4).size() + 8;
        for (int i = 0; i < totalSize + 1 + 3; i++) {
            sheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheet.getRow(i).createCell(j);
                setAroundBorder(sheet.getRow(i).getCell(j));
            }
        }
    }
    public void createHeaderGrid(Sheet sheet) {
        CellStyle cs = buildBook.createCellStyle();
        Font bold = buildBook.createFont();
        bold.setBold(true);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setFont(bold);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 33, 33));
        sheet.getRow(0).getCell(33).setCellValue("Итого часов");
        sheet.getRow(0).getCell(33).setCellStyle(cs);
        setAroundBorder(sheet.getRow(0).getCell(33));
        sheet.setColumnWidth(33, 3000);
        sheet.setColumnWidth(1, 10000);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
        sheet.getRow(2).getCell(0).setCellValue("ФИО");
        sheet.getRow(2).getCell(0).setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 32));
        sheet.getRow(0).getCell(2).setCellStyle(cs);
        sheet.getRow(0).getCell(2).setCellValue(getMonth());
        sheet.getRow(3).getCell(0).setCellStyle(cs);
        sheet.getRow(3).getCell(1).setCellStyle(cs);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
        sheet.getRow(3).getCell(0).setCellValue("Сборщики");
        sheet.getRow(3).getCell(0).setCellStyle(cs);
        setAroundBorderCenterAlignment(sheet.getRow(3).getCell(0));
        setAroundBorderCenterAlignment(sheet.getRow(3).getCell(1));


        for (int i = 2, j = 1; i <= 32; i++, j++) {
            sheet.getRow(2).getCell(i).setCellValue(j);
            sheet.getRow(2).getCell(i).setCellStyle(cs);
            sheet.setColumnWidth(i, 1000);
        }
        sheet.setColumnWidth(0, 1000);
    }
    public String getMonth() {
        return switch (Reports.month) {
            case (1) -> "Январь";
            case (2) -> "Февраль";
            case (3) -> "Март";
            case (4) -> "Апрель";
            case (5) -> "Май";
            case (6) -> "Июнь";
            case (7) -> "Июль";
            case (8) -> "Август";
            case (9) -> "Сентябрь";
            case (10) -> "Октябрь";
            case (11) -> "Ноябрь";
            case (12) -> "Декабрь";
            default -> "";
        };
    }
    public void reportList() {
        Sheet builderSheet = buildBook.createSheet("Сборщики");
        initSheetBuild(builderSheet);
        createHeaderGrid(builderSheet);
        repoLineBuild();
    }
    public BuildRepoPOI() throws IOException {
        Calendar date = new GregorianCalendar();
        DateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        sdf.format(date.getTime());
        reportList();
        FileOutputStream fos = new FileOutputStream("Template.xlsx");
        buildBook.write(fos);
        fos.close();
        System.out.println("Файл был записан на диск");
    }

    public void repoLineBuild() {
        String sheet = "Сборщики";
        int line1Index = 4;
        int line2Index = line1Index+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+2;
        int line3Index = line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+2;
        int line4Index = line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+2;
        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(4).getCell(0));
        buildBook.getSheet(sheet).getRow(4).getCell(0).setCellValue("Бригадная сборка 1");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_1).size(); i++) {
            buildBook.getSheet(sheet).getRow(i + 5).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(buildBook.getSheet(sheet).getRow(i + 5).getCell(0));
            buildBook.getSheet(sheet).getRow(i + 5).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_1).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(i + 5).getCell(days + 1), ConveyLine.LINE_1);
                    buildBook.getSheet(sheet).getRow(i + 5).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    buildBook.getSheet(sheet).getRow(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1)
                            .setCellValue(
                                    buildBook.getSheet(sheet).getRow(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    buildBook.getSheet(sheet).getRow(i + 5).getCell(33).setCellValue(
                            buildBook.getSheet(sheet).getRow(i + 5).getCell(33).getNumericCellValue()
                                    + buildBook.getSheet(sheet).getRow(i + 5).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(i + 5).getCell(days + 1), ConveyLine.LINE_1);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1, 4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1).getCell(0));
        buildBook.getSheet(sheet).getRow(4+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 1 линии

        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2Index, line2Index, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(line2Index).getCell(0));
        buildBook.getSheet(sheet).getRow(line2Index).getCell(0).setCellValue("Бригадная сборка 2");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_2).size(); i++) {
            buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(0));
            buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_2).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    buildBook.getSheet(sheet).getRow(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1)
                            .setCellValue(
                                    buildBook.getSheet(sheet).getRow(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(33).setCellValue(
                            buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(33).getNumericCellValue()
                                    + buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1, line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(0));
        buildBook.getSheet(sheet).getRow(line2Index+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 2 линии

        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3Index, line3Index, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(line3Index).getCell(0));
        buildBook.getSheet(sheet).getRow(line3Index).getCell(0).setCellValue("Бригадная сборка 3");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_3).size(); i++) {
            buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(0));
            buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_3).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    buildBook.getSheet(sheet).getRow(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1)
                            .setCellValue(
                                    buildBook.getSheet(sheet).getRow(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(33).setCellValue(
                            buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(33).getNumericCellValue()
                    + buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1, line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(0));
        buildBook.getSheet(sheet).getRow(line3Index+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 3 линии

        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4Index, line4Index, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(line4Index).getCell(0));
        buildBook.getSheet(sheet).getRow(line4Index).getCell(0).setCellValue("Бригадная сборка 4");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_4).size(); i++) {
            buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(0));
            buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_4).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    buildBook.getSheet(sheet).getRow(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1)
                            .setCellValue(
                                    buildBook.getSheet(sheet).getRow(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(33).setCellValue(
                            buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(33).getNumericCellValue()
                                    + buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, buildBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    setAroundBorderCenterAlignmentTotal(buildBook.getSheet(sheet).getRow(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        buildBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1, line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1, 0, 1));
        setAroundBorderCenterAlignment(buildBook.getSheet(sheet).getRow(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(0));
        buildBook.getSheet(sheet).getRow(line4Index+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 4 линии
    }
}


