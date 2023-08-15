package ru.markov.application.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.service.District;
import ru.markov.application.views.GridEdit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Template {
    SXSSFWorkbook book = new SXSSFWorkbook();
    FileOutputStream fos = new FileOutputStream("Template.xlsx");
    Calendar date = new GregorianCalendar();
    DateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

    public Template() throws IOException {
        sdf.format(date.getTime());

        Sheet techSheet = book.createSheet("Бригада техники");
        Sheet builderSheet = book.createSheet("Бригада сборщики");
        Sheet mountSheet = book.createSheet("Бригада монтажники");

        //инициализация таблицы
        for (int i = 0; i < 50; i++) {
            techSheet.createRow(i);
            builderSheet.createRow(i);
            mountSheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                techSheet.getRow(i).createCell(j);
                setAroundBorder(techSheet.getRow(i).getCell(j));
                builderSheet.getRow(i).createCell(j);
                setAroundBorder(builderSheet.getRow(i).getCell(j));
                mountSheet.getRow(i).createCell(j);
                setAroundBorder(mountSheet.getRow(i).getCell(j));
            }
        }

        createHeaderGrid(techSheet);
        createHeaderGrid(builderSheet);
        createHeaderGrid(mountSheet);
        techList(techSheet);
        builderList(builderSheet);
        mountList(mountSheet);

        book.write(fos);
        fos.close();
        System.out.println("Файл был записан на диск");
    }

    //метод для создания рамки вокруг ячейки
    public void setAroundBorder(Cell cell) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(style);
    }
    public void setStatusCellColor(int workerIndex, int day, Cell cell){
        CellStyle workStatusCell = book.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = book.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = book.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = book.createCellStyle();
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

        switch (GridEdit.workerList.get(workerIndex).getWorkerStatusAtDay(day)){
            case ("Работает") -> cell.setCellStyle(workStatusCell);
            case ("Больничный") -> cell.setCellStyle(hospitalStatusCell);
            case ("Отпуск") -> cell.setCellStyle(holidayStatusCell);
            case ("Не определено") -> cell.setCellStyle(nothingStatusCell);
        }
    }

    //метод  для создания шапки таблицы
    public void createHeaderGrid(Sheet sheet) {
        CellStyle cs = book.createCellStyle();
        Font bold = book.createFont();
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
        for (int i = 2, j = 1; i <= 32; i++, j++) {
            sheet.getRow(2).getCell(i).setCellValue(j);
            sheet.getRow(2).getCell(i).setCellStyle(cs);
            sheet.setColumnWidth(i, 1000);
        }
        sheet.setColumnWidth(0, 1000);
    }

    public String getMonth() {
        return switch (date.get(Calendar.MONTH)) {
            case (0) -> "Январь";
            case (1) -> "Февраль";
            case (2) -> "Март";
            case (3) -> "Апрель";
            case (4) -> "Май";
            case (5) -> "Июнь";
            case (6) -> "Июль";
            case (7) -> "Август";
            case (8) -> "Сентябрь";
            case (9) -> "Октябрь";
            case (10) -> "Ноябрь";
            case (11) -> "Декабрь";
            default -> "";
        };
    }

    public void techList(Sheet sheet) {
        int workerCount = 1;
        for (int i = 0; i < GridEdit.workerList.size(); i++) {
            int days = 1;
            if (GridEdit.workerList.get(i).getDistrict().equals(District.TECH)) {
                sheet.getRow(workerCount + 2)
                        .getCell(0)
                        .setCellValue(workerCount);
                sheet.getRow(workerCount + 2)
                        .getCell(1)
                        .setCellValue(GridEdit.workerList
                                .get(i).getFullName());
                while (days <= 31) {
                    if (!(GridEdit.workerList.get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColor(i, days, sheet.getRow(workerCount + 2).getCell(days + 1));
                        sheet.getRow(workerCount + 2).getCell(days + 1)
                                .setCellValue(GridEdit.workerList.get(i).getWorkTimeToPOI(days));
                        days++;
                    }else{
                        setStatusCellColor(i, days, sheet.getRow(workerCount + 2).getCell(days + 1));
                        days++;
                    }
                }
                workerCount++;
            }
        }
    }
    public void builderList(Sheet sheet) {
        int workerCount = 1;
        for (int i = 0; i < GridEdit.workerList.size(); i++) {
            int days = 1;
            if (GridEdit.workerList.get(i).getDistrict().equals(District.BUILDING)) {
                sheet.getRow(workerCount + 2)
                        .getCell(0)
                        .setCellValue(workerCount);
                sheet.getRow(workerCount + 2)
                        .getCell(1)
                        .setCellValue(GridEdit.workerList
                                .get(i).getFullName());
                while (days <= 31) {
                    if (!(GridEdit.workerList.get(i).getWorkTimeToPOI(days) == 0)) {
                        setStatusCellColor(i, days, sheet.getRow(workerCount + 2).getCell(days + 1));
                        sheet.getRow(workerCount + 2).getCell(days + 1)
                                .setCellValue(GridEdit.workerList.get(i).getWorkTimeToPOI(days));
                        days++;
                    }else{
                        setStatusCellColor(i, days, sheet.getRow(workerCount + 2).getCell(days + 1));
                        days++;
                    }
                }
                workerCount++;
            }
        }
    }
    public void mountList(Sheet sheet) {
        int workerCount = 1;
        for (int i = 0; i < GridEdit.workerList.size(); i++) {
            int days = 1;
            if (GridEdit.workerList.get(i).getDistrict().equals(District.MOUNTING)) {
                sheet.getRow(workerCount + 2)
                        .getCell(0)
                        .setCellValue(workerCount);
                sheet.getRow(workerCount + 2)
                        .getCell(1)
                        .setCellValue(GridEdit.workerList
                                .get(i).getFullName());
                while (days <= 31) {
                    if (!(GridEdit.workerList.get(i).getWorkTimeToPOI(days) == 0)) {
                        setStatusCellColor(i, days, sheet.getRow(workerCount + 2).getCell(days + 1));
                        sheet.getRow(workerCount + 2).getCell(days + 1)
                                .setCellValue(GridEdit.workerList.get(i).getWorkTimeToPOI(days));
                        days++;
                    }else{
                        setStatusCellColor(i, days, sheet.getRow(workerCount + 2).getCell(days + 1));
                        days++;
                    }
                }
                workerCount++;
            }
        }
    }
}

