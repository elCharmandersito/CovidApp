package com.example.demo;

import java.util.Calendar;

public class Regiones {

    private static Calendar calendar = Calendar.getInstance();

    private static String day = Integer.toString(calendar.get(Calendar.DATE)-1);
    private static String month = Integer.toString(calendar.get(Calendar.MONTH) +1);
    private static String year = Integer.toString(calendar.get(Calendar.YEAR));

    //JSON URL
    public static final String URL = "https://api.covid19api.com/live/country/chile/status/confirmed/date/"+year+"-"+month+"-"+day+"T00:00:00Z";

    //Tags used in the JSON String
    public static final String PROVINCE_NAME = "Province";
    public static final String CONFIRMED = "Confirmed";
    public static final String DEATHS = "Deaths";
    public static final String RECOVERED = "Recovered";
    public static final String ACTIVE = "Active";
}
