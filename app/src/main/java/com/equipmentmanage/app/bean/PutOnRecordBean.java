package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/24 9:28
 */
@Data
public class PutOnRecordBean implements Serializable {

    private String areaId;

    private String barCode;

    private List<PointBean> bootBuildDetailList;

    private String componentSize;

    private String componentType;

    private String detectionFreq;

    private String deviceId;

    private String direction;

    private String distance;

    private String equipmentId;

    private String floorLevel;

    private String heatPreser;

    private String height;

    private String mainMedium;

    private String manufacturers;

    private String mediumState;

    private String operPressure;

    private String operTemper;

    private String pictPath;

    private String prodStream;

    private String refMaterial;

    private String remark;

    private String startTime;

    private String tagNum;

    private String thresholdValue;

    private String unreachable;

    private String unreachableDesc;

    private String belongCompany;

    private String directionName;


}
