package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Entity(tableName = "tb_base_stream")
public class BaseStreamTableBean {
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "content")
    public String content;
}
