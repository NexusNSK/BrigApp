package ru.markov.application.poi;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.service.ConveyLine;
import ru.markov.application.service.District;
import ru.markov.application.views.GridEdit;
import ru.markov.application.views.Reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MountRepoPOI {
    private final SXSSFWorkbook mountBook = new SXSSFWorkbook();

    public void setAroundBorder(Cell cell) {
        CellStyle style = mountBook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(style);
    }

    public void setAroundBorderCenterAllignment(Cell cell) {
        CellStyle style = mountBook.createCellStyle();
        Font bold = mountBook.createFont();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(bold);
        cell.setCellStyle(style);
    }

    public void setStatusCellColor(int workerIndex, int day, Cell cell) {
        CellStyle workStatusCell = mountBook.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = mountBook.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = mountBook.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = mountBook.createCellStyle();
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

        switch (GridEdit.workerList.get(workerIndex).getWorkerStatusAtDay(day)) {
            case ("Работает") -> cell.setCellStyle(workStatusCell);
            case ("Больничный") -> cell.setCellStyle(hospitalStatusCell);
            case ("Отпуск") -> cell.setCellStyle(holidayStatusCell);
            case ("Не определено") -> cell.setCellStyle(nothingStatusCell);
        }
    }

    public void initSheet(Sheet sheet) {
        int totalSize = GridEdit.mountMap.get(ConveyLine.COMMON).size()
                + GridEdit.mountMap.get(ConveyLine.LINE_1).size()
                + GridEdit.mountMap.get(ConveyLine.LINE_2).size()
                + GridEdit.mountMap.get(ConveyLine.LINE_3).size()
                + GridEdit.mountMap.get(ConveyLine.LINE_4).size() + 8;


        for (int i = 0; i < totalSize + 1 + 3; i++) {
            sheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheet.getRow(i).createCell(j);
                setAroundBorder(sheet.getRow(i).getCell(j));
            }
        }
    }

    public void createHeaderGrid(Sheet sheet) {
        CellStyle cs = mountBook.createCellStyle();
        Font bold = mountBook.createFont();
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
        sheet.getRow(3).getCell(0).setCellValue("Бригада монтажники");
        sheet.getRow(3).getCell(0).setCellStyle(cs);
        setAroundBorderCenterAllignment(sheet.getRow(3).getCell(0));
        setAroundBorderCenterAllignment(sheet.getRow(3).getCell(1));


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
        Sheet mountSheet = mountBook.createSheet("Бригада монтажники");
        initSheet(mountSheet);
        createHeaderGrid(mountSheet);
        repoLine();


    }




    public MountRepoPOI() throws IOException {
        Calendar date = new GregorianCalendar();
        DateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        sdf.format(date.getTime());
        reportList();
        FileOutputStream fos = new FileOutputStream("Template.xlsx");
        mountBook.write(fos);
        fos.close();
        System.out.println("Файл был записан на диск");
    }

    public void repoLine() {
        String sheet = "Бригада монтажники";
        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
        setAroundBorderCenterAllignment(mountBook.getSheet(sheet).getRow(4).getCell(0));
        mountBook.getSheet(sheet).getRow(4).getCell(0).setCellValue("Волна 1");
        for (int i = 0; i < GridEdit.mountMap.get(ConveyLine.LINE_1).size(); i++) {
            mountBook.getSheet(sheet).getRow(i + 5).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(mountBook.getSheet(sheet).getRow(i + 5).getCell(0));
            mountBook.getSheet(sheet).getRow(i + 5).getCell(1).setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_1).get(i).getFullName());
            int days = 1;

            //переписать setStatusCellColor, после переопределения ячейки фэйлится стиль
            while (days <= 31) {
                if (!(GridEdit.mountMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColor(i, days, mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1));
                    mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1)
                            .setCellValue(GridEdit.mountMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days));
                    days++;
                } else {
                    setStatusCellColor(i, days, mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1));
                    days++;
                }

            }
        }
    }
}

