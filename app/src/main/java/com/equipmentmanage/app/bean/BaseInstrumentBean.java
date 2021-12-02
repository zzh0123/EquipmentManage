package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23 15:47
 */

@Data
public class BaseInstrumentBean implements Serializable {

    private String id;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private String sysOrgCode;

    private String instrumentCode;

    private String instrumentName;

    private String approvalDate;

    private int responseTime;

    private String scrapTime;

    private String belongCompany;

    private String eftflag;
}
