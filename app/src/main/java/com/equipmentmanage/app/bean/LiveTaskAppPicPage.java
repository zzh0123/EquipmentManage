package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: jeecg-boot-parent
 * @description: 任务标签图片明细
 * @author: STONE
 * @create: 2021-10-16
 **/
@Data
public class LiveTaskAppPicPage implements Serializable {

    //"任务ID")
    private String taskId;

    //"标签号")
    private String tagNum;

    //"图片路径")
    private String pictPath;

    //"检测组件")
    List<LiveTaskAppAssignedPage> liveTaskAppAssignedPageList;
}
