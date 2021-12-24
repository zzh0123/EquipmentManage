package com.equipmentmanage.app.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/4
 */
@Entity(tableName = "tb_base_company")
public class BaseCompanyTableBean {
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "content")
    public String content;
}
