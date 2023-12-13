package ru.markov.application.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.service.District;
import ru.markov.application.views.GridEdit;
import ru.markov.application.views.Reports;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Template {
    private final SXSSFWorkbook book = new SXSSFWorkbook();

    public Template(String list) throws IOException {
        Calendar date = new GregorianCalendar();
        DateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        sdf.format(date.getTime());
        reportList(list);
        FileOutputStream fos = new FileOutputStream("Template.xlsx");
        book.write(fos);
        fos.close();
        System.out.println("Файл был записан на диск");
    }

    public void setAroundBorder(Cell cell) {
        CellStyle style = book.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(style);
    }
    public void initSheet(Sheet sheet) {
        for (int i = 0; i < 50; i++) {
            sheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheet.getRow(i).createCell(j);
                setAroundBorder(sheet.getRow(i).getCell(j));
            }
        }
    }

    public void setStatusCellColor(int workerIndex, int day, Cell cell) {
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

        switch (GridEdit.workerList.get(workerIndex).getWorkerStatusAtDay(day)) {
            case ("Работает") -> cell.setCellStyle(workStatusCell);
            case ("Больничный") -> cell.setCellStyle(hospitalStatusCell);
            case ("Отпуск") -> cell.setCellStyle(holidayStatusCell);
            case ("Не определено") -> cell.setCellStyle(nothingStatusCell);
        }
    }

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

    public void reportList(String sheet) {
        Sheet curentSheet = null;
        District district = null;
        switch (sheet){
            case "Бригада монтажники" -> {
                Sheet mountSheet = book.createSheet("Бригада монтажники");
                initSheet(mountSheet);
                createHeaderGrid(mountSheet);
                curentSheet = mountSheet;
                district = District.MOUNTING;
            }
            case "Бригада сборщики" -> {
                Sheet builderSheet = book.createSheet("Бригада сборщики");
                initSheet(builderSheet);
                createHeaderGrid(builderSheet);
                curentSheet = builderSheet;
                district = District.BUILDING;
            }
            case "Бригада техники"-> {
                Sheet techSheet = book.createSheet("Бригада техники");
                curentSheet = techSheet;
                initSheet(techSheet);
                createHeaderGrid(techSheet);
                district = District.TECH;
            }
            case "Все бригады" -> {
                Sheet techSheet = book.createSheet("Бригада техники");
                initSheet(techSheet);
                createHeaderGrid(techSheet);
                Sheet builderSheet = book.createSheet("Бригада сборщики");
                initSheet(builderSheet);
                createHeaderGrid(builderSheet);
                Sheet mountSheet = book.createSheet("Бригада монтажники");
                initSheet(mountSheet);
                createHeaderGrid(mountSheet);
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < GridEdit.workerList.size(); j++){
                        switch (j) {
                            case 0 -> {
                                curentSheet = techSheet;
                                district = District.TECH;
                            }
                            case 1 -> {
                                curentSheet = builderSheet;
                                district = District.BUILDING;
                            }
                            case 2 -> {
                                curentSheet = mountSheet;
                                district = District.MOUNTING;
                            }
                        }
                        int techCount = 1;
                        for (int iter = 0; iter < GridEdit.workerList.size(); iter++) {
                            int days = 1;
                            if (GridEdit.workerList.get(iter).getDistrict().equals(district)) {
                                curentSheet.getRow(techCount + 2)
                                        .getCell(0)
                                        .setCellValue(techCount);
                                curentSheet.getRow(techCount + 2)
                                        .getCell(1)
                                        .setCellValue(GridEdit.workerList
                                                .get(iter).getFullName());
                                while (days <= 31) {
                                    if (!(GridEdit.workerList.get(iter).getWorkTimeToPOI(days) == 0)) {
                                        setStatusCellColor(iter, days, curentSheet.getRow(techCount + 2).getCell(days + 1));
                                        curentSheet.getRow(techCount + 2).getCell(days + 1)
                                                .setCellValue(GridEdit.workerList.get(iter).getWorkTimeToPOI(days));
                                        days++;
                                    } else {
                                        setStatusCellColor(iter, days, curentSheet.getRow(techCount + 2).getCell(days + 1));
                                        days++;
                                    }
                                }
                                techCount++;
                            }
                        }
                    }
                }
                createHeaderGrid(mountSheet);
            }
            case "Пустой шаблон" -> {
                Sheet temp = book.createSheet("Шаблон");
                initSheet(temp);
                createHeaderGrid(temp);
            }
        }

        int techCount = 1;
        for (int i = 0; i < GridEdit.workerList.size(); i++) {
            int days = 1;
            if (GridEdit.workerList.get(i).getDistrict().equals(district)) {
                curentSheet.getRow(techCount + 2)
                        .getCell(0)
                        .setCellValue(techCount);
                curentSheet.getRow(techCount + 2)
                        .getCell(1)
                        .setCellValue(GridEdit.workerList
                                .get(i).getFullName());
                while (days <= 31) {
                    if (!(GridEdit.workerList.get(i).getWorkTimeToPOI(days) == 0)) {
                        setStatusCellColor(i, days, curentSheet.getRow(techCount + 2).getCell(days + 1));
                        curentSheet.getRow(techCount + 2).getCell(days + 1)
                                .setCellValue(GridEdit.workerList.get(i).getWorkTimeToPOI(days));
                        days++;
                    } else {
                        setStatusCellColor(i, days, curentSheet.getRow(techCount + 2).getCell(days + 1));
                        days++;
                    }
                }
                techCount++;
            }
        }

    }
}

