package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.NewAreaAdapter;
import com.equipmentmanage.app.adapter.NewDeviceAdapter;
import com.equipmentmanage.app.adapter.NewEquipAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.bean.NewAreaTableBean;
import com.equipmentmanage.app.bean.NewEquipBean;
import com.equipmentmanage.app.bean.NewEquipTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.AddAreaDialog;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 新建建档(组件管理)-新建设备
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
public class NewRecordEquipActivity extends BaseActivity {

    public static void open(Context c, List<NewAreaBean> newAreaBeanList, int pos,
                            String deviceCode, String deviceName, String deviceType, String deviceId) {
        Intent i = new Intent(c, NewRecordEquipActivity.class);
        i.putExtra(Constant.newAreaBeanList, (Serializable) newAreaBeanList);
        i.putExtra(Constant.pos, pos);
        i.putExtra(Constant.deviceCode, deviceCode);
        i.putExtra(Constant.deviceName, deviceName);
        i.putExtra(Constant.deviceType, deviceType);
        i.putExtra(Constant.deviceId, deviceId);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.rvArea1)
    RecyclerView rvArea1;
    private NewAreaAdapter adapterArea;
    private List<NewAreaBean> newAreaBeanList = new ArrayList<>();


    @BindView(R.id.rvEquip)
    RecyclerView rvEquip;
    private NewEquipAdapter adapterEquip;
    private List<NewEquipBean> newEquipBeanList = new ArrayList<>();

    private AddAreaDialog addAreaDialog;

    private String areaCode, areaName;
    private String deviceCode, deviceName, deviceType, deviceId;
    private int pos;

    @Override
    protected int initLayout() {
        return R.layout.activity_new_record_equip;
    }

    @Override
    protected void initView() {
        pos = getIntent().getIntExtra(Constant.pos, 0);
        newAreaBeanList = (List<NewAreaBean>)getIntent().getSerializableExtra(Constant.newAreaBeanList);
        L.i("zzz1--->newAreaBeanList--" + newAreaBeanList.size());
        deviceCode = getIntent().getStringExtra(Constant.deviceCode);
        deviceName = getIntent().getStringExtra(Constant.deviceName);
        deviceType = getIntent().getStringExtra(Constant.deviceType);
        deviceId = getIntent().getStringExtra(Constant.deviceId);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return "新增设备";
            }

            @Override
            public int getDrawable() {
                return 0;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->1111");
                showAddAreaDialog();
            }

            @Override
            public int leftPadding() {
                return 0;
            }

            @Override
            public int rightPadding() {
                return 0;
            }
        });

        addAreaDialog = new AddAreaDialog(this);
        addAreaDialog.setOnConfirmListener(new AddAreaDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String code, String name) {
//                List<TaskBean> list = DaoUtilsStore.getInstance().getUserDaoUtils().queryAll();
//                L.i("zzz1--->size--" + list.size());
                L.i("zzz1--->code--" + code + "--name--"+ name);
                if (StringUtils.isNullOrEmpty(areaCode)){
                    Toasty.warning(NewRecordEquipActivity.this, "请先选择区域！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (!validCode(code)){
                    Toasty.warning(NewRecordEquipActivity.this, "编号不能重复！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                long currentMills =  System.currentTimeMillis();
                NewEquipTableBean newEquipTableBean = new NewEquipTableBean();
                newEquipTableBean.id = currentMills;
                newEquipTableBean.code = code;
                newEquipTableBean.name = name;
                newEquipTableBean.areaCode = areaCode;
                newEquipTableBean.deviceCode = deviceCode;

                Long rowId = AppDatabase.getInstance(NewRecordEquipActivity.this).newEquipTableDao().insert(newEquipTableBean);
                if (rowId != null){
                    Toasty.success(NewRecordEquipActivity.this, "新增成功！", Toast.LENGTH_SHORT, true).show();
                    readEquipAreaCache();
                } else {
                    Toasty.error(NewRecordEquipActivity.this, "新增失败！", Toast.LENGTH_SHORT, true).show();
                }
                addAreaDialog.dismiss();
            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvArea1.setLayoutManager(manager);
        adapterArea = new NewAreaAdapter(newAreaBeanList);
        adapterArea.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                NewAreaBean bean = newAreaBeanList.get(position);
                if (bean != null) {
                    clearChecked();
                    areaCode = bean.getCode();
                    areaName = bean.getName();
                    bean.setSelected(true);
                    adapterArea.notifyDataSetChanged();

                    readEquipAreaCache();
                }

            }
        });
        rvArea1.setAdapter(adapterArea);

        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEquip.setLayoutManager(manager1);
        adapterEquip = new NewEquipAdapter(newEquipBeanList);
        adapterEquip.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                NewEquipBean bean = newEquipBeanList.get(position);
                if (bean != null) {
                    clearChecked1();
                    bean.setSelected(true);
                    adapterEquip.notifyDataSetChanged();

                    String equipCode = bean.getCode();
                    String equipName = bean.getName();

                    // 跳转密封点建档
//                    SealPointOnRecordActivity1.open(NewRecordEquipActivity.this,
//                            deviceCode, deviceName, deviceType, deviceId,
//                            areaCode, areaName,
//                            equipCode, equipName);

                    // 跳转标签管理
                    NewRecordTagActivity.open(NewRecordEquipActivity.this, newEquipBeanList, position,
                            deviceCode, deviceName, deviceType, deviceId,
                            areaCode, areaName,
                            equipCode, equipName);
                }

            }
        });
        rvEquip.setAdapter(adapterEquip);

        // 初始化数据
        areaCode = newAreaBeanList.get(pos).getCode();
        areaName = newAreaBeanList.get(pos).getName();
        readEquipAreaCache();

    }

    private boolean validCode(String code){
        boolean isCanUse = true;
        if (newEquipBeanList != null && newEquipBeanList.size() > 0){
            for (int i = 0 ; i < newEquipBeanList.size(); i++){
                if (newEquipBeanList.get(i).getCode().equals(code)){
                    isCanUse = false;
                    break;
                }
            }
        }
        return isCanUse;
    }

    private void clearChecked() {
        for (int i = 0; i < newAreaBeanList.size(); i++) {
            newAreaBeanList.get(i).setSelected(false);
        }

    }

    private void clearChecked1() {
        for (int i = 0; i < newEquipBeanList.size(); i++) {
            newEquipBeanList.get(i).setSelected(false);
        }

    }

    private void showAddAreaDialog(){
        if (addAreaDialog == null) {
            addAreaDialog = new AddAreaDialog(this);
        }
        addAreaDialog.show();
        addAreaDialog.setTitle("新增设备");
        addAreaDialog.clearView();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    
    private void readEquipAreaCache() {
        newEquipBeanList.clear();
        List<NewEquipTableBean> list = AppDatabase.getInstance(NewRecordEquipActivity.this).newEquipTableDao().loadAllByDeviceAndAreaCode(deviceCode, areaCode);
        L.i("zzz1-newEquipBeanList->" + newEquipBeanList.size());
        if (list != null && list.size() > 0) {
            for (NewEquipTableBean newAreaTableBean: list) {
                NewEquipBean bean = new NewEquipBean();
                bean.setCode(newAreaTableBean.code);
                bean.setName(newAreaTableBean.name);
                newEquipBeanList.add(bean);
            }


        } else {
            Toasty.warning(NewRecordEquipActivity.this, "设备列表为空！", Toast.LENGTH_SHORT, true).show();

        }
        adapterEquip.notifyDataSetChanged();

    }
}