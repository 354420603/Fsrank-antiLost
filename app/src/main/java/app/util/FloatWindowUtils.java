package app.util;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * 悬浮窗工具类
 * @author jinbing
 */
public class FloatWindowUtils {

    /**
     * 判断是否打开了悬浮窗权限
     * @param context
     * @return
     */
    public static boolean isFloatWindowOpAllowed(Context context) {
        String miuiVersion = getMiuiVersion();
        if (!miuiVersion.isEmpty()) {
            return isMiuiFloatWindowOpAllowed(context);
        }

        return true;
    }

    public static String getMiuiVersion() {
        String line = null;
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name" );
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = reader.readLine();
            return line;
        } catch (IOException e) {
        } finally {
        }

        return "";
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isMiuiFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            boolean result = checkOp(context, 24);
            return result;
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            Method method = manager.getClass().getDeclaredMethod(
                    "checkOp", int.class, int.class, String.class);
            return AppOpsManager.MODE_ALLOWED ==
                    (Integer) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
        } catch (Exception e) { }

        return false;
    }

}
