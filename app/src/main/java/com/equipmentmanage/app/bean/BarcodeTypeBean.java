package com.equipmentmanage.app.bean;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/5
 */
public class BarcodeTypeBean {

    private String type;
    private String name;

    public BarcodeTypeBean(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
