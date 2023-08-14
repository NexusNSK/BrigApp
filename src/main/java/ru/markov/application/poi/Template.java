package ru.markov.application.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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

        Sheet techSheet = book.createSheet("Бригаад техники");
        Sheet builderSheet = book.createSheet("Бригада сборщики");
        Sheet mountSheet = book.createSheet("Бригада монтажники");

        //инициализация таблицы
        for (int i = 0; i < 100; i++) {
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
        techList(techSheet);

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
        sheet.setColumnWidth(1,10000);
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
    public void techList(Sheet sheet){
        for (int i = 0; i < GridEdit.workerList.size(); i++) {
            int days = 0;
           //if (GridEdit.workerList.get(i).getDistrict().equals(District.TECH)){
            sheet.getRow(i+3)
                    .getCell(0)
                    .setCellValue(i+1);
                sheet.getRow(i+3)
                     .getCell(1)
                     .setCellValue(GridEdit.workerList
                     .get(i).getFullName());
               while (days < 31){
                   if (!(GridEdit.workerList.get(i).getWorkTimeToPOI(days)==0)){
                       /*switch (GridEdit.workerList.get(i).getWorkerStatus()){
                           case "Больничный":
                               sheet.getRow(i + 3).getCell(days + 1)
                                       .s
*/
                       }
                       sheet.getRow(i + 3).getCell(days + 1)
                               .setCellValue(GridEdit.workerList.get(i).getWorkTimeToPOI(days));
                   }
                   days++;
                }
            }
        }
   // }
//}

