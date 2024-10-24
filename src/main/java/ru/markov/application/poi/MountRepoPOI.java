package ru.markov.application.poi;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import ru.markov.application.service.ConveyLine;
import ru.markov.application.views.BrigEdit;
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
    public void setAroundBorderCenterAlignment(Cell cell) {
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
    public void setAroundBorderCenterAlignmentTotal(Cell cell) {
        CellStyle style = mountBook.createCellStyle();
        Font bold = mountBook.createFont();
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

        CellStyle adminOtpyskStatusCell = mountBook.createCellStyle();
        adminOtpyskStatusCell.setBorderBottom(BorderStyle.THIN);
        adminOtpyskStatusCell.setBorderLeft(BorderStyle.THIN);
        adminOtpyskStatusCell.setBorderRight(BorderStyle.THIN);
        adminOtpyskStatusCell.setBorderTop(BorderStyle.THIN);

        CellStyle otrabotkaStatusCell = mountBook.createCellStyle();
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


        switch (BrigEdit.mountMap.get(line).get(workerIndex).getWorkerStatusAtDayToRepo(day)) {
            case WORK, PERERABOTKA -> cell.setCellStyle(workStatusCell);
            case HOSPITAL -> cell.setCellStyle(hospitalStatusCell);
            case HOLIDAY -> cell.setCellStyle(holidayStatusCell);
            case ADMINOTP -> cell.setCellStyle(adminOtpyskStatusCell);
            case OTRABOTKA -> cell.setCellStyle(otrabotkaStatusCell);
            default -> cell.setCellStyle(nothingStatusCell);
        }
    }
    public void initSheet(Sheet sheet) {
        int totalSize = BrigEdit.mountMap.get(ConveyLine.COMMON).size()
                + BrigEdit.mountMap.get(ConveyLine.LINE_1).size()
                + BrigEdit.mountMap.get(ConveyLine.LINE_2).size()
                + BrigEdit.mountMap.get(ConveyLine.LINE_3).size()
                + BrigEdit.mountMap.get(ConveyLine.LINE_4).size() + 8;
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
        Sheet mountSheet = mountBook.createSheet("Монтажники");
        initSheet(mountSheet);
        createHeaderGrid(mountSheet);
        repoLineMount();


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

    public void repoLineMount() {
        String sheet = "Монтажники";
        int line1Index = 4;
        int line2Index = line1Index+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+2;
        int line3Index = line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+2;
        int line4Index = line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+2;
        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(4).getCell(0));
        mountBook.getSheet(sheet).getRow(4).getCell(0).setCellValue("Волна 1");
        for (int i = 0; i < BrigEdit.mountMap.get(ConveyLine.LINE_1).size(); i++) {
            mountBook.getSheet(sheet).getRow(i + 5).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(mountBook.getSheet(sheet).getRow(i + 5).getCell(0));
            mountBook.getSheet(sheet).getRow(i + 5).getCell(1).setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_1).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(BrigEdit.mountMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1), ConveyLine.LINE_1);
                    mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1)
                            .setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_1).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    mountBook.getSheet(sheet).getRow(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1)
                            .setCellValue(
                                    mountBook.getSheet(sheet).getRow(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    mountBook.getSheet(sheet).getRow(i + 5).getCell(33).setCellValue(
                            mountBook.getSheet(sheet).getRow(i + 5).getCell(33).getNumericCellValue()
                                    +mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(i + 5).getCell(days + 1), ConveyLine.LINE_1);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1, 4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(0));
        mountBook.getSheet(sheet).getRow(4+ BrigEdit.mountMap.get(ConveyLine.LINE_1).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 1 линии

        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2Index, line2Index, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(line2Index).getCell(0));
        mountBook.getSheet(sheet).getRow(line2Index).getCell(0).setCellValue("Волна 2");
        for (int i = 0; i < BrigEdit.mountMap.get(ConveyLine.LINE_2).size(); i++) {
            mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(0));
            mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(1).setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_2).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(BrigEdit.mountMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1)
                            .setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_2).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    mountBook.getSheet(sheet).getRow(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1)
                            .setCellValue(
                                    mountBook.getSheet(sheet).getRow(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(33).setCellValue(
                            mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(33).getNumericCellValue()
                                    +mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(line2Index+1+i).getCell(days + 1), ConveyLine.LINE_2);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1, line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(0));
        mountBook.getSheet(sheet).getRow(line2Index+ BrigEdit.mountMap.get(ConveyLine.LINE_2).size()+1).getCell(0).setCellValue("Итого в бригаде:");
        //завершен отчёт по 2 линии

        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3Index, line3Index, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(line3Index).getCell(0));
        mountBook.getSheet(sheet).getRow(line3Index).getCell(0).setCellValue("Волна 3");
        for (int i = 0; i < BrigEdit.mountMap.get(ConveyLine.LINE_3).size(); i++) {
            mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(0));
            mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(1).setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_3).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(BrigEdit.mountMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1)
                            .setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_3).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    mountBook.getSheet(sheet).getRow(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1)
                            .setCellValue(
                                    mountBook.getSheet(sheet).getRow(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(33).setCellValue(
                            mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(33).getNumericCellValue()
                                    +mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(line3Index+1+i).getCell(days + 1), ConveyLine.LINE_3);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1, line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(0));
        mountBook.getSheet(sheet).getRow(line3Index+ BrigEdit.mountMap.get(ConveyLine.LINE_3).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 3 линии

        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4Index, line4Index, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(line4Index).getCell(0));
        mountBook.getSheet(sheet).getRow(line4Index).getCell(0).setCellValue("Волна 4");
        for (int i = 0; i < BrigEdit.mountMap.get(ConveyLine.LINE_4).size(); i++) {
            mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(0).setCellValue(i + 1); // порядковый номер сотрудника
            setAroundBorder(mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(0));
            mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(1).setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_4).get(i).getFullName());
            int days = 1;
            while (days <= 31) {
                if (!(BrigEdit.mountMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days) == 0)) {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1)
                            .setCellValue(BrigEdit.mountMap.get(ConveyLine.LINE_4).get(i).getWorkTimeToPOI(days));

                    //добавляем итого за день:
                    mountBook.getSheet(sheet).getRow(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1)
                            .setCellValue(
                                    mountBook.getSheet(sheet).getRow(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1).getNumericCellValue()+1);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    //добавляем общие часы за месяц по работнику
                    mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(33).setCellValue(
                            mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(33).getNumericCellValue()
                                    +mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1).getNumericCellValue());
                    days++;
                } else {
                    setStatusCellColorMount(i, days, mountBook.getSheet(sheet).getRow(line4Index+1+i).getCell(days + 1), ConveyLine.LINE_4);
                    setAroundBorderCenterAlignmentTotal(mountBook.getSheet(sheet).getRow(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(days + 1));
                    days++;
                }
            }
        }
        mountBook.getSheet(sheet).addMergedRegion(new CellRangeAddress(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1, line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1, 0, 1));
        setAroundBorderCenterAlignment(mountBook.getSheet(sheet).getRow(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(0));
        mountBook.getSheet(sheet).getRow(line4Index+ BrigEdit.mountMap.get(ConveyLine.LINE_4).size()+1).getCell(0).setCellValue("Итого в бригаде:");

        //завершен отчёт по 4 линии
    }
}


