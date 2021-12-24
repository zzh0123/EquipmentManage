package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/11 16:50
 */

@Data
public class NewAreaBean implements Serializable {
    private String code;
    private String name;
    private boolean isSelected;
}
