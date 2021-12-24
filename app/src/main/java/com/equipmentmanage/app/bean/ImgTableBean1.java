package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/24
 */
@Entity(tableName = "tb_base_img1")
public class ImgTableBean1 implements Serializable {
//    @NonNull
//    @PrimaryKey
//    public String id;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "file_name")
    public String fileName;

    @ColumnInfo(name = "local_path")
    public String localPath;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "content")
    public String content;

    // 缓存存储日期
    @ColumnInfo(name = "current_date")
    public String currentDate;


    /**所属装置*/
    //"所属装置"
    @ColumnInfo(name = "device_code")
    public String deviceCode;
    //"所属装置"
    @ColumnInfo(name = "device_name")
    public String deviceName;
    @ColumnInfo(name = "device_type")
    public String deviceType;
    @ColumnInfo(name = "device_id")
    public String deviceId;
    /**所属区域*/
    //"所属区域"
    @ColumnInfo(name = "area_code")
    public String areaCode;
    @ColumnInfo(name = "area_name")
    public String areaName;
    /**所属设备*/
    //"所属设备"
    @ColumnInfo(name = "equipment_code")
    public String equipmentCode;
    @ColumnInfo(name = "equip_name")
    public String equipName;

    /**标签号*/
    //"标签号"
    @ColumnInfo(name = "tag_num")
    public String tagNum;
    //"参考物"
    @ColumnInfo(name = "ref_material")
    public String refMaterial;
    /**方向*/
    //"方向"
    @ColumnInfo(name = "direction_value")
    public String directionValue;
    //"方向名称"
    @ColumnInfo(name = "direction_name")
    public String directionName;

    /**方位*/
    //"方位"
    @ColumnInfo(name = "direction_pos_value")
    public String directionPosValue;
    //"方位名称"
    @ColumnInfo(name = "direction_pos_name")
    public String directionPosName;

    /**距离(米)*/
    //"距离(米)"
    @ColumnInfo(name = "distance")
    public String distance;
    /**高度(米)*/
    //"高度(米)"
    @ColumnInfo(name = "height")
    public String height;
    /**楼层*/
    //"楼层"
    @ColumnInfo(name = "floor_level")
    public String floorLevel;
    //"位置描述"
    @ColumnInfo(name = "remark")
    public String remark;

    //"密封点"
    @ColumnInfo(name = "component_type")
    public String componentType;
    //"是否保温")
    @ColumnInfo(name = "heat_preser")
    public String heatPreser;
    /**尺寸(mm)*/
    //"尺寸(mm)"
    @ColumnInfo(name = "component_size")
    public String componentSize;
    /**介质状态*/
    //"介质状态"
    @ColumnInfo(name = "medium_state")
    public String mediumState;
    /**产品流*/
    //"产品流"
    @ColumnInfo(name = "prod_stream")
    public String prodStream;

    @ColumnInfo(name = "prod_stream_name")
    public String prodStreamName;

    //"不可达"
    @ColumnInfo(name = "unreachable")
    public String unreachable;
    /**不可达原因*/
    //"不可达原因"
    @ColumnInfo(name = "unreachable_desc")
    public String unreachableDesc;
    /**主要介质*/
    //"主要介质-化学品"
    @ColumnInfo(name = "main_medium")
    public String mainMedium;

    @ColumnInfo(name = "chemical_name")
    public String chemicalName;

    //"图片路径")
    @ColumnInfo(name = "pict_path")
    public String pictPath;

}
