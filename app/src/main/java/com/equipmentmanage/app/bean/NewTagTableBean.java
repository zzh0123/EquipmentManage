package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/15
 */

@Entity(tableName = "tb_new_tag")
public class NewTagTableBean {

    @NonNull
    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "tag_num")
    public String tagNum;

    @ColumnInfo(name = "count")
    public String count;

    @ColumnInfo(name = "local_path")
    public String localPath;


    @ColumnInfo(name = "equip_code")
    public String equipCode;

    @ColumnInfo(name = "area_code")
    public String areaCode;

    @ColumnInfo(name = "device_code")
    public String deviceCode;
}
