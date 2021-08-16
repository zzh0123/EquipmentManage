package com.equipmentmanage.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Description: 任务列表实体
 * @Author: zzh
 * @CreateDate: 2021/8/16 17:03
 */
@Entity
@Data
public class TaskBean implements Serializable {

    static final long serialVersionUID = 42L;

//    @Id(autoincrement = true)
//    private Long index;

    @Property
    private String detectionSdate;

    @Property
    private String areaId_dictText;

    @Property
    private String taskEnd;

    @Property
    private String detectionPeriod_dictText;

    @Property
    private String detectionYear;

    @Property
    private String belongCompany;

    @Property
    private String detectionYear_dictText;

    @Property
    private String createScale;

    @Property
    private String deviceId;

    @Property
    private String createScale_dictText;

    @Property
    private String updateBy;

    @Property
    private String detectionEdate;

    @Id
    private String id;

    @Property
    private String taskNum;

    @Property
    private String deviceId_dictText;

    @Property
    private String planType;

    @Property
    private String inspectionType_dictText;

    @Property
    private String inspectionType;

    @Property
    private String updateTime;

    @Property
    private String detectionPeriod;

    @Property
    private String createBy;

    @Property
    private String areaId;

    @Property
    private String createTime;

    @Property
    private String planType_dictText;

    @Property
    private String taskEnd_dictText;

    @Property
    private String sysOrgCode;

    @Property
    private String taskName;

    @Generated(hash = 799590373)
    public TaskBean(String detectionSdate, String areaId_dictText, String taskEnd,
            String detectionPeriod_dictText, String detectionYear,
            String belongCompany, String detectionYear_dictText, String createScale,
            String deviceId, String createScale_dictText, String updateBy,
            String detectionEdate, String id, String taskNum,
            String deviceId_dictText, String planType,
            String inspectionType_dictText, String inspectionType,
            String updateTime, String detectionPeriod, String createBy,
            String areaId, String createTime, String planType_dictText,
            String taskEnd_dictText, String sysOrgCode, String taskName) {
        this.detectionSdate = detectionSdate;
        this.areaId_dictText = areaId_dictText;
        this.taskEnd = taskEnd;
        this.detectionPeriod_dictText = detectionPeriod_dictText;
        this.detectionYear = detectionYear;
        this.belongCompany = belongCompany;
        this.detectionYear_dictText = detectionYear_dictText;
        this.createScale = createScale;
        this.deviceId = deviceId;
        this.createScale_dictText = createScale_dictText;
        this.updateBy = updateBy;
        this.detectionEdate = detectionEdate;
        this.id = id;
        this.taskNum = taskNum;
        this.deviceId_dictText = deviceId_dictText;
        this.planType = planType;
        this.inspectionType_dictText = inspectionType_dictText;
        this.inspectionType = inspectionType;
        this.updateTime = updateTime;
        this.detectionPeriod = detectionPeriod;
        this.createBy = createBy;
        this.areaId = areaId;
        this.createTime = createTime;
        this.planType_dictText = planType_dictText;
        this.taskEnd_dictText = taskEnd_dictText;
        this.sysOrgCode = sysOrgCode;
        this.taskName = taskName;
    }

    @Generated(hash = 1443476586)
    public TaskBean() {
    }

    public String getDetectionSdate() {
        return this.detectionSdate;
    }

    public void setDetectionSdate(String detectionSdate) {
        this.detectionSdate = detectionSdate;
    }

    public String getAreaId_dictText() {
        return this.areaId_dictText;
    }

    public void setAreaId_dictText(String areaId_dictText) {
        this.areaId_dictText = areaId_dictText;
    }

    public String getTaskEnd() {
        return this.taskEnd;
    }

    public void setTaskEnd(String taskEnd) {
        this.taskEnd = taskEnd;
    }

    public String getDetectionPeriod_dictText() {
        return this.detectionPeriod_dictText;
    }

    public void setDetectionPeriod_dictText(String detectionPeriod_dictText) {
        this.detectionPeriod_dictText = detectionPeriod_dictText;
    }

    public String getDetectionYear() {
        return this.detectionYear;
    }

    public void setDetectionYear(String detectionYear) {
        this.detectionYear = detectionYear;
    }

    public String getBelongCompany() {
        return this.belongCompany;
    }

    public void setBelongCompany(String belongCompany) {
        this.belongCompany = belongCompany;
    }

    public String getDetectionYear_dictText() {
        return this.detectionYear_dictText;
    }

    public void setDetectionYear_dictText(String detectionYear_dictText) {
        this.detectionYear_dictText = detectionYear_dictText;
    }

    public String getCreateScale() {
        return this.createScale;
    }

    public void setCreateScale(String createScale) {
        this.createScale = createScale;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreateScale_dictText() {
        return this.createScale_dictText;
    }

    public void setCreateScale_dictText(String createScale_dictText) {
        this.createScale_dictText = createScale_dictText;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDetectionEdate() {
        return this.detectionEdate;
    }

    public void setDetectionEdate(String detectionEdate) {
        this.detectionEdate = detectionEdate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskNum() {
        return this.taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    public String getDeviceId_dictText() {
        return this.deviceId_dictText;
    }

    public void setDeviceId_dictText(String deviceId_dictText) {
        this.deviceId_dictText = deviceId_dictText;
    }

    public String getPlanType() {
        return this.planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getInspectionType_dictText() {
        return this.inspectionType_dictText;
    }

    public void setInspectionType_dictText(String inspectionType_dictText) {
        this.inspectionType_dictText = inspectionType_dictText;
    }

    public String getInspectionType() {
        return this.inspectionType;
    }

    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDetectionPeriod() {
        return this.detectionPeriod;
    }

    public void setDetectionPeriod(String detectionPeriod) {
        this.detectionPeriod = detectionPeriod;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getAreaId() {
        return this.areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPlanType_dictText() {
        return this.planType_dictText;
    }

    public void setPlanType_dictText(String planType_dictText) {
        this.planType_dictText = planType_dictText;
    }

    public String getTaskEnd_dictText() {
        return this.taskEnd_dictText;
    }

    public void setTaskEnd_dictText(String taskEnd_dictText) {
        this.taskEnd_dictText = taskEnd_dictText;
    }

    public String getSysOrgCode() {
        return this.sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    

}
