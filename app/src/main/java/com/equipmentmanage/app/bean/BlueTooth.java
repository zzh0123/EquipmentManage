package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ZengZeHong on 2017/5/10.
 */

@Data
public class BlueTooth implements Serializable {
    public static final int TAG_NORMAL = 0;
    public static final int TAG_TOAST = 1;
    private int tag;
    private String name;
    private String mac;
    private String rssi;
    public BlueTooth(String name , int tag) {
        this.tag = tag;
        this.name = name;
    }
    public BlueTooth(String name, String mac, String rssi) {
        tag = TAG_NORMAL;
        this.name = name;
        this.mac = mac;
        this.rssi = rssi;
    }

}
