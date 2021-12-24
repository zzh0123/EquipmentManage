package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: jeecg-boot-parent
 * @description: 装置建档
 * @author: STONE
 * @create: 2021-12-11
 **/
@Data
public class LiveBookBuildDevicePage implements Serializable {
    private static final long serialVersionUID = 1L;

    //value = "主键")
    private String id;
    /**编号*/
    //value = "编号")
    private String deviceCode;
    /**名称*/
    //value = "名称")
    private String deviceName;
    /**装置类型*/
    //value = "装置类型")
    private String deviceType;
    /**归属公司*/
    //value = "归属公司")
    private String belongCompany;
    //value = "建档区域")
    List<LiveBookBuildAreaPage> bookBuildAreaList;
}
