package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: jeecg-boot-parent
 * @description: 已分配任务
 * @author: STONE
 * @create: 2021-10-16
 **/
@Data
public class LiveTaskAppAssignedPage implements Serializable {

    //"主键")
    private String id;

    //"任务ID")
    private String taskId;

    //"任务编号")
    private String taskNum;

    //"组件ID")
    private String componentsId;

    //"所属装置")
    private String deviceName;
    //"所属区域")
    private String areaName;
    //"所属设备")
    private String equipmentName;

    //"所属装置ID")
    private String deviceId;
    //"所属区域ID")
    private String areaId;
    //"所属设备ID")
    private String equipmentId;

    //"标签号")
    private String tagNum;

    //"扩展号")
    private String extNum;

    //"图片路径")
    private String pictPath;





    //"净检测值") 传蓝牙值
    private BigDecimal detectionVal;

    //"是否泄漏")
    private String isleakage;
    //"是否修复")
    private String isrepair;
    //"是否复测")
    private String isretest;
    //"延迟修复")
    private String delaRepair;

    //"不可达")
    private String unreachable;

    // "背景值") 蓝牙传值
    private BigDecimal backgrVal;

}
