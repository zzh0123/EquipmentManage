package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/13 15:14
 */

@Data
public class CommonSelectBean implements Serializable {
    private String name;
    private String value;
    private boolean isSelected;
}
