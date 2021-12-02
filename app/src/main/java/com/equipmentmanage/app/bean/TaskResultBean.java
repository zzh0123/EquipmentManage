package com.equipmentmanage.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/16
 * Description: 任务列表实体
 */
@Data
public class TaskResultBean implements Serializable {

    static final long serialVersionUID = 42L;

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
    public static class Records implements Serializable {
        static final long serialVersionUID = 42L;

        private String userTaskCompCount;

        private String taskSdate;

        private String uploadTime;

        private String detectionYear;

        private String belongCompany;

        private List<LiveTaskAppPicPageList> liveTaskAppPicPageList;

        private String tagInfo;

        private String userDetectionCount;

        private String taskName;

        private String id;

        private String taskEdate;

        private String detectionUser;

        private String taskId;

        private String taskNum;

        private String leakageCount;
    }

    @Data
    public static class LiveTaskAppPicPageList implements Serializable {
        static final long serialVersionUID = 42L;
        private List<LiveTaskAppAssignedPageList> liveTaskAppAssignedPageList;

        private String tagNum;

        private String pictPath;

        private String taskId;
    }

    @Data
    public static class LiveTaskAppAssignedPageList implements Serializable {
        static final long serialVersionUID = 42L;
        private String isrepair;

        private String unreachable;

        private String pictPath;

        private String backgrVal;

        private String deviceName;

        private String deviceId;

        private String equipmentId;

        // 净检测值 与 泄露阈值进行比较
        // 净检测值 >= 泄露阈值 是否泄露 是
        // 净检测值 < 泄露阈值 是否泄露 否
        // 1是0否
        private String isleakage;

        private String extNum;

        private String componentsId;

        private String areaId;

        private String areaName;

        private String tagNum;

        private String detectionVal;

        private String isretest;

        private String equipmentName;

        private String id;

        private String taskId;

        private String taskNum;

        private String detectionTime;

        private String delaRepair;

        // 新增 泄露阈值/DPM
        private String thresholdValue;

    }

}
