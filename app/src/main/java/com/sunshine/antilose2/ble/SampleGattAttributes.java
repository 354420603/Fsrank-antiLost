package com.sunshine.antilose2.ble;

import java.util.HashMap;

public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String HEART_RATE_MEASUREMENT = "0000C004-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String BATTERY_LEVEL_READ = "00002a19-0000-1000-8000-00805f9b34fb";
    public static String TX_POWER_LEVEL_READ = "00002a07-0000-1000-8000-00805f9b34fb";
    public static String LOSS_LEVEL_READ = "0000ffe1-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "BATTERY SERVICE");
        attributes.put("00001804-0000-1000-8000-00805f9b34fb", "TX POWER");
        attributes.put("00001802-0000-1000-8000-00805f9b34fb", "IMMEDIATE ALERT");
        attributes.put("00001803-0000-1000-8000-00805f9b34fb", "LINK LOSS");
        attributes.put("0000ffe0-0000-1000-8000-00805f9b34fb", "SERVICE");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }
    
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}

 