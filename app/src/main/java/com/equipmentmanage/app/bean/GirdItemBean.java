package com.equipmentmanage.app.bean;

/**
 * Author: zzh
 * Date: 2021/08/15
 * Description: 各demo实体
 */
public class GirdItemBean {
    private String name;
    private Class clazz;

    public GirdItemBean(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
