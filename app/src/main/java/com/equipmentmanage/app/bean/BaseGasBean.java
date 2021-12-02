package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23 15:47
 */

@Data
public class BaseGasBean implements Serializable {

    private String id;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private String sysOrgCode;

    private String gasCode;

    private String gasName;

    private String calibType;

    private String ppm;

    private String calibThreshold;

    private String eftDate;

    private String blockUp;

    private String blockupDate;

    private String belongCompany;

    private String eftflag;


    private String date;
    private String responseTime;
    private String actualValue;
    private String status;
}
