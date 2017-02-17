package app.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * 获取MAC地址
 * @author jinbing
 *
 */
public class MacHelper {
	
	private static String MAC_ADDRESS;
	
	/**
	 * 获取Android设备的MAC地址
	 * 优先通过WifiManager读取，否则然后读取文件的方式读取wlan0，否则然后读取文件的方式读取eth0
	 * @param context
	 * @return
	 */
	public synchronized static String getMacAddress(Context context) {
		if (MAC_ADDRESS == null) {
			String mac = getWifiMac(context);
			if (TextUtils.isEmpty(mac)) {
				mac = getWifiMac();
			}
			if (TextUtils.isEmpty(mac)) {
				mac = getEthMac();
			}
			
			if (!TextUtils.isEmpty(mac)) {
				MAC_ADDRESS = mac;
			}
		}
		
		return TextUtils.isEmpty(MAC_ADDRESS) ? "" : MAC_ADDRESS;
	}
	
	/**
	 * 通过WifiManager获取Mac地址
	 * @param context
	 * @return
	 */
    private static String getWifiMac(Context context) {
        String macAddress = "";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
            	macAddress = info.getMacAddress();
            }
        } catch (Exception e) { }
        
        return macAddress;
    }

    /**
	 * 通过读取系统文件的方式获取wlan0的MAC地址
	 * @return
	 */
    private static String getWifiMac() {
    	return getMac("/sys/class/net/wlan0/address");
	}
    
    /**
     * 通过读取系统文件的方式获取eth0的MAC地址
     * @return
     */
    private static String getEthMac() {
    	return getMac("/sys/class/net/eth0/address");
	}
    
    /**
     * 通过读取某一系统路径文件的方式获取MAC地址
     * @param path
     * @return
     */
    private static String getMac(String path) {
    	String macSerial = "";
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat " + path);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
                // 赋予默认值
                ex.printStackTrace();
        }
        return macSerial;
    }

}
