package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/15
 */
@Data
public class PointBean1 implements Serializable {

    private float pointx;

    private float pointy;

    private float pointx1;

    private float pointy1;

    private String extNum;


    private String componentType;

    private String heatPreser;

    private int componentSize;



}
