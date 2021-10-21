package com.equipmentmanage.app.bean;

import lombok.Data;

import java.util.List;

/**
 * @program: jeecg-boot-parent
 * @description:
 * @author: STONE
 * @create: 2021-10-16
 **/
@Data
public class LiveTaskAppPage {

    //"主键")
    private String id;
    //"任务ID")
    private String taskId;
    //"任务编号")
    private String taskNum;
    //"任务名称")
    private String taskName;

    //"检测人")
    private String detectionUser;
    //"开始日期")
    private String taskSdate;
    //"结束日期")
    private String taskEdate;
    //"检测年度")
    private String detectionYear;

    //"归属公司")
    private String belongCompany;
    //"任务总组件数")
    private String userTaskCompCount;
    //"已检测数")
    private String userDetectionCount;
    //"泄漏总数")
    private String leakageCount;

    //"标签信息") 要展示的
    private String tagInfo;

    //"任务标签图片")
    List<LiveTaskAppPicPage> liveTaskAppPicPageList;

}
