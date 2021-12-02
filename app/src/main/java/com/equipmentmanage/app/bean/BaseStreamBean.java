package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23 15:47
 */

@Data
public class BaseStreamBean implements Serializable {

    private String id;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private String sysOrgCode;

    private String prodStreamCode;

    private String prodStreamName;

    private String deviceId;

    private String deviceName;

    private String belongCompany;

    private String mediumState;

    private String mediumStateName;

    private String eftflag;

}
