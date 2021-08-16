package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import butterknife.BindView;

/**
 * @Description: 组件详情
 * @Author: zzh
 * @CreateDate: 2021/8/15
 */
public class PartManageDetailActivity extends BaseActivity {

    public static void open(Context c){
        Intent i = new Intent(c, PartManageDetailActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    // 基本信息
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName; //装置

    @BindView(R.id.tv_area_code)
    TextView tvAreaCode; //区域编码

    @BindView(R.id.tv_equipment)
    TextView tvEquipment; //设备

    @BindView(R.id.tv_label_num)
    TextView tvLabelNum; //标签号

    @BindView(R.id.tv_extend_num)
    TextView tvExtendNum; //扩展号

    // 位置信息
    @BindView(R.id.tv_reference)
    TextView tvReference; //参考物

    @BindView(R.id.tv_orientation)
    TextView tvOrientation; //方向

    @BindView(R.id.tv_distance)
    TextView tvDistance; //距离

    @BindView(R.id.tv_floor)
    TextView tvFloor; //楼层

    @BindView(R.id.tv_height)
    TextView tvHeight; //高度

    @BindView(R.id.tv_additional_desc)
    TextView tvAdditionalDesc; //additional_desc

    // 主要信息
    @BindView(R.id.tv_seal_point)
    TextView tvSealPoint; //密封点

    @BindView(R.id.tv_seal_type)
    TextView tvSealType; //密封点子类型

    @BindView(R.id.tv_product_flow)
    TextView tvProductFlow; //产品流

    @BindView(R.id.tv_medium_status)
    TextView tvMediumStatus; //介质状态

    @BindView(R.id.tv_size)
    TextView tvSize; //尺寸

    @BindView(R.id.tv_main_medium)
    TextView tvMainMedium; //主要介质

    @BindView(R.id.tv_pid)
    TextView tvPid; //PID图号

    @BindView(R.id.tv_seal_material)
    TextView tvSealMaterial; //密封材质

    @BindView(R.id.tv_put_into_pro_date)
    TextView tvPutIntoProDate; //投产日期

    @BindView(R.id.tv_annual_run_time)
    TextView tvAnnualRunTime; //年运行时间

    @BindView(R.id.tv_manufacturer)
    TextView tvManufacturer; //生产厂家

    @BindView(R.id.tv_path_num)
    TextView tvPathNum; //路径号

    @BindView(R.id.tv_unreachable)
    TextView tvUnreachable; //不可达

    @BindView(R.id.tv_unreachable_reason)
    TextView tvUnreachableReason; //不可达原因

    @BindView(R.id.tv_keep_warm)
    TextView tvKeepWarm; //保温

    // 次要信息
    @BindView(R.id.tv_barcode)
    TextView tvBarcode; //条形码

    @BindView(R.id.tv_operate_temp)
    TextView tvOperateTemp; //操作温度

    @BindView(R.id.tv_operate_pressure)
    TextView tvOperatePressure; //操作压力

    @BindView(R.id.tv_change_manage_id)
    TextView tvChangeManageId; //变更管理ID

    @BindView(R.id.tv_replace_date)
    TextView tvReplaceDate; //替换日期

    @BindView(R.id.tv_control_sys)
    TextView tvControlSys; //控制设备和密闭通风系统

    @BindView(R.id.tv_control_type)
    TextView tvControlType; //控制设备和密闭通风类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_detail);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_area_manage;
    }

    @Override
    protected void initView() {

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
//        refresh();
    }
}