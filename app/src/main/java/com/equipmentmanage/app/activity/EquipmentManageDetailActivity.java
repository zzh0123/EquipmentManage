package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.ChemicalDetailBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.EquipmentManageResultBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 设备管理详情
 * @Author: zzh
 * @CreateDate: 2021/8/12
 */
public class EquipmentManageDetailActivity extends BaseActivity {

    public static void open(Context c, EquipmentManageResultBean.Records bean){
        Intent i = new Intent(c, EquipmentManageDetailActivity.class);
        i.putExtra(Constant.equipmentBean, bean);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_equipment_name)
    TextView tvEquipmentName; //名称

    @BindView(R.id.tv_equipment_code)
    TextView tvEquipmentCode; //编号

    @BindView(R.id.tv_belong_device)
    TextView tvBelongDevice; //所属装置

    @BindView(R.id.tv_belong_area)
    TextView tvBelongArea; //所属区域

    @BindView(R.id.tv_enabled)
    TextView tvEnabled; //是否可用

    @BindView(R.id.tv_order)
    TextView tvOrder; //顺序

    private String department, deviceType;

    private EquipmentManageResultBean.Records bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_equipment_manage_detail;
    }

    @Override
    protected void initView() {

        bean = (EquipmentManageResultBean.Records) getIntent().getSerializableExtra(Constant.equipmentBean);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (bean != null) {
            tvEquipmentName.setText(StringUtils.nullStrToEmpty(bean.getEquipmentName())); //名称
            tvEquipmentCode.setText(StringUtils.nullStrToEmpty(bean.getEquipmentCode())); //编号
            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getDeviceId_dictText())); //所属装置
            tvBelongArea.setText(StringUtils.nullStrToEmpty(bean.getAreaId_dictText())); //所属区域
//            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceCapacity())); //是否可用
//            tvOrder.setText(StringUtils.nullStrToEmpty(bean.getUseDate())); //顺序

        }

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

}