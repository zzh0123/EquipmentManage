package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/15
 * Description: 产品流列表实体
 */
@Data
public class ProductFlowResultBean implements Serializable {
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

        private String eftflag;

        private String updateTime;

        private String belongCompany;

        private String deviceId;

        private String mediumState;

        private String createBy;

        private String mediumState_dictText;

        private String createTime;

        private String updateBy;

        private String prodStreamCode;

        private String prodStreamName;

        private String sysOrgCode;

        private String id;

        private String deviceId_dictText;

    }

}
