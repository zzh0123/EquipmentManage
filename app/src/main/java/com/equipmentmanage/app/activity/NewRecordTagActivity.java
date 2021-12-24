package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.NewAreaAdapter;
import com.equipmentmanage.app.adapter.NewEquipAdapter;
import com.equipmentmanage.app.adapter.NewTagAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.ImgTableBean1;
import com.equipmentmanage.app.bean.NewAreaBean;
import com.equipmentmanage.app.bean.NewEquipBean;
import com.equipmentmanage.app.bean.NewEquipTableBean;
import com.equipmentmanage.app.bean.NewTagBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.view.AddAreaDialog;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

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
public class NewRecordTagActivity extends BaseActivity {

    public static void open(Context c, List<NewEquipBean> newEquipBeanList, int pos, String deviceCode, String deviceName, String deviceType, String deviceId,
                            String areaCode, String areaName,
                            String equipCode, String equipName) {
        Intent i = new Intent(c, NewRecordTagActivity.class);
        i.putExtra(Constant.newEquipBeanList, (Serializable) newEquipBeanList);
        i.putExtra(Constant.pos, pos);
        i.putExtra(Constant.deviceCode, deviceCode);
        i.putExtra(Constant.deviceName, deviceName);
        i.putExtra(Constant.deviceType, deviceType);
        i.putExtra(Constant.deviceId, deviceId);

        i.putExtra(Constant.areaCode, areaCode);
        i.putExtra(Constant.areaName, areaName);
        i.putExtra(Constant.equipCode, equipCode);
        i.putExtra(Constant.equipName, equipName);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏


    @BindView(R.id.rvEquip1)
    RecyclerView rvEquip1;
    private NewEquipAdapter adapterEquip;
    private List<NewEquipBean> newEquipBeanList = new ArrayList<>();


    @BindView(R.id.rvTag)
    RecyclerView rvTag;
    private NewTagAdapter adapterNewTag;
    private List<NewTagBean> newTagBeanList = new ArrayList<>();


    private AddAreaDialog addAreaDialog;

    private String equipCode, equipName;
    private String deviceCode, deviceName, deviceType, deviceId;
    private String areaCode, areaName;
    private int pos;

    private String currentDate;
    private List<ImgTableBean1> list;

    @Override
    protected int initLayout() {
        return R.layout.activity_new_record_tag;
    }

    @Override
    protected void initView() {
        // 注册订阅者
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

        pos = getIntent().getIntExtra(Constant.pos, 0);
        newEquipBeanList = (List<NewEquipBean>) getIntent().getSerializableExtra(Constant.newEquipBeanList);
        L.i("zzz1--->newEquipBeanList--" + newEquipBeanList.size());
        deviceCode = getIntent().getStringExtra(Constant.deviceCode);
        deviceName = getIntent().getStringExtra(Constant.deviceName);
        deviceType = getIntent().getStringExtra(Constant.deviceType);
        deviceId = getIntent().getStringExtra(Constant.deviceId);

        areaCode = getIntent().getStringExtra(Constant.areaCode);
        areaName = getIntent().getStringExtra(Constant.areaName);

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
//                return null;
                return "新增建档";
            }

            @Override
            public int getDrawable() {
//                return R.mipmap.ic_add_image;
                return 0;
            }

            @Override
            public void performAction(View view) {
                L.i("zzz1--->1111");
                SealPointOnRecordActivity1.open(NewRecordTagActivity.this, true,null,
                        deviceCode, deviceName, deviceType, deviceId,
                        areaCode, areaName,
                        equipCode, equipName);
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
                L.i("zzz1--->code--" + code + "--name--" + name);
                if (StringUtils.isNullOrEmpty(areaCode)) {
                    Toasty.warning(NewRecordTagActivity.this, "请先选择区域！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (!validCode(code)) {
                    Toasty.warning(NewRecordTagActivity.this, "编号不能重复！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                long currentMills = System.currentTimeMillis();
                NewEquipTableBean newEquipTableBean = new NewEquipTableBean();
                newEquipTableBean.id = currentMills;
                newEquipTableBean.code = code;
                newEquipTableBean.name = name;
                newEquipTableBean.areaCode = areaCode;
                newEquipTableBean.deviceCode = deviceCode;

                Long rowId = AppDatabase.getInstance(NewRecordTagActivity.this).newEquipTableDao().insert(newEquipTableBean);
                if (rowId != null) {
                    Toasty.success(NewRecordTagActivity.this, "新增成功！", Toast.LENGTH_SHORT, true).show();
                    readTagCache();
                } else {
                    Toasty.error(NewRecordTagActivity.this, "新增失败！", Toast.LENGTH_SHORT, true).show();
                }
                addAreaDialog.dismiss();
            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvEquip1.setLayoutManager(manager);
        adapterEquip = new NewEquipAdapter(newEquipBeanList);
        adapterEquip.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                NewEquipBean bean = newEquipBeanList.get(position);
                if (bean != null) {
                    clearChecked();
                    equipCode = bean.getCode();
                    equipName = bean.getName();
                    bean.setSelected(true);
                    adapterEquip.notifyDataSetChanged();

                    readTagCache();
                }

            }
        });
        rvEquip1.setAdapter(adapterEquip);

        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTag.setLayoutManager(manager1);
        adapterNewTag = new NewTagAdapter(newTagBeanList);
        adapterNewTag.addChildClickViewIds(R.id.tv_delete, R.id.tv_edit);
        adapterNewTag.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                int id = view.getId();
                NewTagBean bean = newTagBeanList.get(position);
                if (bean != null) {
                    clearChecked1();
                    bean.setSelected(true);
                    adapterNewTag.notifyDataSetChanged();

                    if (id == R.id.tv_edit) {
//                        String tagNum = bean.getTagNum();
//                        String equipName = bean.getName();
                        ImgTableBean1 imgTableBean1 = list.get(position);
                        // 跳转密封点建档-编辑
                        SealPointOnRecordActivity1.open(NewRecordTagActivity.this, false, imgTableBean1,
                                deviceCode, deviceName, deviceType, deviceId,
                                areaCode, areaName,
                                equipCode, equipName);
                    } else if (id == R.id.tv_delete){
                        String tagNum = bean.getTagNum();
                        int rowCount = AppDatabase.getInstance(NewRecordTagActivity.this)
                                .imgTableDao1()
                                .deleteByTagNum(currentDate, deviceId, areaCode, equipCode, tagNum);
//                        L.i("zzz1--rowCount->" + rowCount);
                        if (rowCount > 0) {
                            Toasty.success(NewRecordTagActivity.this, "删除成功！", Toast.LENGTH_SHORT, true).show();
                            readTagCache();
                        } else {
                            Toasty.error(NewRecordTagActivity.this, "删除失败！", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }
            }
        });
        adapterNewTag.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                int id = view.getId();
                NewTagBean bean = newTagBeanList.get(position);
                if (bean != null) {
                    clearChecked1();
                    bean.setSelected(true);
                    adapterNewTag.notifyDataSetChanged();

//                    if (id == R.id.tv_edit) {
//                        String tagNum = bean.getTagNum();
////                        String equipName = bean.getName();
//                        ImgTableBean1 imgTableBean1 = list.get(position);
//                        // 跳转密封点建档-编辑
//                        SealPointOnRecordActivity1.open(NewRecordTagActivity.this, false, imgTableBean1,
//                                deviceCode, deviceName, deviceType, deviceId,
//                                areaCode, areaName,
//                                equipCode, equipName);
//                    }
                }

            }
        });

        rvTag.setAdapter(adapterNewTag);

        // 初始化数据
        equipCode = newEquipBeanList.get(pos).getCode();
        equipName = newEquipBeanList.get(pos).getName();
        readTagCache();

    }

    private boolean validCode(String code) {
        boolean isCanUse = true;
        if (newEquipBeanList != null && newEquipBeanList.size() > 0) {
            for (int i = 0; i < newEquipBeanList.size(); i++) {
                if (newEquipBeanList.get(i).getCode().equals(code)) {
                    isCanUse = false;
                    break;
                }
            }
        }
        return isCanUse;
    }

    private void clearChecked() {
        for (int i = 0; i < newEquipBeanList.size(); i++) {
            newEquipBeanList.get(i).setSelected(false);
        }

    }

    private void clearChecked1() {
        for (int i = 0; i < newTagBeanList.size(); i++) {
            newTagBeanList.get(i).setSelected(false);
        }

    }

    private void showAddAreaDialog() {
        if (addAreaDialog == null) {
            addAreaDialog = new AddAreaDialog(this);
        }
        addAreaDialog.show();
        addAreaDialog.setTitle("新增设备");
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }


    private void readTagCache() {
        currentDate = DateUtil.getCurentTime1();
        newTagBeanList.clear();
        list = AppDatabase.getInstance(NewRecordTagActivity.this)
                .imgTableDao1()
                .loadByDateAndEquip(currentDate, deviceId, areaCode, equipCode);
//        L.i("zzz1-list->" + list.size());
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++){
                ImgTableBean1 imgTableBean1 = list.get(i);
                NewTagBean bean = new NewTagBean();
                bean.setTagNum(imgTableBean1.tagNum);
                if (StringUtils.isNullOrEmpty(imgTableBean1.localPath)){
                    bean.setCount("0");
                } else {
                    bean.setCount("1");
                }
                bean.setLocalPath(imgTableBean1.localPath);
                L.i("zzz1-tagNum->" + imgTableBean1.tagNum);
                newTagBeanList.add(bean);
            }
//            for (ImgTableBean1 imgTableBean1 : list) {
//
//            }


        } else {
            Toasty.warning(NewRecordTagActivity.this, "标签列表为空！", Toast.LENGTH_SHORT, true).show();

        }
        adapterNewTag.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //订阅事件
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(String event) {
        L.i("zzz1-event-->", "" + event);
//        //接收以及处理数据
        if (!StringUtils.isNullOrEmpty(event)) {
            if (ConstantValue.event_photo_save.equals(event)) {
                readTagCache();
            }
        }
    }
}