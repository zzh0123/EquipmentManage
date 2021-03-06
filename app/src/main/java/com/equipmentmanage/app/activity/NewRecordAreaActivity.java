package com.equipmentmanage.app.activity;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.AreaAdapter;
import com.equipmentmanage.app.adapter.DeviceAdapter;
import com.equipmentmanage.app.adapter.NewAreaAdapter;
import com.equipmentmanage.app.adapter.NewDeviceAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BaseAreaTableBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.ImgTableBean1;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.bean.NewAreaTableBean;
import com.equipmentmanage.app.bean.NewTagBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.AddAreaDialog;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: ????????????(????????????)-????????????
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
public class NewRecordAreaActivity extends BaseActivity {

    public static void open(Context c) {
        Intent i = new Intent(c, NewRecordAreaActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //?????????

    @BindView(R.id.rvDevice)
    RecyclerView rvDevice;
    private NewDeviceAdapter adapterDevice;
    private List<BaseDeviceBean> baseDeviceBeanList = new ArrayList<>();
    private List<DeviceManageResultBean.Records> mListDevice = new ArrayList<>();


    @BindView(R.id.rvArea)
    SwipeRecyclerView rvArea;
    private NewAreaAdapter adapterArea;
    private List<NewAreaBean> newAreaBeanList = new ArrayList<>();

    private AddAreaDialog addAreaDialog;

    private String deviceCode, deviceName, deviceType, deviceId;

    private TipDialog tipDialog;
    private NewAreaBean bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_new_record;
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

        titleBar.addAction(new TitleBar.Action() {
            @Override
            public String getText() {
                return null;
            }

            @Override
            public int getDrawable() {
                return R.mipmap.ic_download;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->download");
                getBaseDeviceList();
            }

            @Override
            public int leftPadding() {
                return 0;
            }

            @Override
            public int rightPadding() {
                return 20;
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
                return 20;
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
                if (StringUtils.isNullOrEmpty(deviceCode)){
                    Toasty.warning(NewRecordAreaActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (!validCode(code)){
                    Toasty.warning(NewRecordAreaActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                long currentMills =  System.currentTimeMillis();
                NewAreaTableBean newAreaTableBean = new NewAreaTableBean();
                newAreaTableBean.id = currentMills;
                newAreaTableBean.code = code;
                newAreaTableBean.name = name;
                newAreaTableBean.deviceCode = deviceCode;
                Long rowId = AppDatabase.getInstance(NewRecordAreaActivity.this).newAreaTableDao().insert(newAreaTableBean);
                if (rowId != null){
                    Toasty.success(NewRecordAreaActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                    readNewAreaCache();
                } else {
                    Toasty.error(NewRecordAreaActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
                }
                addAreaDialog.dismiss();
            }
        });
        

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDevice.setLayoutManager(manager);
        adapterDevice = new NewDeviceAdapter(baseDeviceBeanList);
        adapterDevice.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BaseDeviceBean bean = baseDeviceBeanList.get(position);
                if (bean != null) {
                    clearChecked();
                    deviceCode = bean.getDeviceCode();
                    deviceName = bean.getDeviceName();
                    deviceType = bean.getDeviceType();
                    deviceId = bean.getId();
                    bean.setSelected(true);
                    adapterDevice.notifyDataSetChanged();
                    L.i("zzz1---deviceCode>" + deviceCode);
                    readNewAreaCache();
                }

            }
        });
        rvDevice.setAdapter(adapterDevice);

        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvArea.setLayoutManager(manager1);
        adapterArea = new NewAreaAdapter(newAreaBeanList);
        adapterArea.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                NewAreaBean bean = newAreaBeanList.get(position);
                if (bean != null) {
                    // ??????????????????
                    clearChecked1();
                    bean.setSelected(true);
                    adapterArea.notifyDataSetChanged();

                    NewRecordEquipActivity.open(NewRecordAreaActivity.this, newAreaBeanList, position,
                            deviceCode, deviceName, deviceType, deviceId);
                }

            }
        });

        // ??????????????????
        rvArea.setSwipeMenuCreator(mSwipeMenuCreator);
        // ?????????????????????
        rvArea.setOnItemMenuClickListener(mItemMenuClickListener);
        rvArea.setAdapter(adapterArea);


    }

    // ???????????????
    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
            SwipeMenuItem deleteItem = new SwipeMenuItem(NewRecordAreaActivity.this);
            deleteItem.setText("??????");
            deleteItem.setTextSize(16);
            deleteItem.setTextColor(getResources().getColor(R.color.white));
            deleteItem.setWidth(200);
            deleteItem.setHeight(MATCH_PARENT);
            deleteItem.setBackgroundColor(getResources().getColor(R.color.c_F56C6C));
            // ????????????????????????????????????
            rightMenu.addMenuItem(deleteItem); // ???Item???????????????????????????

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

            bean = newAreaBeanList.get(position);
            if (bean != null) {
                clearChecked1();
                bean.setSelected(true);
                adapterArea.notifyDataSetChanged();

                if (menuPosition == 0) {
                    showDeleteDialog();
                }
            }
        }
    };

    private void showDeleteDialog() {
        if (tipDialog == null) {
            tipDialog = new TipDialog(this);
        }
        tipDialog.show();
        tipDialog.setTitleAndTip(null, getString(R.string.delete_tip));
    }

    private void delete(){
        String areaCode = bean.getCode();
        int rowCount = AppDatabase.getInstance(NewRecordAreaActivity.this)
                .newAreaTableDao()
                .deleteByAreaCode(deviceCode, areaCode);
//                        L.i("zzz1--rowCount->" + rowCount);
        if (rowCount > 0) {
            Toasty.success(NewRecordAreaActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
            readNewAreaCache();

            int rowCountEquip = AppDatabase.getInstance(NewRecordAreaActivity.this)
                    .newEquipTableDao()
                    .deleteByAreaCode(deviceCode, areaCode);

            int rowCountTag = AppDatabase.getInstance(NewRecordAreaActivity.this)
                    .imgTableDao1()
                    .deleteByAreaCode(deviceId, areaCode);
        } else {
            Toasty.error(NewRecordAreaActivity.this, "???????????????", Toast.LENGTH_SHORT, true).show();
        }
    }

    private boolean validCode(String code){
        boolean isCanUse = true;
        if (newAreaBeanList != null && newAreaBeanList.size() > 0){
            for (int i = 0 ; i < newAreaBeanList.size(); i++){
                if (newAreaBeanList.get(i).getCode().equals(code)){
                    isCanUse = false;
                    break;
                }
            }
        }
        return isCanUse;
    }

    private void clearChecked() {
        for (int i = 0; i < baseDeviceBeanList.size(); i++) {
            baseDeviceBeanList.get(i).setSelected(false);
        }

    }

    private void clearChecked1() {
        for (int i = 0; i < newAreaBeanList.size(); i++) {
            newAreaBeanList.get(i).setSelected(false);
        }

    }

    private void showAddAreaDialog(){
        if (addAreaDialog == null) {
            addAreaDialog = new AddAreaDialog(this);
        }
        addAreaDialog.show();
        addAreaDialog.clearView();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        readDeviceCache();
    }

    private void readDeviceCache() {
        baseDeviceBeanList.clear();
        BaseDeviceTableBean list = AppDatabase.getInstance(NewRecordAreaActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseDeviceBean> baseDeviceBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseDeviceBean>>() {
            }.getType());
            L.i("zzz1-baseDeviceBeans.list222--->" + baseDeviceBeans.size());


            if (baseDeviceBeans != null && baseDeviceBeans.size() > 0) {
                baseDeviceBeanList.addAll(baseDeviceBeans);
                baseDeviceBeanList.get(0).setSelected(true);
                deviceCode = baseDeviceBeanList.get(0).getDeviceCode();
                deviceName = baseDeviceBeanList.get(0).getDeviceName();
                deviceType = baseDeviceBeanList.get(0).getDeviceType();
                deviceId = baseDeviceBeanList.get(0).getId();
                readNewAreaCache();

            } else {
                Toasty.warning(NewRecordAreaActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

            }
        } else {
            Toasty.normal(NewRecordAreaActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        }
        adapterDevice.notifyDataSetChanged();

    }

    /**
     * ??????????????????
     */
    private void getBaseDeviceList() {
        Subscribe.getBaseDeviceList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //??????
                try {
                    BaseBean<List<BaseDeviceBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseDeviceBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseDeviceBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseDeviceTableBean baseDeviceTableBean = new BaseDeviceTableBean();
                                baseDeviceTableBean.id = "1";
                                baseDeviceTableBean.content = GsonUtils.toJson(dataList);
                                Long rowId = AppDatabase.getInstance(NewRecordAreaActivity.this).baseDeviceTableDao().insert(baseDeviceTableBean);
                                if (rowId != null){
                                    Toasty.success(NewRecordAreaActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();
                                    readDeviceCache();
                                } else {
                                    Toasty.error(NewRecordAreaActivity.this, R.string.download_fail, Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                int row = AppDatabase.getInstance(NewRecordAreaActivity.this).baseDeviceTableDao().deleteAll();
//                                    L.i("zzz1-del-row-->" + row);
                                if (row >= 0){
                                    mListDevice.clear();
                                    adapterDevice.notifyDataSetChanged();
                                }
                                Toasty.error(NewRecordAreaActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(NewRecordAreaActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(NewRecordAreaActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(NewRecordAreaActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(NewRecordAreaActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, NewRecordAreaActivity.this));
    }


    private void readNewAreaCache() {
        newAreaBeanList.clear();
        List<NewAreaTableBean> list = AppDatabase.getInstance(NewRecordAreaActivity.this).newAreaTableDao().loadAllByDeviceCode(deviceCode);
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
        if (list != null && list.size() > 0) {
            for (NewAreaTableBean newAreaTableBean: list) {
                NewAreaBean bean = new NewAreaBean();
                bean.setCode(newAreaTableBean.code);
                bean.setName(newAreaTableBean.name);
                newAreaBeanList.add(bean);
            }


        } else {
            Toasty.warning(NewRecordAreaActivity.this, "?????????????????????", Toast.LENGTH_SHORT, true).show();

        }
        adapterArea.notifyDataSetChanged();

    }
}