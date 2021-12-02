package com.equipmentmanage.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/8/1 15:17
 */
public class DateUtil {


    private static String getTime1(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    public static void showDateDialog1(Context context, View view) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1921, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(endDate.get(Calendar.YEAR) + 10, 12, endDate.get(Calendar.DAY_OF_MONTH));
        TimePickerView pvCustomTime = new TimePickerBuilder(context, ((date, v) -> {
            if (view instanceof TextView) {
                ((TextView) view).setText(getTime1(date));
            }
        }))
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentTextSize(15)
                .setSubCalSize(15)
                .setDividerColor(context.getResources().getColor(R.color.divider))//设置分割线的颜色
                .setTextColorCenter(context.getResources().getColor(R.color.black))//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setBgColor(context.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setTitleBgColor(context.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setSubmitColor(context.getResources().getColor(R.color.blue3))
                .setCancelColor(context.getResources().getColor(R.color.text_gray9))
                /*.animGravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvCustomTime.show();
    }

    public static Dialog showDateDialog2(Context context, View view) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1921, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(endDate.get(Calendar.YEAR) + 10, 12, endDate.get(Calendar.DAY_OF_MONTH));
        TimePickerView pvCustomTime = new TimePickerBuilder(context, ((date, v) -> {
            if (view instanceof TextView) {
                ((TextView) view).setText(getTime1(date));
            }
        }))
                .setCancelText("取消")
                .setSubmitText("确定")
                .setContentTextSize(15)
                .setSubCalSize(15)
                .setDividerColor(context.getResources().getColor(R.color.divider))//设置分割线的颜色
                .setTextColorCenter(context.getResources().getColor(R.color.black))//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setBgColor(context.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setTitleBgColor(context.getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setSubmitColor(context.getResources().getColor(R.color.blue3))
                .setCancelColor(context.getResources().getColor(R.color.text_gray9))
                /*.animGravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)
                .build();
        pvCustomTime.show();

        return pvCustomTime.getDialog();
    }

    public static Long getTimeStamp(String time) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //设置要读取的时间字符串格式
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (null==date){
            return 0L;
        }
        //转换为Date类
        Long timestamp = date.getTime();
        L.i("zzz1-timestamp->" + timestamp);
        return timestamp;
    }

    public static String getCurentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    public static String getCurentTime1() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }
}
