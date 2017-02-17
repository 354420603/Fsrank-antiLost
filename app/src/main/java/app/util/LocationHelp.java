package app.util;

import android.content.Context;

/**
 *
 * @author jinbing
 */
public class LocationHelp {

    private static final String KEY_PROVINCE = "location.province";
    private static final String KEY_CITY  = "location.city";
    private static final String KEY_COUNTY  = "location.county";

    private static String sProvince;
    private static String sCity;
    private static String sCounty;

    public static String getProvince(Context context) {
        if (sProvince == null) {
            sProvince = ConfigHelp.getInstance(context).getString(KEY_PROVINCE, "");
        }
        return sProvince;
    }

    public static String getCity(Context context) {
        if (sCity == null) {
            sCity = ConfigHelp.getInstance(context).getString(KEY_CITY, "");
        }
        return sCity;
    }

    public static String getCounty(Context context) {
        if (sCounty == null) {
            sCounty = ConfigHelp.getInstance(context).getString(KEY_COUNTY, "");
        }
        return sCounty;
    }

    public static void saveProvince(Context context, String province) {
        sProvince = province;
        ConfigHelp.getInstance(context).setString(KEY_PROVINCE, sProvince);
    }

    public static void saveCity(Context context, String city) {
        sCity = city;
        ConfigHelp.getInstance(context).setString(KEY_CITY, sCity);
    }

    public static void saveCounty(Context context, String county) {
        sCounty = county;
        ConfigHelp.getInstance(context).setString(KEY_COUNTY, sCounty);
    }
}
