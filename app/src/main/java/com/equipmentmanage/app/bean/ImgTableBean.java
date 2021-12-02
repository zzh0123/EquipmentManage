package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/24
 */
@Entity(tableName = "tb_base_img")
public class ImgTableBean {
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

    @ColumnInfo(name = "device_id")
    public String deviceId;

    @ColumnInfo(name = "area_id")
    public String areaId;

    @ColumnInfo(name = "equipment_id")
    public String equipmentId;

    @ColumnInfo(name = "medium_state")
    public String mediumState;

    @ColumnInfo(name = "prod_stream")
    public String prodStream;

    // 密封点
    @ColumnInfo(name = "component_type")
    public String componentType;

    // 化学品名称
    @ColumnInfo(name = "chemical_name")
    public String chemicalName;

    // 化学品
    @ColumnInfo(name = "chemical_type_value")
    public String chemicalTypeValue;

    // 方向名称
    @ColumnInfo(name = "direction_name")
    public String directionName;

    // 标签号
    @ColumnInfo(name = "tag_num")
    public String tagNum;
    // 参考物
    @ColumnInfo(name = "reference")
    public String reference;
    // 距离(米)
    @ColumnInfo(name = "distance")
    public String distance;
    // 高度(米)
    @ColumnInfo(name = "height")
    public String height;
    // 楼层
    @ColumnInfo(name = "floor_level")
    public String floorLevel;

    // 尺寸(mm
    @ColumnInfo(name = "size")
    public String size;
    // 生产厂家
    @ColumnInfo(name = "manufacturer")
    public String manufacturer;
    // 投产日期
    @ColumnInfo(name = "production_date")
    public String productionDate;
    // 不可达
    @ColumnInfo(name = "unreachable")
    public String unreachable;
    // 不可达原因
    @ColumnInfo(name = "unreachable_reason")
    public String unreachableReason;

    // 是否保温
    @ColumnInfo(name = "heat_preservation")
    public String heatPreservation;


    // 操作温度(℃)
    @ColumnInfo(name = "oper_temper")
    public String operTemper;
    // 操作压力(MPa)
    @ColumnInfo(name = "oper_pressure")
    public String operPressure;
    // 条形码
    @ColumnInfo(name = "barcode")
    public String barcode;
    // 检测频率/AOV检测频率
    @ColumnInfo(name = "detection_freq")
    public String detectionFreq;
    // 泄漏阈值/DPM
    @ColumnInfo(name = "leakage_threshold")
    public String leakageThreshold;

    // 缓存存储日期
    @ColumnInfo(name = "current_date")
    public String currentDate;

}
