package com.equipmentmanage.app.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: jeecg-boot-parent
 * @description: 建档明细
 * @author: STONE
 * @create: 2021-10-28
 **/
@Data
public class LiveBookBuildDetailPage implements Serializable {

    /**扩展号*/
    //value = "扩展号")
    private String extNum;

    //value = "图片标注X")
    private BigDecimal pointx;

    //value = "图片标注Y")
    private BigDecimal pointy;




}
