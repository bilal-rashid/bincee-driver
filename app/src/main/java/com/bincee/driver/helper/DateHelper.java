package com.bincee.driver.helper;

import com.bincee.driver.api.model.ShiftItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static Date stringToDate(String ss) throws ParseException {

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date date = fmt.parse(ss);
        Date nowDate = new Date();

        date.setYear(nowDate.getYear());
        date.setMonth(nowDate.getMonth());
        date.setDate(nowDate.getDate());

//        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");

        return date;
    }

    public static boolean isMorningShift(ShiftItem shift) throws ParseException {
//        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

//        Date date = fmt.parse(shift.start_time);
//
////        Date nowDate = new Date();
////        date.setYear(nowDate.getYear());
////        date.setMonth(nowDate.getMonth());
////        date.setDate(nowDate.getDate());
//
//
//        if (date.getHours() <= 10) {
//            return true;
//        } else
//            return false;


        if (shift.type.equalsIgnoreCase("Pickup")) {
            return true;
        } else {
            return false;
        }


    }
}
