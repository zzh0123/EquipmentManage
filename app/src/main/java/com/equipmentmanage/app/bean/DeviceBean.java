package com.equipmentmanage.app.bean;

import lombok.Data;

/**
 * @Description: 装置实体
 * @Author: zzh
 * @CreateDate: 2021/8/10 16:39
 */
@Data
public class DeviceBean {
    private String name;
    private String status;
    private String code;
    private String department;
    private String type;
    private String date;
}
