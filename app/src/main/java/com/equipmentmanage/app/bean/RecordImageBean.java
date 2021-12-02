package com.equipmentmanage.app.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/24 13:26
 */
@Data
public class RecordImageBean implements Serializable {

    private String url;

    private String localPath;

    private List<PointDot> points;

    @Data
    public static class PointDot implements Serializable{
        private float x;

        private float y;
    }
}
