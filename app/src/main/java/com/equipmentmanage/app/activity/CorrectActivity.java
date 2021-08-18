package com.equipmentmanage.app.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.StringUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 漂移校准/校准设备
 * @Author: zzh
 * @CreateDate: 2021/8/18
 */
public class CorrectActivity extends BaseActivity {

    public static void open(Context c, String type){
        Intent i = new Intent(c, CorrectActivity.class);
        i.putExtra(ConstantValue.correct_type, type);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.cstl_standard_gas)
    ConstraintLayout cstlStandardGas; //标准气

    @BindView(R.id.tv_standard_gas)
    TextView tvStandardGas; //标准气

    @BindView(R.id.tv_std_gas_read_value)
    TextView tvStdGasReadValue; //标准气读数

    @BindView(R.id.tv_ppm_threshold_value)
    TextView tvPpmThresholdValue; //PPM阈值

    @BindView(R.id.et_correct_read_value)
    EditText etCorrectReadValue; //校准读数

    @BindView(R.id.et_response_time)
    EditText etResponseTime; //响应时间

    @BindView(R.id.cstl_if_pass)
    ConstraintLayout cstlIfPass; //是否通过

    @BindView(R.id.tv_if_pass)
    TextView tvIfPass; //是否通过

    @BindView(R.id.tv_submit)
    TextView tvSubmit; //提交

    private String correctType;

    @Override
    protected int initLayout() {
        return R.layout.activity_drift_correct;
    }

    @Override
    protected void initView() {

//        bean = (AreaManageResultBean.Records) getIntent().getSerializableExtra(Constant.areaBean);
        correctType = getIntent().getStringExtra(ConstantValue.correct_type);
        // 0 校准设备(日常校准), 1 漂移校准
        if (!StringUtils.isNullString(correctType) && correctType.equals(ConstantValue.correct_type_0)){
            titleBar.setTitle(R.string.daily_correct);
        } else {
            titleBar.setTitle(R.string.drift_correct);
        }

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

    @OnClick({R.id.cstl_standard_gas, R.id.cstl_if_pass, R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cstl_standard_gas: // 标准气

                break;
            case R.id.cstl_if_pass: // 是否通过

                break;
            case R.id.tv_submit: // 提交

                break;
        }
    }
}