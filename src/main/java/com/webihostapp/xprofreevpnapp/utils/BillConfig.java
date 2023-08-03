package com.webihostapp.xprofreevpnapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class BillConfig {
    private static final String PREF_NAME = "snow-intro-slider";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    public static final String INAPPSKUUNIT = "inappskuunit";
    public static final String PURCHASETIME = "purchasetime";
    public static final String PRIMIUM_STATE = "primium_state";//boolean

    public static final String COUNTRY_DATA = "Country_data";
    public static final String BUNDLE = "Bundle";
    public static final String SELECTED_COUNTRY = "selected_country";

    public static final String IN_PURCHASE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl5uXMT/9fIpE/SZvg2/5TeAgwtaCoUhyfZTff2UZqJaUN48fhr6Rev71Jyptw8ZswyOmR+E7y6JBxpxkOgw9BSUYaYmLwzJfcVaLBlU8dceFNGIEuToICMrJdRFCKjs2EfvFHjzg6uRFUUB4K+XikYlQE3plSAxxMpuuAwFaUYTqBB/ZOQVQDZF73kA7xaksePDex+yILc8+Sm5C/tfwVxx1aj1ISZmHj2S1ZYI4zHPMquO3hfxNxYMW+2VOzBrGu2ZbUnz8v1rPmHqMOQhV7mMLKTZRi53BsZ9OgfN+ObSnNw4rrGY3iOEUtaelHax0c1Dz2diyqO61luFKaoOcuwIDAQAB";
    public static final String One_Month_Sub = "oll_feature_for_onemonth";
    public static final String Six_Month_Sub = "oll_feature_for_sixmonth";
    public static final String One_Year_Sub = "oll_feature_for_year";


    public BillConfig(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

}