package com.equipmentmanage.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Util {

    public static Object convertMapToList(String reqResultStr, TypeToken typeToken) {
        Gson gson = getInstance();
        Type type = typeToken.getType();
        if (null == reqResultStr || "".equals(reqResultStr) || "{}".equals(reqResultStr)) {
            return null;
        } else {
            return gson.fromJson(reqResultStr, type);
        }
    }

    //饿汉式单例 节省内存
    public static Gson getInstance() {
        Gson gson = null;
        if (gson == null) {
            synchronized (Gson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    public static String readAssert(Context context, String fileName){
        String jsonString="";
        String resultString="";
        try {
            InputStream inputStream=context.getResources().getAssets().open(fileName);
            byte[] buffer=new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString=new String(buffer,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    // 状态栏高度
    private static  int statusBarHeight = 0;
    // 屏幕像素点
    private static final Point screenSize = new Point();

    // 获取屏幕像素点
    public static Point getScreenSize(Activity context) {

        if (context == null) {
            return screenSize;
        }
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics mDisplayMetrics = new DisplayMetrics();
            Display diplay = wm.getDefaultDisplay();
            if (diplay != null) {
                diplay.getMetrics(mDisplayMetrics);
                int W = mDisplayMetrics.widthPixels;
                int H = mDisplayMetrics.heightPixels;
                if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
                    screenSize.set(W, H);
                }
            }
        }
        return screenSize;
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight <= 0) {
            Rect frame = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (statusBarHeight <= 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(x);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public static Map<String, Object> transJsonToMap(String jsonStr) {
        try {
            if (null != jsonStr && !"".equals(jsonStr) && !"{}".equals(jsonStr) && !"null".equals(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Iterator<String> keyItems = jsonObject.keys();
                Map<String, Object> map = new HashMap<>();
                String key, value;
                while (keyItems.hasNext()) {
                    key = keyItems.next();
                    value = jsonObject.getString(key);
                    map.put(key, value);
                }
                return map;
            }
        } catch (JSONException e) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("list", jsonStr);
                return map;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }
        return null;
    }
    public static Map<String, Object> transResultJsonToMap(String jsonStr) {
        try {
            if (null != jsonStr && !"".equals(jsonStr) && !"{}".equals(jsonStr) && !"null".equals(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Iterator<String> keyItems = jsonObject.keys();
                Map<String, Object> map = new HashMap<>();
                String key, value;
                while (keyItems.hasNext()) {
                    key = keyItems.next();
                    value = jsonObject.getString(key);
                    map.put(key, value);
                }
                return map;
            }
        } catch (JSONException e) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("randomCode", jsonStr);//返回json是乱码
                return map;
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
