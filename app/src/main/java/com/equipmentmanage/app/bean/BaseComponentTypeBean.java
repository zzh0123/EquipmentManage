package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/30 14:45
 */
@Data
public class BaseComponentTypeBean implements Serializable {

    private String componentTypeCode;

    private String componentTypeName;

}
