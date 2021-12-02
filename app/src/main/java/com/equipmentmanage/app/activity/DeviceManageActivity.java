package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.DeviceAdapter;
import com.equipmentmanage.app.adapter.DeviceTypeAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.DeviceTableBean;
import com.equipmentmanage.app.bean.DictItemBean;
import com.equipmentmanage.app.bean.TaskTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 装置管理
 * @Author: zzh
 * @CreateDate: 2021/8/10
 */
public class DeviceManageActivity extends BaseActivity {

    public static void open(Context c) {
        Intent i = new Intent(c, DeviceManageActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.iv_search)
    ImageView ivSearch; //搜索

    @BindView(R.id.et_search)
    EditText etSearch; //搜索

    @BindView(R.id.imb_clear)
    ImageButton imbClear; //清除

    @BindView(R.id.ll_department)
    LinearLayout llDepartment; //部门

    @BindView(R.id.tv_department)
    TextView tvDepartment; //部门

    @BindView(R.id.ll_device_type)
    LinearLayout llDeviceType; //装置类型

    @BindView(R.id.tv_device_type)
    TextView tvDeviceType; //装置类型

    //选择装置类型
    private ListPopupWindow departmentPopupWindow;
    private String departmentName = "";
    private String departmentValue = "";
    private List<DepartmentBean> departmentBeanList = new ArrayList<>();
    private DepartmentAdapter departmentAdapter;

    //选择装置类型
    private ListPopupWindow devicePopupWindow;
    private String deviceTypeName = "";

    private String deviceTypeValue = "";

    private List<DictItemBean> deviceTypeBeanList = new ArrayList<>();
    private DeviceTypeAdapter deviceTypeAdapter;

    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private DeviceAdapter adapter;
    private List<DeviceManageResultBean.Records> mList = new ArrayList<>();

    private List<BaseDeviceBean> baseDeviceBeanList = new ArrayList<>();

    List<DeviceTableBean> deviceTableBeans = new ArrayList<>();

    private int pageNo = 1, pageSize = 10;

    private String department, deviceType;

    @Override
    public void onAttachedToWindow() {
        setShowTitle(false);
        super.onAttachedToWindow();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_device_manage;
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
                refresh();
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

        srl.setEnableRefresh(false);
        srl.setEnableLoadMore(false);
        srl.setEnableFooterFollowWhenNoMoreData(true);

        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });
        srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(manager);
        adapter = new DeviceAdapter(baseDeviceBeanList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BaseDeviceBean bean = baseDeviceBeanList.get(position);
                if (bean != null) {
                    DeviceManageDetailActivity.open(DeviceManageActivity.this, bean);
                }

            }
        });
        adapter.setEmptyView(getEmptyDataView());
        rvList.setAdapter(adapter);

        departmentPopupWindow = new ListPopupWindow(this);
        departmentPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        departmentPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        departmentPopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        departmentPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        departmentPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        departmentPopupWindow.setAnchorView(llDepartment);
        departmentPopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        departmentPopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        departmentPopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        departmentAdapter = new DepartmentAdapter(this, departmentBeanList);
        departmentPopupWindow.setAdapter(departmentAdapter);


        devicePopupWindow = new ListPopupWindow(this);
        devicePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        devicePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        devicePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        devicePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        devicePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        devicePopupWindow.setAnchorView(llDeviceType);
        devicePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        devicePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        devicePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        deviceTypeAdapter = new DeviceTypeAdapter(this, deviceTypeBeanList);
        devicePopupWindow.setAdapter(deviceTypeAdapter);

    }

    @Override
    protected void initEvent() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    L.i("zzz1--->etSearch");
                    readCache();
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() > 0) {
                    imbClear.setVisibility(View.VISIBLE);
                } else {
                    imbClear.setVisibility(View.INVISIBLE);
                }
            }
        });

        departmentPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentBean bean = departmentBeanList.get(position);
                if (bean != null) {
                    departmentValue = bean.getValue();
                    tvDepartment.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }
                departmentPopupWindow.dismiss();
            }
        });

        devicePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DictItemBean bean = deviceTypeBeanList.get(position);
                if (bean != null) {
                    deviceTypeValue = bean.getValue();
                    tvDeviceType.setText(StringUtils.nullStrToEmpty(bean.getText()));
                    refresh();
                }
                devicePopupWindow.dismiss();
            }
        });
    }

    @SingleClick
    @OnClick({R.id.imb_clear, R.id.ll_department, R.id.ll_device_type})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_clear:
                etSearch.getText().clear();
                break;
            case R.id.ll_department:
                departmentPopupWindow.show();
                break;
            case R.id.ll_device_type:
                devicePopupWindow.show();
                break;
        }
    }

    private View getEmptyDataView() {
        View notDataView = getLayoutInflater().inflate(R.layout.layout_no_data, rvList, false);
        LinearLayout llNoData = notDataView.findViewById(R.id.ll_no_data);
        llNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wahId = -999;
//                ownerId = -999;
//                getDeptList();
                readCache();
            }
        });
        return notDataView;
    }

    @Override
    protected void initData() {
        DictItemBean dictItemBean = new DictItemBean();
        dictItemBean.setTitle(ConstantValue.all_device_type);
        dictItemBean.setText(ConstantValue.all_device_type);
        dictItemBean.setValue(ConstantValue.all_device_type_value);
        deviceTypeBeanList.add(dictItemBean);
        devicePopupWindow.setSelection(0);

        readCache();

//        getDictList();
    }

    private void refresh() {
//        pageNo = 1;
        getBaseDeviceList();
    }

    private void loadMore() {
//        pageNo++;
        getBaseDeviceList();
    }

    private void readCache() {
        baseDeviceBeanList.clear();
        BaseDeviceTableBean list = AppDatabase.getInstance(DeviceManageActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseDeviceBean> baseDeviceBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseDeviceBean>>() {
            }.getType());
            L.i("zzz1-baseDeviceBeans.list222--->" + baseDeviceBeans.size());


            if (baseDeviceBeans != null && baseDeviceBeans.size() > 0) {
                baseDeviceBeanList.addAll(baseDeviceBeans);

            } else {
                Toasty.error(DeviceManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

            }
        } else {
            Toasty.normal(DeviceManageActivity.this, "请先下载数据", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 获取装置类型列表
     */
    private void getDictList() {
        String code = "ldar_deviceType";
        //部门也是字典接口，传这个： 这样： sys_depart,depart_name,id
//        String code = "sys_depart";
        Subscribe.getDictList(code, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<DictItemBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<DictItemBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<DictItemBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                deviceTypeBeanList.addAll(dataList);
                                devicePopupWindow.setSelection(0);
                                deviceTypeValue = deviceTypeBeanList.get(0).getValue();
                                tvDeviceType.setText(StringUtils.nullStrToEmpty(deviceTypeBeanList.get(0).getText()));
                            } else {
                                Toasty.error(DeviceManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(DeviceManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(DeviceManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(DeviceManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(DeviceManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, DeviceManageActivity.this));
    }

    /**
     * 获取装置列表
     */
    private void getBaseDeviceList() {
        Subscribe.getBaseDeviceList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
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
                                Long rowId = AppDatabase.getInstance(DeviceManageActivity.this).baseDeviceTableDao().insert(baseDeviceTableBean);
                                if (rowId != null){
                                    Toasty.success(DeviceManageActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();
                                    readCache();
                                } else {
                                    Toasty.error(DeviceManageActivity.this, R.string.download_fail, Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                int row = AppDatabase.getInstance(DeviceManageActivity.this).baseDeviceTableDao().deleteAll();
//                                    L.i("zzz1-del-row-->" + row);
                                if (row >= 0){
                                    mList.clear();
                                    adapter.notifyDataSetChanged();
                                }
                                Toasty.error(DeviceManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(DeviceManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(DeviceManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(DeviceManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(DeviceManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, DeviceManageActivity.this));
    }

    /**
     * 获取装置列表
     */
    private void getDeviceList1() {

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.belongCompany, ""); // 	归属公司
        params.put(Constant.chemicalName, ""); // 化学品名称
        params.put(Constant.createBy, ""); // 	创建人
        params.put(Constant.createTime, ""); // 创建日期
        params.put(Constant.deviceCapacity, ""); // 	产能

        params.put(Constant.deviceCode, ""); // 	编号
        params.put(Constant.deviceName, StringUtils.nullStrToEmpty(etSearch.getText().toString().trim())); // 	名称
        params.put(Constant.deviceType, StringUtils.nullStrToEmpty(deviceTypeValue)); // 装置类型 value值
        params.put(Constant.id, ""); // 主键
        params.put(Constant.leakingDate, ""); // 	泄露提报邮箱

//        params.put(Constant.pageNo, "" + pageNo); // pageNo
//        params.put(Constant.pageSize, "" + pageSize); // pageSize
        params.put(Constant.sysOrgCode, ""); // 所属部门
        params.put(Constant.testSdate, ""); // 开始检测日期
        params.put(Constant.updateBy, ""); // 更新人

        params.put(Constant.updateTime, ""); // 更新日期
        params.put(Constant.useDate, ""); // 投产日期

        Subscribe.getDeviceList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<DeviceManageResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<DeviceManageResultBean>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            if (pageNo == 1) {
//                                mList.clear();
//                            }
                            mList.clear();
                            DeviceManageResultBean resultBean = baseBean.getResult();
                            if (resultBean != null) {
                                List<DeviceManageResultBean.Records> dataList = resultBean.getRecords();
                                if (dataList != null && dataList.size() > 0) {
                                    mList.addAll(dataList);
                                    for (int i = 0; i < mList.size(); i++) {
                                        DeviceTableBean deviceTableBean = new DeviceTableBean();
                                        deviceTableBean.id = mList.get(i).getId();
                                        deviceTableBean.content = GsonUtils.toJson(mList.get(i));
                                        deviceTableBeans.add(deviceTableBean);
                                    }
                                    AppDatabase.getInstance(DeviceManageActivity.this).deviceTableDao().insertAll(deviceTableBeans);
                                    srl.finishRefresh();
                                    srl.finishLoadMore();
                                } else {
                                    srl.finishRefresh();
                                    srl.finishLoadMoreWithNoMoreData();
                                }
                            } else {
                                srl.finishRefresh();
                                srl.finishLoadMoreWithNoMoreData();
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Toasty.error(DeviceManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                            srl.finishRefresh();
                            srl.finishLoadMore();
                        }
                    } else {
                        Toasty.error(DeviceManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                        srl.finishRefresh();
                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(DeviceManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                    srl.finishRefresh();
                    srl.finishLoadMore();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(DeviceManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
                srl.finishRefresh();
                srl.finishLoadMore();
            }
        }, DeviceManageActivity.this));
    }

}