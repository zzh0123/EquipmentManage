/*
 *
 * Copyright (C) 2021 安徽中技国医医疗科技有限公司
 *
 * 文件：GsonUtils.java  模块：MvvmComponent.library-base  项目：MvvmComponent
 * 当前修改时间：2021年01月18日 15:18:41
 * 上次修改时间：2021年01月18日 11:54:34
 * 作者：SPD
 *
 *
 */

package com.equipmentmanage.app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * 应用模块:utils
 * <p>
 * 类描述: json解析工具类
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-01-27
 */
public class GsonUtils
{
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    
    private static final Gson sLocalGson = createLocalGson();
    
    private static final Gson sRemoteGson = createRemoteGson();
    
    private static GsonBuilder createLocalGsonBuilder()
    {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setDateFormat(DATE_FORMAT);
        return gsonBuilder;
    }
    
    private static Gson createLocalGson()
    {
        return createLocalGsonBuilder().create();
    }
    
    private static Gson createRemoteGson()
    {
        return createLocalGsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .create();
    }
    
    public static Gson getLocalGson()
    {
        return sLocalGson;
    }
    
    public static <T> T fromLocalJson(String json, Class<T> clazz)
        throws JsonSyntaxException
    {
        try
        {
            return sLocalGson.fromJson(json, clazz);
        }
        catch (JsonSyntaxException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T fromLocalJson(String json, Type typeOfT)
    {
        return sLocalGson.fromJson(json, typeOfT);
    }
    
    public static String toJson(Object src)
    {
        return sLocalGson.toJson(src);
    }
    
    public static String toRemoteJson(Object src)
    {
        return sRemoteGson.toJson(src);
    }
}
