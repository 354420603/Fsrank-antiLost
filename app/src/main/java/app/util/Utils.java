package app.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Utils {

    public static File getFileFromURI(Context context, Uri contentUri) {
        String path = getRealPathFromURI(context, contentUri);
        if (path == null) {
            return new File(contentUri.getPath());
        } else {
            return new File(path);
        }
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, proj,
                    null, null, null);
            if (cursor == null) return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 判断是否为华为手机
     * @return
     */
    public static boolean isHuaWei() {
        String manufacturer = Build.MANUFACTURER;
        manufacturer = manufacturer.toLowerCase();
        return manufacturer.contains("huawei");
    }

	public static String inStreamToString(InputStream inStream, String charset)
			throws IOException {
		String result;
		StringWriter writer = new StringWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream,
				"UTF-8"));
		while ((result = br.readLine()) != null) {
			writer.append(result);
		}
		return writer.toString();
	}

	public static void showToastAtMainUi(final Activity activity, final String msg, final int... duration) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(duration != null && duration.length > 0){
					Toast.makeText(activity, msg, duration[0]).show();
					
				}else {
					Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public static boolean checkImageUrl(String url){
		if(!TextUtils.isEmpty(url)){
			if(url.startsWith("http://") || url.startsWith("https://")){
				return true;	
			}
		}
		
		return false;
	}

	public static String getImageUrl(String urlStr, int dimens){
		if(!checkImageUrl(urlStr))
			return urlStr;
		try {
			URL url = new URL(urlStr);
			String query = url.getQuery();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean isWifiConnected(Context cxt){
	    ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if(cm!=null){  
	        NetworkInfo ni = cm.getActiveNetworkInfo();
	        if(ni.getType() == ConnectivityManager.TYPE_WIFI){
	            /* 
	             * ni.getTypeNmae()可能取值如下 
	             * WIFI，表示WIFI联网 
	             * MOBILE，表示GPRS、EGPRS 
	             * 3G网络没有测试过 
	             * WIFI和(E)GPRS不能共存，如果两个都打开，系统仅支持WIFI 
	             */  
	            return true;  
	        }  
	    }  
	    return false;  
	}  
	
	/**
     * 检查网络是否有效
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static Integer getActiveNetworkType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w("network", "couldn't get connectivity manager");
            return null;
        }

        NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
        if (activeInfo == null) {
            Log.v("network", "network is not available");
            return null;
        }
        return activeInfo.getType();
    }

    /**
     * 获取推荐图片大小
     * 屏幕为xhdpi以上，并且网络不为WIFI，则用服务器默认图片，否则采用ImageView在layout中的大小
     * @param context
     * @param layoutSize ImageView在layout中的大小
     * @return 推荐图片大小，为0则没有推荐值
     */
    public static int getRecommendImageSize(Context context, int layoutSize) {
        Integer networkType = Utils.getActiveNetworkType(context);
        float scale = getImageScale(context);
        //手机网络，2G以上，显示2/3，否则显示1/3
        if (networkType != null && networkType == ConnectivityManager.TYPE_MOBILE) {
//                if (isFastMobileNetwork(context)) {
//                    return layoutSize * 2 / 3;
//                } else {
//                    return layoutSize * 2 / 3;
//                }
            return (int) (layoutSize * scale);
        //WIFI情况下，显示1
        } else if (networkType != null && networkType == ConnectivityManager.TYPE_WIFI) {
            return (int) (layoutSize * scale);
//                return layoutSize;
        } else {
            return 0;
        }
    }

    public static float getImageScale(Context context) {
        final float density = context.getResources().getDisplayMetrics().density;
        if (density <= 1.5f) {
            return 1;
        } else if (density <= 2f) {
            return 4.0f / 5;
        } else {
            return 3.0f / 4;
        }
    }

    /**
     * 获取网络类型名称
     * @param context
     * @return
     */
    public static String getActiveNetworkTypeName(Context context) {
        Integer networkType = Utils.getActiveNetworkType(context);
        if (networkType != null) {
            if (networkType == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager == null)
                    return "unknown";

                switch (telephonyManager.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return "4G";
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        return "unknown";
                    default:
                        return "unknown";
                }
            } else if (networkType == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            } else {
                return "unknown";
            }
        } else {
            return "";
        }
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null)
            return false;

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 打开系统网络设置界面
     * @param context
     */
    public static void startNetworkSettingActivity(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        } else {
            intent.setClassName("com.android.settings",
                    "com.android.settings.WirelessSettings");// android4.0系统找不到此activity。
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 解析配置逗号分隔字符串为字符串数组
     * @param line
     * @return
     */
    public static String[] getConfigColumn(String line) {
        String dot = UUID.randomUUID().toString();
        line = line.replaceAll("\",\"", dot);
        String[] columns = line.split(",");
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (dot.equals(column))
                columns[i] = ",";
        }
        return columns;
    }

    //获取手机状态栏高度
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getNumColumnsImpl(GridView gridView) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            return gridView.getNumColumns();
        try {
            Field field = GridView.class.getDeclaredField("mNumColumns");
            field.setAccessible(true);
            return field.getInt(gridView);
        }catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件夹空间大小bytes
     * @param dir 文件夹
     * @return
     */
    public static long getFolderSize(File dir) {
        long size = 0;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                size += file.length();
            } else {
                size += getFolderSize(file);
            }
        }
        return size;
    }

    /**
     * 将bytes转化为不同单位的可读大小
     * @param bytes
     * @return
     */
    public static String humanReadableByteCount(long bytes) {
        boolean si = false;
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
                + (si ? "" : "");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
