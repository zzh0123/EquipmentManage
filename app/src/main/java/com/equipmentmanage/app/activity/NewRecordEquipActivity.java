package com.equipmentmanage.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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
import com.equipmentmanage.app.view.EditAreaDialog;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: ????????????(????????????)-????????????
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
    TitleBar titleBar; //?????????

    @BindView(R.id.rvArea1)
    RecyclerView rvArea1;
    private NewAreaAdapter adapterArea;
    private List<NewAreaBean> newAreaBeanList = new ArrayList<>();


    @BindView(R.id.rvEquip)
    SwipeRecyclerView rvEquip;
    private NewEquipAdapter adapterEquip;
    private List<NewEquipBean> newEquipBeanList = new ArrayList<>();

    private AddAreaDialog addAreaDialog, addAreaDialog1;

    private String areaCode, areaName;
    private String deviceCode, deviceName, deviceType, deviceId;
    private int pos;

    private TipDialog tipDialog;
    private NewEquipBean bean;

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
                return "????????????";
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

        tipDialog = new TipDialog(this);
        tipDialog.setOnConfirmListener(new TipDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                // ??????
                delete();
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
                    Toasty.warning(NewRecordEquipActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (!validCode(code)){
                    Toasty.warning(NewRecordEquipActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
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
                    Toasty.success(NewRecordEquipActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                    readEquipAreaCache();
                } else {
                    Toasty.error(NewRecordEquipActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                }
                addAreaDialog.dismiss();
            }
        });

        addAreaDialog1 = new AddAreaDialog(this);
        addAreaDialog1.setOnConfirmListener(new AddAreaDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String code, String name) {
//                List<TaskBean> list = DaoUtilsStore.getInstance().getUserDaoUtils().queryAll();
//                L.i("zzz1--->size--" + list.size());
                L.i("zzz1--->code--" + code + "--name--"+ name);
                if (StringUtils.isNullOrEmpty(areaCode)){
                    Toasty.warning(NewRecordEquipActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
                    return;
                }

//                if (!validCode(code)){
//                    Toasty.warning(NewRecordEquipActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
//                    return;
//                }

                // String equipCode, String equipName, String deviceCode, String areaCode
                int rowId = AppDatabase.getInstance(NewRecordEquipActivity.this)
                        .newEquipTableDao()
                        .updateByEquipCode(code, name, deviceCode, areaCode);
                if (rowId >= 0){
                    Toasty.success(NewRecordEquipActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                  // String equipCode, String equipName, String deviceCode, String areaCode, String oldEquipCode
                    int row = AppDatabase.getInstance(NewRecordEquipActivity.this)
                            .imgTableDao1()
                            .updateByEquipCode(code, name, deviceCode, areaCode, bean.getCode());
                    readEquipAreaCache();
                } else {
                    Toasty.error(NewRecordEquipActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                }
                addAreaDialog1.dismiss();
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

                    // ?????????????????????
//                    SealPointOnRecordActivity1.open(NewRecordEquipActivity.this,
//                            deviceCode, deviceName, deviceType, deviceId,
//                            areaCode, areaName,
//                            equipCode, equipName);

                    // ??????????????????
                    NewRecordTagActivity.open(NewRecordEquipActivity.this, newEquipBeanList, position,
                            deviceCode, deviceName, deviceType, deviceId,
                            areaCode, areaName,
                            equipCode, equipName);
                }

            }
        });
        // ??????????????????
        rvEquip.setSwipeMenuCreator(mSwipeMenuCreator);
        // ?????????????????????
        rvEquip.setOnItemMenuClickListener(mItemMenuClickListener);
        rvEquip.setAdapter(adapterEquip);

        // ???????????????
        areaCode = newAreaBeanList.get(pos).getCode();
        areaName = newAreaBeanList.get(pos).getName();
        readEquipAreaCache();

    }

    // ???????????????
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(NewRecordEquipActivity.this);
            deleteItem.setText("??????");
            deleteItem.setTextSize(16);
            deleteItem.setTextColor(getResources().getColor(R.color.white));
            deleteItem.setWidth(200);
            deleteItem.setHeight(MATCH_PARENT);
            deleteItem.setBackgroundColor(getResources().getColor(R.color.c_F56C6C));
            // ????????????????????????????????????
            rightMenu.addMenuItem(deleteItem); // ???Item???????????????????????????

            SwipeMenuItem editItem = new SwipeMenuItem(NewRecordEquipActivity.this);
            editItem.setText("??????");
            editItem.setTextSize(16);
            editItem.setTextColor(getResources().getColor(R.color.white));
            editItem.setWidth(200);
            editItem.setHeight(MATCH_PARENT);
            editItem.setBackgroundColor(getResources().getColor(R.color.c_409EFF));
            // ????????????????????????????????????
            rightMenu.addMenuItem(editItem); // ???Item???????????????????????????
            // ????????????????????????????????????????????????????????????
        }
    };

    OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            // ??????????????????????????????????????????????????????Item???????????????????????????
            menuBridge.closeMenu();

            // ???????????????????????????
            int direction = menuBridge.getDirection();
            // ?????????Item??????Position???
            int menuPosition = menuBridge.getPosition();

            bean = newEquipBeanList.get(position);
            if (bean != null) {
                clearChecked1();
                bean.setSelected(true);
                adapterEquip.notifyDataSetChanged();

                if (menuPosition == 0) {
                    showDeleteDialog();
                }else if (menuPosition == 1) {
                        String equipCode = bean.getCode();
                        String equipName = bean.getName();
                    showEditAreaDialog(equipCode, equipName);
                }
            }
        }
    };

    private void edit(){

    }

    private void showEditAreaDialog(String equipCode, String equipName){
        if (addAreaDialog1 == null) {
            addAreaDialog1 = new AddAreaDialog(this);
        }
        addAreaDialog1.show();
        addAreaDialog1.setTitle("????????????");
        addAreaDialog1.clearView();
        addAreaDialog1.setCodeName(equipCode, equipName);
    }

    private void showDeleteDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
        tipDialog.show();
        tipDialog.setTitleAndTip(null, getString(R.string.delete_tip));
    }



    private void delete(){
        String equipCode = bean.getCode();
        int rowCount = AppDatabase.getInstance(NewRecordEquipActivity.this)
                .newEquipTableDao()
                .deleteByEquipCode(deviceCode, areaCode, equipCode);
//                        L.i("zzz1--rowCount->" + rowCount);
        if (rowCount > 0) {
            Toasty.success(NewRecordEquipActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
            readEquipAreaCache();

            int rowCountTag = AppDatabase.getInstance(NewRecordEquipActivity.this)
                    .imgTableDao1()
                    .deleteByEquipCode(deviceId, areaCode, equipCode);
        } else {
            Toasty.error(NewRecordEquipActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
        }
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
        addAreaDialog.setTitle("????????????");
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
            Toasty.warning(NewRecordEquipActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();

        }
        adapterEquip.notifyDataSetChanged();

    }
}