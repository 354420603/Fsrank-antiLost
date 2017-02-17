package app.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author jinbing
 */
public class DeviceUtils {

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();
    }
}
