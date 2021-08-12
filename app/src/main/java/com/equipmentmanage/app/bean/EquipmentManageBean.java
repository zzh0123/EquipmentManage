package com.equipmentmanage.app.bean;

import lombok.Data;

/**
 * @Description: 设备实体
 * @Author: zzh
 * @CreateDate: 2021/8/10 16:39
 */
@Data
public class EquipmentManageBean {
    private String name;
    private String status;
    private String code;
    private String belongEquipment;
    private String belongArea;
}
