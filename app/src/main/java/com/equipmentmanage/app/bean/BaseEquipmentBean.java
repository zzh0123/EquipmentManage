package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23 15:47
 */

@Data
public class BaseEquipmentBean implements Serializable {

    private String id;

    private String deviceId;

    private String deviceName;

    private String areaId;

    private String areaName;

    private String equipmentCode;

    private String equipmentName;

    private String belongCompany;

    private String eftflag;
}
