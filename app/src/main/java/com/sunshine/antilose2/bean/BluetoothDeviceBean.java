package com.sunshine.antilose2.bean;

/**
 *
 * Created by huangrengxingon 2017/2/16.
 *
 */

public class BluetoothDeviceBean {
    private String name;
    private String address;
    private String rssi;
    private Boolean state = false;

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public BluetoothDeviceBean(String name, String address, String rssi) {
        this.name = name;
        this.address = address;
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
