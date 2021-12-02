package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/25 16:51
 */
@Data
public class CorrectCheckBean implements Serializable {

    private String belongCompany;

    private String correctingDate;

    private String correctingDevice;

    private String createBy;

    private String createTime;

    private String detectionPeople;

    private String eftflag;

    private String id;

    private List<LiveCorrectingGasList> liveCorrectingGasList;

    private String remark;

    private String sysOrgCode;

    private String taskId;

    private String updateBy;

    private String updateTime;

    @Data
    public static class LiveCorrectingGasList implements Serializable{
        private String calibGas;

        private int calibReading;

        private String correctingId;

        private String createBy;

        private String createTime;

        private int gasAreading;

        private int gasBreading;

        private String id;

        private String isapass;

        private String isbpass;

        private int ppm;

        private int responseAtime;

        private int responseBtime;

        private String sysOrgCode;

        private String updateBy;

        private String updateTime;
    }
}
