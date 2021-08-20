package com.equipmentmanage.app.utils;


import com.equipmentmanage.app.netapi.URLConstant;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/EasyHttp
 *    time   : 2019/05/19
 *    desc   : 测试环境
 */
public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
        return URLConstant.BASE_URL;
    }
}