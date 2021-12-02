package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/11/6
 */
@Entity(tableName = "tb_base_task_records")
public class TaskRecordsTableBean {
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "content")
    public String content;
}
