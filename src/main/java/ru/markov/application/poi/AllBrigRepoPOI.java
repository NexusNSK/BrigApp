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

public class AllBrigRepoPOI {

    private final SXSSFWorkbook allBook = new SXSSFWorkbook();
    public void setAroundBorder(Cell cell) {
        CellStyle style = allBook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(style);
    }
    public void setAroundBorderCenterAlignment(Cell cell) {
        CellStyle style = allBook.createCellStyle();
        Font bold = allBook.createFont();
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
        CellStyle style = allBook.createCellStyle();
        Font bold = allBook.createFont();
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
    public void setStatusCellColorMount(int workerIndex, int day, Cell cell, ConveyLine line) {
        CellStyle workStatusCell = allBook.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = allBook.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = allBook.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = allBook.createCellStyle();
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

        switch (GridEdit.mountMap.get(line).get(workerIndex).getWorkerStatusAtDay(day)) {
            case ("Работает") -> cell.setCellStyle(workStatusCell);
            case ("Больничный") -> cell.setCellStyle(hospitalStatusCell);
            case ("Отпуск") -> cell.setCellStyle(holidayStatusCell);
            case ("Не определено") -> cell.setCellStyle(nothingStatusCell);
        }
    }
    public void setStatusCellColorBuild(int workerIndex, int day, Cell cell, ConveyLine line) {
        CellStyle workStatusCell = allBook.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = allBook.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = allBook.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = allBook.createCellStyle();
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
    public void setStatusCellColorTech(int workerIndex, int day, Cell cell) {
        CellStyle workStatusCell = allBook.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = allBook.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = allBook.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = allBook.createCellStyle();
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

        switch (GridEdit.techList.get(workerIndex).getWorkerStatusAtDay(day)) {
            case ("Работает") -> cell.setCellStyle(workStatusCell);
            case ("Больничный") -> cell.setCellStyle(hospitalStatusCell);
            case ("Отпуск") -> cell.setCellStyle(holidayStatusCell);
            case ("Не определено") -> cell.setCellStyle(nothingStatusCell);
        }
    }
    public void initSheet(Sheet sheet) {
        int totalSize = GridEdit.workerList.size()+19;
        for (int i = 0; i < totalSize + 1 + 3; i++) {
            sheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheet.getRow(i).createCell(j);
                sheet.getRow(i).getCell(j).setCellValue("1");
                setAroundBorder(sheet.getRow(i).getCell(j));
            }
        }
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
    public void createHeaderGrid(Sheet sheet) {
        for (int i = 0; i < 4; i++) {
            sheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheet.getRow(i).createCell(j);
                setAroundBorder(sheet.getRow(i).getCell(j));
            }
        }
        CellStyle cs = allBook.createCellStyle();
        Font bold = allBook.createFont();
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

        for (int i = 2, j = 1; i <= 32; i++, j++) {
            sheet.getRow(2).getCell(i).setCellValue(j);
            sheet.getRow(2).getCell(i).setCellStyle(cs);
            sheet.setColumnWidth(i, 1000);
        }
        sheet.setColumnWidth(0, 1000);
    }
    public void reportList() {
        Sheet allBrigSheet = allBook.createSheet("Все бригады");
        createHeaderGrid(allBrigSheet);
        repoAllLineBuild(allBrigSheet);
    }
    public AllBrigRepoPOI() throws IOException {
        Calendar date = new GregorianCalendar();
        DateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        sdf.format(date.getTime());
        reportList();
        FileOutputStream fos = new FileOutputStream("Template.xlsx");
        allBook.write(fos);
        fos.close();
        System.out.println("Файл был записан на диск");
    }
    public void repoAllLineBuild(Sheet sheets){
        CellStyle cs = allBook.createCellStyle();
        Font bold = allBook.createFont();
        bold.setBold(true);
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setFont(bold);

        //бригада монтажники

        for (int i = 4; i <= 4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }
        String sheet = "Все бригады";
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
        allBook.getSheet(sheet).getRow(3).getCell(0).setCellValue("Монтажники");
        allBook.getSheet(sheet).getRow(3).getCell(0).setCellStyle(cs);
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(3).getCell(0));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(3).getCell(1));

