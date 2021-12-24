package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */

@Entity(tableName = "tb_new_area")
public class NewAreaTableBean {

    @NonNull
    @PrimaryKey
    public Long id;

    @ColumnInfo(name = "code")
    public String code;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "device_code")
    public String deviceCode;
}
