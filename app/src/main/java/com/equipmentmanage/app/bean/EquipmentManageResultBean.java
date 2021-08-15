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
public class EquipmentManageResultBean implements Serializable {
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
        private String equipmentCode;

        private String areaId_dictText;

        private String eftflag;

        private String updateTime;

        private String belongCompany;

        private String deviceId;

        private String createBy;

        private String areaId;

        private String createTime;

        private String updateBy;

        private String sysOrgCode;

        private String equipmentName;

        private String id;

        private String belongCompany_dictText;

        private String deviceId_dictText;

    }

}
