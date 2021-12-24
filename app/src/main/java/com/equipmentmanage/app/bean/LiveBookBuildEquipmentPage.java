package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: jeecg-boot-parent
 * @description: 建档设备
 * @author: STONE
 * @create: 2021-12-11
 **/
@Data
public class LiveBookBuildEquipmentPage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键*/
    //value = "主键")
    private String id;
    /**所属装置*/
    //value = "所属装置")
    private String deviceCode;
    /**所属区域*/
    //value = "所属区域")
    private String areaCode;
    /**编号*/
    //value = "编号")
    private String equipmentCode;
    /**名称*/
    //value = "名称")
    private String equipmentName;
    //value = "所属组件")
    List<LiveBookBuildPage> bookBuildPageList;
}
