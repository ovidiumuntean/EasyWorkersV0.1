package com.example.ovidiu.easyworkersv01.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ovidiu on 05/12/2017.
 */

public class UsefullyFunctions {

    public UsefullyFunctions() {
    }

    // Converts the date to dd/mm/yyyy
    public String convertDateToString(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
   /*you can also use DateFormat reference instead of SimpleDateFormat
    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
    */
        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            return null;
        }
        return dateString;
    }

    public Date convertStringToDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = sdf.parse(dateString);
            if (!dateString.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return null;
        } else {
            return date;
        }
    }

//    public static boolean isDateValid(Date date){
//        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
//    }
}
