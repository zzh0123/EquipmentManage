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
public class BaseAreaBean implements Serializable {

    private String id;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private String sysOrgCode;

    private String areaCode;

    private String areaName;

    private String belongDevice;

    private String deviceName;

    private String belongCompany;

    private String eftflag;

    private List<BaseAreaPidList> baseAreaPidList;

    @Data
    public static class BaseAreaPidList implements Serializable{
        private String id;

        private String createBy;

        private String createTime;

        private String updateBy;

        private String updateTime;

        private String sysOrgCode;

        private String areaId;

        private String productCode;

        private String pid;
    }
}
