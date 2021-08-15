package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/15
 * Description: 区域管理列表实体
 */
@Data
public class AreaManageResultBean implements Serializable {
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
        private String createBy;

        private String areaCode;

        private String belongDevice;

        private String createTime;

        private String updateBy;

        private String areaName;

        private String sysOrgCode;

        private String eftflag;

        private String updateTime;

        private String id;

        private String belongCompany;

        private String belongDevice_dictText;

    }

}
