package com.equipmentmanage.app.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.equipmentmanage.app.utils.AppUtils;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.checker.IThrowableHandler;
import com.xuexiang.xaop.logger.XLogger;

import java.util.ArrayList;


public class MyApplication extends Application {

    public static Context appContext;
    public static ArrayList<Activity> allActivities = new ArrayList<Activity>();
    public static MyApplication app;
    public static boolean LOG_SWITCH = true;
    public static String APP_NAME = "装置管理";


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        app = this;

        initAOP();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }

    private void initAOP(){

        XAOP.init(this); //初始化插件
//        XAOP.debug(true); //日志打印切片开启
//        XAOP.setPriority(Log.INFO); //设置日志打印的等级,默认为0

//设置动态申请权限切片 申请权限被拒绝的事件响应监听
//        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
//            @Override
//            public void onDenied(List<String> permissionsDenied) {
//                ToastUtil.get().toast("权限申请被拒绝:" + Utils.listToString(permissionsDenied));
//            }
//
//        });

//设置自定义拦截切片的处理拦截器
//        XAOP.setInterceptor(new Interceptor() {
//            @Override
//            public boolean intercept(int type, JoinPoint joinPoint) throws Throwable {
//                XLogger.d("正在进行拦截，拦截类型:" + type);
//                switch(type) {
//                    case 1:
//                        //做你想要的拦截
//                        break;
//                    case 2:
//                        return true; //return true，直接拦截切片的执行
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });

//设置自动捕获异常的处理者
//        XAOP.setIThrowableHandler(new IThrowableHandler() {
//            @Override
//            public Object handleThrowable(String flag, Throwable throwable) {
//                XLogger.d("捕获到异常，异常的flag:" + flag);
//                if (flag.equals(TRY_CATCH_KEY)) {
//                    return 100;
//                }
//                return null;
//            }
//        });


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
