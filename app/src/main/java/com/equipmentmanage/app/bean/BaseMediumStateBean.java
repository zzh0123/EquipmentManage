package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/26 15:25
 */

@Data
public class BaseMediumStateBean implements Serializable {
    private String value;

    private String text;

    private String title;
}
