package com.equipmentmanage.app.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;
import androidx.multidex.MultiDex;

import com.equipmentmanage.app.BuildConfig;
//import com.equipmentmanage.app.db.utils.DaoManager;
import com.equipmentmanage.app.listener.PictureSelectorEngineImp;
import com.equipmentmanage.app.netapi.BaseConstant;
import com.equipmentmanage.app.utils.AppUtils;
import com.equipmentmanage.app.utils.ReleaseServer;
import com.equipmentmanage.app.utils.RequestHandler;
import com.equipmentmanage.app.utils.TestServer;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.checker.IThrowableHandler;
import com.xuexiang.xaop.logger.XLogger;

import java.util.ArrayList;

import okhttp3.OkHttpClient;


public class MyApplication extends Application implements IApp, CameraXConfig.Provider{

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

        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);
        initAOP();
//        DaoManager.getInstance().init(this);

        PictureAppMaster.getInstance().setApp(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
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

    private void initEasyHttp(){
        // 网络请求框架初始化
        IRequestServer server;
        if (BuildConfig.DEBUG) {
            server = new TestServer();
        } else {
            server = new ReleaseServer();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
                //.setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(this))
                // 设置请求参数拦截器
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(IRequestApi api, HttpParams params, HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                .addParam("X-Access-Token",  BaseConstant.TOKEN)
                // 添加全局请求头
                //.addHeader("time", "20191030")
                .into();
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
