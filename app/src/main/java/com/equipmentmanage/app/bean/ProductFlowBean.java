package com.equipmentmanage.app.bean;

import lombok.Data;

/**
 * @Description: 产品流实体
 * @Author: zzh
 * @CreateDate: 2021/8/10 16:39
 */
@Data
public class ProductFlowBean {
    private String name;
    private String status;
    private String code;
    private String belongEquipment;
    private String mediumStatus;
}
