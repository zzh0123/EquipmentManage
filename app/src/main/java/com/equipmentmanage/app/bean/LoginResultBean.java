package com.equipmentmanage.app.bean;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/14 11:12
 * Description: 登录返回结果实体
 */
@Data
public class LoginResultBean {

    private UserInfoBean userInfo;

    private String token;
}
