package com.clathrop.kamcord.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyDate extends Date{
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    public MyDate(Date date){
        super();
        this.setTime(date.getTime());
    }

    public static MyDate parseDate(String date){
        try{
            return new MyDate(df.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MyDate add7Days(){
        Calendar c = Calendar.getInstance();
        c.setTime(this);
        c.add(Calendar.DATE, 7);

        return new MyDate(c.getTime());
    }

    @Override
    public String toString(){
        return df.format(this);
    }


}
