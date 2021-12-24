package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: jeecg-boot-parent
 * @description: 建档区域
 * @author: STONE
 * @create: 2021-12-11
 **/
@Data
public class LiveBookBuildAreaPage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键*/
    //value = "主键")
    private String id;
    /**编号*/
    //value = "编号")
    private String areaCode;
    /**名称*/
    //value = "名称")
    private String areaName;
    /**所属装置*/
    //value = "所属装置")
    private String deviceCode;

    //value = "所属设备")
    List<LiveBookBuildEquipmentPage> bookBuildEquipmentPageList;
}