        int line1IndexMount = 5;
        int line2IndexMount = line1IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1;
        int line3IndexMount = line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+2;
        int line4IndexMount = line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+2;
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(4).getCell(0));
        allBook.getSheet(sheet).getRow(4).getCell(0).setCellValue("Волна 1");
        for (int i = 0; i < GridEdit.mountMap.get(ConveyLine.LINE_1).size(); i++) {
            allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(0));
            allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(1).setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_1).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.mountMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(days + 1), ConveyLine.LINE_1);
                    allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(days + 1)
                            .setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(33).getNumericCellValue()
                                    +allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(i + line1IndexMount).getCell(days + 1), ConveyLine.LINE_1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1, 4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(4+GridEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 1 линии

        for (int i = line2IndexMount; i <= line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }

        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2IndexMount, line2IndexMount, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line2IndexMount).getCell(0));
        allBook.getSheet(sheet).getRow(line2IndexMount).getCell(0).setCellValue("Волна 2");
        for (int i = 0; i < GridEdit.mountMap.get(ConveyLine.LINE_2).size(); i++) {
            allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(0));
            allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(1).setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_2).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.mountMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(33).getNumericCellValue()
                                    +allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(line2IndexMount+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1, line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 2 линии

        for (int i = line2IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_2).size()+2; i <= line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }

        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3IndexMount, line3IndexMount, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line3IndexMount).getCell(0));
        allBook.getSheet(sheet).getRow(line3IndexMount).getCell(0).setCellValue("Волна 3");
        for (int i = 0; i < GridEdit.mountMap.get(ConveyLine.LINE_3).size(); i++) {
            allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(0));
            allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(1).setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_3).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.mountMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(33).getNumericCellValue()
                                    +allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(line3IndexMount+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1, line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 3 линии

        for (int i = line3IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_3).size()+2; i <= line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }

        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4IndexMount, line4IndexMount, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line4IndexMount).getCell(0));
        allBook.getSheet(sheet).getRow(line4IndexMount).getCell(0).setCellValue("Волна 4");
        for (int i = 0; i < GridEdit.mountMap.get(ConveyLine.LINE_4).size(); i++) {
            allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(0));
            allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(1).setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_4).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.mountMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(33).getNumericCellValue()
                                    +allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, allBook.getSheet(sheet).getRow(line4IndexMount+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1, line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 4 линии


        //бригада сборщики
        int buildHeaderIndex = line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+2;
        int line1IndexBuild = buildHeaderIndex+2;
        int line2IndexBuild = line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1;
        int line3IndexBuild = line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+2;
        int line4IndexBuild = line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+2;

        for (int i = line4IndexMount+GridEdit.mountMap.get(ConveyLine.LINE_4).size()+2; i <= line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size(); i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }


        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(buildHeaderIndex, buildHeaderIndex, 0, 1));
        allBook.getSheet(sheet).getRow(buildHeaderIndex).getCell(0).setCellValue("Сборщики");
        allBook.getSheet(sheet).getRow(buildHeaderIndex).getCell(0).setCellStyle(cs);
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(buildHeaderIndex).getCell(0));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(buildHeaderIndex).getCell(1));


        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(buildHeaderIndex+1, buildHeaderIndex+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(buildHeaderIndex+1).getCell(0));
        allBook.getSheet(sheet).getRow(buildHeaderIndex+1).getCell(0).setCellValue("Бригадная сборка 1");

        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_1).size(); i++) {
            allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(0));
            allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_1).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(days + 1), ConveyLine.LINE_1);
                    allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(33).getNumericCellValue()
                                    + allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(i + line1IndexBuild).getCell(days + 1), ConveyLine.LINE_1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size(), line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size(), 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()).getCell(0));
        allBook.getSheet(sheet).getRow(line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 1 линии

        for (int i = line1IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_1).size()+1; i <= line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }

        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2IndexBuild, line2IndexBuild, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line2IndexBuild).getCell(0));
        allBook.getSheet(sheet).getRow(line2IndexBuild).getCell(0).setCellValue("Бригадная сборка 2");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_2).size(); i++) {
            allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(0));
            allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_2).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(33).getNumericCellValue()
                                    + allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(line2IndexBuild+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1, line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 2 линии

        for (int i = line2IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_2).size()+2; i <= line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }

        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3IndexBuild, line3IndexBuild, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line3IndexBuild).getCell(0));
        allBook.getSheet(sheet).getRow(line3IndexBuild).getCell(0).setCellValue("Бригадная сборка 3");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_3).size(); i++) {
            allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(0));
            allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_3).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(33).getNumericCellValue()
                                    + allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(line3IndexBuild+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1, line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 3 линии

        for (int i = line3IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_3).size()+2; i <= line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1; i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }

        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4IndexBuild, line4IndexBuild, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line4IndexBuild).getCell(0));
        allBook.getSheet(sheet).getRow(line4IndexBuild).getCell(0).setCellValue("Бригадная сборка 4");
        for (int i = 0; i < GridEdit.builderMap.get(ConveyLine.LINE_4).size(); i++) {
            allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(0));
            allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(1).setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_4).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.builderMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(days + 1)
                            .setCellValue(GridEdit.builderMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(33).getNumericCellValue()
                                    + allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorBuild(i, days, allBook.getSheet(sheet).getRow(line4IndexBuild+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1, line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1, 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(0));
        allBook.getSheet(sheet).getRow(line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 4 линии

        //бригада техники
        int techHeaderIndex = line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+2;
        int techIndex = techHeaderIndex+1;

        for (int i = line4IndexBuild+GridEdit.builderMap.get(ConveyLine.LINE_4).size()+2; i <= techIndex+GridEdit.techList.size(); i++) {
            sheets.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheets.getRow(i).createCell(j);
                setAroundBorder(sheets.getRow(i).getCell(j));
            }
        }


        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(techHeaderIndex, techHeaderIndex, 0, 1));
        allBook.getSheet(sheet).getRow(techHeaderIndex).getCell(0).setCellValue("Техники");
        allBook.getSheet(sheet).getRow(techHeaderIndex).getCell(0).setCellStyle(cs);
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(techHeaderIndex).getCell(0));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(techHeaderIndex).getCell(1));


        for (int i = 0; i < GridEdit.techList.size(); i++) {
            allBook.getSheet(sheet).getRow(i + techIndex).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(allBook.getSheet(sheet).getRow(i + techIndex).getCell(0));
            allBook.getSheet(sheet).getRow(i + techIndex).getCell(1).setCellValue(GridEdit.techList.get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(GridEdit.techList.get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorTech(i, days, allBook.getSheet(sheet).getRow(i + techIndex).getCell(days + 1));
                    allBook.getSheet(sheet).getRow(i + techIndex).getCell(days + 1)
                            .setCellValue(GridEdit.techList.get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    allBook.getSheet(sheet).getRow(techIndex+GridEdit.techList.size()).getCell(days + 1)
                            .setCellValue(
                                    allBook.getSheet(sheet).getRow(techIndex+GridEdit.techList.size()).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(techIndex+GridEdit.techList.size()).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    allBook.getSheet(sheet).getRow(i + techIndex).getCell(33).setCellValue(
                            allBook.getSheet(sheet).getRow(i + techIndex).getCell(33).getNumericCellValue()
                                    + allBook.getSheet(sheet).getRow(i + techIndex).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorTech(i, days, allBook.getSheet(sheet).getRow(i + techIndex).getCell(days + 1));
                    setAroundBorderCenterAlignmentTotal(allBook.getSheet(sheet).getRow(techIndex+GridEdit.techList.size()).getCell(days + 1));
                    days++;
                }
            }
        }
        allBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(techIndex+GridEdit.techList.size(), techIndex+GridEdit.techList.size(), 0, 1));
        setAroundBorderCenterAlignment(allBook.getSheet(sheet).getRow(techIndex+GridEdit.techList.size()).getCell(0));
        allBook.getSheet(sheet).getRow(techIndex+GridEdit.techList.size()).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт

    }

}
