package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/14 14:41
 * Description: 装备管理列表实体
 */
@Data
public class DeviceManageResultBean implements Serializable {
    private List<Records> records;

    private int total;

    private int size;

    private int current;

    private List<String> orders;

    private boolean optimizeCountSql;

    private boolean hitCount;

    private String countId;

    private String maxLimit;

    private boolean searchCount;

    private int pages;

    @Data
    public static class Records implements Serializable{
        private String deviceType;

        private String chemicalName;

        private String deviceType_dictText;

        private int deviceCapacity;

        private String updateTime;

        private String deviceCode;

        private String belongCompany;

        private String deviceName;

        private String createBy;

        private String useDate;

        private String createTime;

        private String updateBy;

        private String testSdate;

        private String chemicalName_dictText;

        private String sysOrgCode;

        private String id;

        private String leakingDate;

    }

}
