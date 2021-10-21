package com.equipmentmanage.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Description: 任务列表实体
 * @Author: zzh
 * @CreateDate: 2021/8/16 17:03
 */
//@Entity
@Data
public class TaskBean implements Serializable {

    static final long serialVersionUID = 42L;

//    @Id(autoincrement = true)
//    private Long index;

//    @Property
    private String userTaskCompCount;

//    @Property
    private String taskSdate;

//    @Property
    private String uploadTime;

//    @Property
    private String detectionYear;

//    @Property
    private String belongCompany;

//    @Property
    private List<LiveTaskAppPicPage> liveTaskAppPicPageList;

//    @Property
    private String userDetectionCount;

//    @Property
    private String taskName;

//    @Property
    private String id;

//    @Property
    private String taskEdate;

//    @Property
    private String detectionUser;

//    @Property
    private String taskId;

//    @Property
    private String taskNum;

//    @Property
    private String leakageCount;
    

}
