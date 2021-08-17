package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.utils.StringUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 气象参数
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class WeatherParamsActivity extends BaseActivity {

    public static void open(Context c){
        Intent i = new Intent(c, WeatherParamsActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.et_temperature)
    EditText etTemperature; //温度

    @BindView(R.id.et_humidity)
    EditText etHumidity; //湿度

    @BindView(R.id.et_wind_speed)
    EditText etWindSpeed; //风速

    @BindView(R.id.cstl_wind_direction)
    ConstraintLayout cstlWindDirection; //风向

    @BindView(R.id.tv_wind_direction)
    TextView tvWindDirection; //风向

    @BindView(R.id.et_pressure)
    EditText etPressure; //气压

    @Override
    protected int initLayout() {
        return R.layout.activity_weather_params;
    }

    @Override
    protected void initView() {

//        bean = (AreaManageResultBean.Records) getIntent().getSerializableExtra(Constant.areaBean);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        if (bean != null) {
//            tvAreaName.setText(StringUtils.nullStrToEmpty(bean.getAreaName())); //名称
//            tvAreaCode.setText(StringUtils.nullStrToEmpty(bean.getAreaCode())); //编号
//            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getBelongDevice_dictText())); //所属装置
////            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
//        }

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
//        refresh();
    }

    @OnClick({R.id.cstl_wind_direction})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cstl_wind_direction:
//                if (valid()){
//                    login();
//                }
                break;
        }
    }
}