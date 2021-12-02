package com.equipmentmanage.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.CorrectCheckBean;
import com.equipmentmanage.app.bean.GasTableBean;
import com.equipmentmanage.app.bean.WeatherParamsBean;
import com.equipmentmanage.app.bean.WeatherParamsTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 气象参数
 * @Author: zzh
 * @CreateDate: 2021/8/17
 */
public class WeatherParamsActivity extends BaseActivity {

    public static void open(Context c, String belongCompany, String id){
        Intent i = new Intent(c, WeatherParamsActivity.class);
        i.putExtra(Constant.belongCompany, belongCompany);
        i.putExtra(Constant.id, id);
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
    EditText etWindDirection; //风向
//    TextView tvWindDirection; //风向

    @BindView(R.id.et_pressure)
    EditText etPressure; //气压

    @BindView(R.id.tv_save)
    TextView tv_save; //保存

    private String belongCompany, id;

    private MMKV kv = MMKV.defaultMMKV();

    @Override
    protected int initLayout() {
        return R.layout.activity_weather_params;
    }

    @Override
    protected void initView() {

//        bean = (AreaManageResultBean.Records) getIntent().getSerializableExtra(Constant.areaBean);
        belongCompany = getIntent().getStringExtra(Constant.belongCompany);
        id = getIntent().getStringExtra(Constant.id);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                savaData();
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
        readCache();
    }

    @OnClick({R.id.cstl_wind_direction, R.id.tv_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cstl_wind_direction:

                break;
            case R.id.tv_save: // 保存
                savaData();
//                accessWeather();
                break;
        }
    }

    /**
     * 气象参数-缓存
     */
    private void readCache() {
        // 装置
        WeatherParamsTableBean list = AppDatabase.getInstance(this).weatherParamsTableDao().loadById(id);
//        L.i("zzz1-weatherParamsBean00->" + GsonUtils.toJson(list));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            WeatherParamsBean weatherParamsBean = GsonUtils.fromJson(list.content, new TypeToken<WeatherParamsBean>() {
            }.getType());
            L.i("zzz1-weatherParamsBean 222--->" + weatherParamsBean.getTemperatures());

            if (weatherParamsBean != null) {
                // 温度
                etTemperature.setText("" + weatherParamsBean.getTemperatures());
                // 湿度
                etHumidity.setText("" + weatherParamsBean.getMoisture());
                // 风速
                etWindSpeed.setText("" + weatherParamsBean.getWindSpeed());
                // 风向
                etWindDirection.setText(StringUtils.nullStrToEmpty(weatherParamsBean.getWindDirection()));
                // 气压
                etPressure.setText("" + weatherParamsBean.getPressure());
            }

        }

    }

    private void savaData(){
        if (StringUtils.isNullOrEmpty(etTemperature.getText().toString().trim())) {
            Toasty.warning(WeatherParamsActivity.this, "温度为空！", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (StringUtils.isNullOrEmpty(etHumidity.getText().toString().trim())) {
            Toasty.warning(WeatherParamsActivity.this, "湿度为空！", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (StringUtils.isNullOrEmpty(etWindSpeed.getText().toString().trim())) {
            Toasty.warning(WeatherParamsActivity.this, "风速为空！", Toast.LENGTH_SHORT, true).show();
            return;
        }

        if (StringUtils.isNullOrEmpty(etWindDirection.getText().toString().trim())) {
            Toasty.warning(WeatherParamsActivity.this, "风向为空！", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if (StringUtils.isNullOrEmpty(etPressure.getText().toString().trim())) {
            Toasty.warning(WeatherParamsActivity.this, "气压为空！", Toast.LENGTH_SHORT, true).show();
            return;
        }

        WeatherParamsBean weatherParamsBean = new WeatherParamsBean();
        weatherParamsBean.setBelongCompany(belongCompany);
        weatherParamsBean.setTemperatures(Integer.parseInt(etTemperature.getText().toString().trim())); // temperatures 温度(℃)
        weatherParamsBean.setMoisture(Integer.parseInt(etHumidity.getText().toString().trim())); // moisture 湿度(%RH)
        weatherParamsBean.setWindSpeed(Integer.parseInt(etWindSpeed.getText().toString().trim())); // windSpeed 风速(米/秒)
        weatherParamsBean.setWindDirection(StringUtils.nullStrToEmpty(etWindDirection.getText().toString().trim())); // windDirection 风向
        weatherParamsBean.setPressure(Integer.parseInt(etPressure.getText().toString().trim())); // pressure 气压(hPa)

        WeatherParamsTableBean weatherParamsTableBean = new WeatherParamsTableBean();
        weatherParamsTableBean.id = id;
        weatherParamsTableBean.content = GsonUtils.toJson(weatherParamsBean);
        Long rowId = AppDatabase.getInstance(WeatherParamsActivity.this).weatherParamsTableDao().insert(weatherParamsTableBean);
        if (rowId != null) {
            kv.encode(Constant.is_weather_checked + id, "1");
            Toasty.success(WeatherParamsActivity.this, "保存成功！", Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.error(WeatherParamsActivity.this, "保存失败！", Toast.LENGTH_SHORT, true).show();
        }

        EventBus.getDefault().post(ConstantValue.event_weather_save);
    }

    /**
     * 气象参数
     */
    private void accessWeather() {
        WeatherParamsBean weatherParamsBean = new WeatherParamsBean();
        weatherParamsBean.setTemperatures(Integer.parseInt(etTemperature.getText().toString().trim())); // temperatures 温度(℃)
        weatherParamsBean.setMoisture(Integer.parseInt(etHumidity.getText().toString().trim())); // moisture 湿度(%RH)
        weatherParamsBean.setWindSpeed(Integer.parseInt(etWindSpeed.getText().toString().trim())); // windSpeed 风速(米/秒)
        weatherParamsBean.setWindDirection(StringUtils.nullStrToEmpty(etWindDirection.getText().toString().trim())); // windDirection 风向
        weatherParamsBean.setPressure(Integer.parseInt(etPressure.getText().toString().trim())); // pressure 气压(hPa)

        Subscribe.accessWeather(weatherParamsBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            Toasty.success(WeatherParamsActivity.this, R.string.submit_success, Toast.LENGTH_SHORT, true).show();
                            clearView();
                        } else {
                            Toasty.error(WeatherParamsActivity.this, R.string.submit_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(WeatherParamsActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(WeatherParamsActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(WeatherParamsActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, WeatherParamsActivity.this));
    }

    private void clearView() {
        etTemperature.setText("");
        etHumidity.setText("");
        etWindSpeed.setText("");
        etWindDirection.setText("");
        etPressure.setText("");
    }

    @Override
    public void onBackPressed() {
        savaData();
        finish();
//        super.onBackPressed();
    }
}