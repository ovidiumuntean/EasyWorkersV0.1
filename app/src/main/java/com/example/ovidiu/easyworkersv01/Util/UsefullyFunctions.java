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
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
   /*you can also use DateFormat reference instead of SimpleDateFormat
    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
    */
        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }

    public Date convertStringToDate(String dateString){
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");

        try {
            Date date = sdfr.parse(dateString);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

//    public static boolean isDateValid(Date date){
//        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMM/yyyy");
//    }
}
