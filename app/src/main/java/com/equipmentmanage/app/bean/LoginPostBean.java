package com.equipmentmanage.app.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * Author: zzh
 * Date: 2021/8/14 10:14
 * Description: 登录post json 实体
 */
@Data
public class LoginPostBean implements Serializable {

    private String captcha; // 验证码
    private String checkKey; // 验证码key
    private String username; // 账号
    private String password; // 密码
}
