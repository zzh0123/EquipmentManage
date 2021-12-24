package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23 15:47
 */

@Data
public class BaseDeviceBean implements Serializable {

    private String id;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private String sysOrgCode;

    private String deviceCode;

    private String deviceName;

    private String deviceType;

    private String deviceCapacity;

    private String useDate;

    private String testSdate;

    private String leakingDate;

    private String chemicalName;

    private String belongCompany;

    private boolean isSelected;
}
