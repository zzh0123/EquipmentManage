package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/12 16:50
 */

@Data
public class NewRecordBean implements Serializable {
    private String areaCode;

    private String areaId;

    private String areaName;

    private String belongCompany;

    private List<BootBuildDetailList> bootBuildDetailList;

    private String deviceCode;

    private String deviceId;

    private String deviceName;

    private String direction;

    private String directionName;

    private String distance;

    private String equipmentCode;

    private String equipmentId;

    private String equipmentName;

    private String floorLevel;

    private String height;

    private String mainMedium;

    private String mediumState;

    private String pathNum;

    private String pictPath;

    private String prodStream;

    private String refMaterial;

    private String remark;

    private String tagNum;

    private String unreachable;

    private String unreachableDesc;

    @Data
    public static class BootBuildDetailList implements Serializable{
        private String extNum;

        private float pointx;

        private float pointy;

        private float pointx1;

        private float pointy1;

        private String componentType;

        private String heatPreser;

        private int componentSize;

    }
}
