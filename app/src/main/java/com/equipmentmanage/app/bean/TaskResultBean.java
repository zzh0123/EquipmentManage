package com.equipmentmanage.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/16
 * Description: 任务列表实体
 */
@Data
public class TaskResultBean implements Serializable {

    static final long serialVersionUID = 42L;

    private List<TaskBean> records;

    private int total;

    private int size;

    private int current;

    private List<String> orders;

    private boolean optimizeCountSql;

    private boolean hitCount;

    private String countId;

    private String maxLimit;

    private boolean searchCount;

    private int pages;


}
