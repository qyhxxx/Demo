package com.example.demo.helper;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Function {
    public static java.sql.Date getSqlDate() {
        java.sql.Date sqlDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }

    public static java.sql.Date getSqlDate(String dateString) {
        java.sql.Date sqlDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(dateString);
            sqlDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }

    public static void exportExcel(Object[] outfit, Object[][] cellData, Object[] tail, String filename, HttpServletResponse httpServletResponse) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        HSSFRow hssfRow = hssfSheet.createRow(0);
        for (int i = 0; i < outfit.length; i++) {
            hssfRow.createCell(i).setCellValue(outfit[i].toString());
        }
        for (int i = 0; i < cellData.length; i++) {
            hssfRow = hssfSheet.createRow(i + 1);
            for (int j = 0; j < cellData[i].length; j++) {
                hssfRow.createCell(j).setCellValue(cellData[i][j].toString());
            }
        }
        hssfRow = hssfSheet.createRow(cellData.length + 1);
        for (int i = 0; i < tail.length; i++) {
            hssfRow.createCell(i).setCellValue(tail[i].toString());
        }
        try {
            filename += ".xls";
            httpServletResponse.setContentType("application/ms-excel;charset=UTF-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
            OutputStream outputStream = httpServletResponse.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
