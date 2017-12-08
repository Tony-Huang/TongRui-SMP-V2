package com.hdp.smp.front.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class FormatUtil {
    public FormatUtil() {
        super();
    }

    public static Date formatDate(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String startDateStr = "2015-06-13 14:59:43"; // '2015-06-13 14:59:43'   '2015-06-13 14:42:42'
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }
    
    public static Date formatDatePart(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // String startDateStr = "2015-06-13 14:59:43"; // '2015-06-13 14:59:43'   '2015-06-13 14:42:42'
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }
    
    public static Date formatDateHour(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // String startDateStr = "2015-06-13 14:59:43"; // '2015-06-13 14:59:43'   '2015-06-13 14:42:42'
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String formateDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        if (date==null) {
            date = new Date();
            }
        
        String dateStr = "";
       
        dateStr = df.format(date);
    
        return dateStr;
    }
    
    public static String formateDatePart(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        if (date==null) {
            date = new Date();
            }
        
        String dateStr = "";
       
        dateStr = df.format(date);
    
        return dateStr;
    }
    
    public static String formatDateHour(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
        if (date==null) {
            date = new Date();
            }
        String dateStr = "";
        dateStr = df.format(date);
        return dateStr;
    }
    
    //====
    public static Date formatTime(String timeStr) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = df.parse(timeStr);
            } catch (ParseException e) {
            }
            return date;
        }
    
    public static String formatTime(Date time) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        
        if (time==null) {
            time = new Date();
            }
        
        String dateStr = "";
       
        dateStr = df.format(time);
    
        return dateStr;
    }
    
    
    public static void main (String[]  args) {
            Date time =formatTime("10:28:12");
            System.out.println(time);
            
            String timestr =formatTime(new Date());
            System.out.println(timestr);
        }
    
}
