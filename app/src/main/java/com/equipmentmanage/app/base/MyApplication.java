package com.equipmentmanage.app.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.equipmentmanage.app.utils.AppUtils;

import java.util.ArrayList;


public class MyApplication extends Application {

    public static Context appContext;
    public static ArrayList<Activity> allActivities = new ArrayList<Activity>();
    public static MyApplication app;
    public static boolean LOG_SWITCH = true;
    public static String APP_NAME;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        app = this;
        APP_NAME = AppUtils.getAppName(this);
    }

    public static Context getContext(){
        return appContext;
    }

    public static MyApplication getApp(){
        return app;
    }

    public static void addActivity(Activity activity) {
        allActivities.add(activity);
    }

    public static void delActivity(Activity activity) {
        allActivities.remove(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for(Activity activity:allActivities) {
            activity.finish();
        }
        allActivities.clear();
    }
}
