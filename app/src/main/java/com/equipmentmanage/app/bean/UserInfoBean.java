package com.equipmentmanage.app.bean;

import lombok.Data;

/**
 * Author: zzhh
 * Date: 2021/8/14 11:14
 * Description: 用户信息实体
 */
@Data
public class UserInfoBean {
    private String id;

    private String username;

    private String realname;

    private String avatar;

    private String birthday;

    private String sex;

    private String email;

    private String phone;

    private String orgCode;

    private String orgCodeTxt;

    private int status;

    private int delFlag;

    private String workNo;

    private String post;

    private String telephone;

    private String createBy;

    private String createTime;

    private String updateBy;

    private String updateTime;

    private int activitiSync;

    private int userIdentity;

    private String departIds;

    private String relTenantIds;

    private String clientId;
}
