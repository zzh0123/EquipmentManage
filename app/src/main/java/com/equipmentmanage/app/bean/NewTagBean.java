package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/15 16:50
 */

@Data
public class NewTagBean implements Serializable {
    private String tagNum;
    private String count;
    public String localPath;
    private boolean isSelected;
}
