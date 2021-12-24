package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description: 所属公司
 * @Author: zzh
 * @CreateDate: 2021/12/4
 */
@Data
public class BaseCompanyResultBean implements Serializable {
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
        private String companyProperty_dictText;

        private int standardGas;

        private int standardHeavy;

        private String orgCode_dictText;

        private String companyName;

        private String updateTime;

        private String companyShortName;

        private String companyProperty;

        private String createBy;

        private String createTime;

        private String updateBy;

        private String orgCode;

        private String sysOrgCode;

        private String id;

    }

}
