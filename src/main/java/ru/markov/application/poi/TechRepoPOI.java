package ru.markov.application.poi;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.views.BrigEdit;
import ru.markov.application.views.Reports;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TechRepoPOI {
    private final SXSSFWorkbook techBook = new SXSSFWorkbook();
    public void setAroundBorder(Cell cell) {
        CellStyle style = techBook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        cell.setCellStyle(style);
    }
    public void setAroundBorderCenterAlignment(Cell cell) {
        CellStyle style = techBook.createCellStyle();
        Font bold = techBook.createFont();
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
        CellStyle style = techBook.createCellStyle();
        Font bold = techBook.createFont();
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
    public void setStatusCellColorTech(int workerIndex, int day, Cell cell) {
        CellStyle workStatusCell = techBook.createCellStyle();
        workStatusCell.setBorderBottom(BorderStyle.THIN);
        workStatusCell.setBorderLeft(BorderStyle.THIN);
        workStatusCell.setBorderRight(BorderStyle.THIN);
        workStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle holidayStatusCell = techBook.createCellStyle();
        holidayStatusCell.setBorderBottom(BorderStyle.THIN);
        holidayStatusCell.setBorderLeft(BorderStyle.THIN);
        holidayStatusCell.setBorderRight(BorderStyle.THIN);
        holidayStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle hospitalStatusCell = techBook.createCellStyle();
        hospitalStatusCell.setBorderBottom(BorderStyle.THIN);
        hospitalStatusCell.setBorderLeft(BorderStyle.THIN);
        hospitalStatusCell.setBorderRight(BorderStyle.THIN);
        hospitalStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle nothingStatusCell = techBook.createCellStyle();
        nothingStatusCell.setBorderBottom(BorderStyle.THIN);
        nothingStatusCell.setBorderLeft(BorderStyle.THIN);
        nothingStatusCell.setBorderRight(BorderStyle.THIN);
        nothingStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle adminOtpyskStatusCell = techBook.createCellStyle();
        adminOtpyskStatusCell.setBorderBottom(BorderStyle.THIN);
        adminOtpyskStatusCell.setBorderLeft(BorderStyle.THIN);
        adminOtpyskStatusCell.setBorderRight(BorderStyle.THIN);
        adminOtpyskStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle otrabotkaStatusCell = techBook.createCellStyle();
        otrabotkaStatusCell.setBorderBottom(BorderStyle.THIN);
        otrabotkaStatusCell.setBorderLeft(BorderStyle.THIN);
        otrabotkaStatusCell.setBorderRight(BorderStyle.THIN);
        otrabotkaStatusCell.setBorderTop(BorderStyle.THIN);

        otrabotkaStatusCell.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        otrabotkaStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        otrabotkaStatusCell.setVerticalAlignment(VerticalAlignment.CENTER);
        otrabotkaStatusCell.setAlignment(HorizontalAlignment.CENTER);

        adminOtpyskStatusCell.setFillForegroundColor(IndexedColors.PLUM.getIndex());
        adminOtpyskStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        adminOtpyskStatusCell.setVerticalAlignment(VerticalAlignment.CENTER);
        adminOtpyskStatusCell.setAlignment(HorizontalAlignment.CENTER);

        workStatusCell.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        workStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        workStatusCell.setVerticalAlignment(VerticalAlignment.CENTER);
        workStatusCell.setAlignment(HorizontalAlignment.CENTER);

        holidayStatusCell.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        holidayStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        holidayStatusCell.setVerticalAlignment(VerticalAlignment.CENTER);
        holidayStatusCell.setAlignment(HorizontalAlignment.CENTER);

        hospitalStatusCell.setFillForegroundColor(IndexedColors.RED.getIndex());
        hospitalStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        hospitalStatusCell.setVerticalAlignment(VerticalAlignment.CENTER);
        hospitalStatusCell.setAlignment(HorizontalAlignment.CENTER);

        nothingStatusCell.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        nothingStatusCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        nothingStatusCell.setVerticalAlignment(VerticalAlignment.CENTER);
        nothingStatusCell.setAlignment(HorizontalAlignment.CENTER);

        switch (BrigEdit.techListUPC.get(workerIndex).getWorkerStatusAtDayToRepo(day)) {
            case WORK, PERERABOTKA -> cell.setCellStyle(workStatusCell);
            case HOSPITAL -> cell.setCellStyle(hospitalStatusCell);
            case HOLIDAY -> cell.setCellStyle(holidayStatusCell);
            case ADMINOTP -> cell.setCellStyle(adminOtpyskStatusCell);
            case OTRABOTKA -> cell.setCellStyle(otrabotkaStatusCell);
            default -> cell.setCellStyle(nothingStatusCell);
        }
    }
    public void initSheetTech(Sheet sheet) {
        int totalSize = BrigEdit.techListUPC.size() + 1;
        for (int i = 0; i < totalSize + 1 + 3; i++) {
            sheet.createRow(i);
            for (int j = 0; j <= 33; j++) {
                sheet.getRow(i).createCell(j);
                setAroundBorder(sheet.getRow(i).getCell(j));
            }
        }
    }
    public void createHeaderGrid(Sheet sheet) {
        CellStyle cs = techBook.createCellStyle();
        Font bold = techBook.createFont();
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
        sheet.getRow(3).getCell(0).setCellValue("Техники");
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
        Sheet techSheet = techBook.createSheet("Техники");
        initSheetTech(techSheet);
        createHeaderGrid(techSheet);
        repoLineTech();


    }
    public TechRepoPOI() throws IOException {
        Calendar date = new GregorianCalendar();
        DateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        sdf.format(date.getTime());
        reportList();
        FileOutputStream fos = new FileOutputStream("Template.xlsx");
        techBook.write(fos);
        fos.close();
        System.out.println("Файл был записан на диск");
    }

    public void repoLineTech() {
        String sheet = "Техники";
        int line1Index = 4;
        for (int i = 0; i < BrigEdit.techListUPC.size(); i++) {
            techBook.getSheet(sheet).getRow(i + line1Index).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(techBook.getSheet(sheet).getRow(i + line1Index).getCell(0));
            techBook.getSheet(sheet).getRow(i + line1Index).getCell(1).setCellValue(BrigEdit.techListUPC.get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(BrigEdit.techListUPC.get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorTech(i, days, techBook.getSheet(sheet).getRow(i + line1Index).getCell(days + 1));
                    techBook.getSheet(sheet).getRow(i + line1Index).getCell(days + 1)
                            .setCellValue(BrigEdit.techListUPC.get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    techBook.getSheet(sheet).getRow(line1Index+ BrigEdit.techListUPC.size()).getCell(days + 1)
                            .setCellValue(
                                    techBook.getSheet(sheet).getRow(line1Index+ BrigEdit.techListUPC.size()).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(techBook.getSheet(sheet).getRow(line1Index+ BrigEdit.techListUPC.size()).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    techBook.getSheet(sheet).getRow(i + line1Index).getCell(33).setCellValue(
                            techBook.getSheet(sheet).getRow(i + line1Index).getCell(33).getNumericCellValue()
                                    + techBook.getSheet(sheet).getRow(i + line1Index).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorTech(i, days, techBook.getSheet(sheet).getRow(i + line1Index).getCell(days + 1));
                    setAroundBorderCenterAlignmentTotal(techBook.getSheet(sheet).getRow(line1Index+ BrigEdit.techListUPC.size()).getCell(days + 1));
                    days++;
                }
            }
        }
        techBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line1Index+ BrigEdit.techListUPC.size(), line1Index+ BrigEdit.techListUPC.size(), 0, 1));
        setAroundBorderCenterAlignment(techBook.getSheet(sheet).getRow(line1Index+ BrigEdit.techListUPC.size()).getCell(0));
        techBook.getSheet(sheet).getRow(line1Index+ BrigEdit.techListUPC.size()).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт

    }

}





