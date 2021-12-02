package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/22 21:19
 */
@Entity(tableName = "tb_task")
public class TaskTableBean {
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "content")
    public String content;
}
