package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/24 9:01
 */
@Data
public class PointBean implements Serializable {

    private float x;

    private float y;

    private String extNum;
}
