package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: jeecg-boot-parent
 * @description: 组件建档信息
 * @author: STONE
 * @create: 2021-10-27
 **/
@Data
public class LiveBookBuildPage implements Serializable {

    /**所属装置*/
    //"所属装置"
    private String deviceCode;
    /**所属区域*/
    //"所属区域"
    private String areaCode;
    /**所属设备*/
    //"所属设备"
    private String equipmentCode;
    /**标签号*/
    //"标签号"
    private String tagNum;
    //"参考物"
    private String refMaterial;
    /**方向*/
    //"方向"
    private String direction;
    //"方向名称"
    private String directionName;
    /**距离(米)*/
    //"距离(米)"
    private BigDecimal distance;
    /**高度(米)*/
    //"高度(米)"
    private BigDecimal height;
    /**楼层*/
    //"楼层"
    private String floorLevel;
    //"位置描述"
    private String remark;

    //"密封点"
    private String componentType;
    //"是否保温")
    private String heatPreser;
    /**尺寸(mm)*/
    //"尺寸(mm)"
    private BigDecimal componentSize;
    /**介质状态*/
    //"介质状态"
    private String mediumState;
    /**产品流*/
    //"产品流"
    private String prodStream;
    //"不可达"
    private String unreachable;
    /**不可达原因*/
    //"不可达原因"
    private String unreachableDesc;
    /**主要介质*/
    //"主要介质-化学品"
    private String mainMedium;
    //"图片路径")
    private String pictPath;
    //"扩展号明细"
    private List<LiveBookBuildDetailPage> bootBuildDetailList;

}
