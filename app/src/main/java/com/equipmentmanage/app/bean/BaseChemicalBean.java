package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/30 17:42
 */
@Data
public class BaseChemicalBean implements Serializable {
    private String id;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private String sysOrgCode;

    private String chemicalsId;

    private String chemicalsCode;

    private String chemicalsName;

    private String casNum;

    private String chemicalFormula;

    private int boilingPoint;

    private int vapourPressure;

    private String responseFactor;

    private String isPrincipal;

    private String belongCompany;

    private String eftflag;
}
