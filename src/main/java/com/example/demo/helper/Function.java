package com.example.demo.helper;

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
}
