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
import com.equipmentmanage.app.adapter.AreaAdapter;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.DeviceAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.AreaManageBean;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.BaseAreaTableBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DeviceManageBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.DeviceTableBean;
import com.equipmentmanage.app.bean.EquipmentManageResultBean;
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
 * @Description: ????????????
 * @Author: zzh
 * @CreateDate: 2021/8/11
 */
public class AreaManageActivity extends BaseActivity {

    public static void open(Context c) {
        Intent i = new Intent(c, AreaManageActivity.class);
        c.startActivity(i);
    }


    @BindView(R.id.titleBar)
    TitleBar titleBar; //?????????

    @BindView(R.id.iv_search)
    ImageView ivSearch; //??????

    @BindView(R.id.et_search)
    EditText etSearch; //??????

    @BindView(R.id.imb_clear)
    ImageButton imbClear; //??????

    @BindView(R.id.ll_device_name)
    LinearLayout llDeviceName; //????????????

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName; //????????????

    //????????????
    private ListPopupWindow departmentPopupWindow;
    private String departmentName;
    private String departmentValue;
    private List<DepartmentBean> departmentBeanList = new ArrayList<>();
    private DepartmentAdapter departmentAdapter;

    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private AreaAdapter adapter;
    private List<AreaManageResultBean.Records> mList = new ArrayList<>();

    private int pageNo = 1, pageSize = 10;

    private String department, deviceType;

    private List<BaseAreaBean> baseAreaBeanList = new ArrayList<>();


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

//        List<DeviceTableBean> list = AppDatabase.getInstance(AreaManageActivity.this).deviceTableDao().getAll();
////        L.i("zzz1-getdevicelist->" + GsonUtils.toJson(list));
//        DeviceManageResultBean.Records records = GsonUtils.fromJson(list.get(0).content, DeviceManageResultBean.Records.class);
//        L.i("zzz1-getDeviceName->" + records.getDeviceName());

        DepartmentBean departmentBean = new DepartmentBean();
        departmentBean.setName("??????1");
        departmentBean.setValue("1");
        DepartmentBean departmentBean1 = new DepartmentBean();
        departmentBean1.setName("??????2");
        departmentBean1.setValue("2");
        departmentBeanList.add(departmentBean);
        departmentBeanList.add(departmentBean1);

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
        adapter = new AreaAdapter(baseAreaBeanList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BaseAreaBean bean = baseAreaBeanList.get(position);
                if (bean != null) {
                    AreaManageDetailActivity.open(AreaManageActivity.this, bean);
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
        departmentPopupWindow.setAnchorView(llDeviceName);
        departmentPopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        departmentPopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        departmentPopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        departmentAdapter = new DepartmentAdapter(this, departmentBeanList);
        departmentPopupWindow.setAdapter(departmentAdapter);

    }

    @Override
    protected void initEvent() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //???actionId == XX_SEND ?????? XX_DONE????????????
                //??????event.getKeyCode == ENTER ??? event.getAction == ACTION_DOWN????????????
                //??????????????????????????????event != null???????????????????????????????????????null???
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //????????????
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
                    tvDeviceName.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }
                departmentPopupWindow.dismiss();
            }
        });
    }

    @OnClick({R.id.imb_clear, R.id.ll_device_name})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_clear:
                etSearch.getText().clear();
                break;
            case R.id.ll_device_name:
                departmentPopupWindow.show();
                break;
        }
    }

    private View getEmptyDataView() {
        View notDataView = getLayoutInflater().inflate(R.layout.layout_no_data, rvList, false);
        LinearLayout llNoData = notDataView.findViewById(R.id.ll_no_data);
        llNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readCache();
            }
        });
        return notDataView;
    }

    @Override
    protected void initData() {
        readCache();
    }

    private void refresh() {
//        pageNo = 1;
        getBaseAreaList();
    }

    private void loadMore() {
//        pageNo++;
        getBaseAreaList();
    }

    private void readCache() {
//        BaseDeviceTableBean list = AppDatabase.getInstance(AreaManageActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)

        baseAreaBeanList.clear();
        BaseAreaTableBean list = AppDatabase.getInstance(AreaManageActivity.this).baseAreaTableDao().loadById("1");
//                L.i("zzz1-Area->" + GsonUtils.toJson(list3));
        if (list != null) {
            List<BaseAreaBean> baseAreaBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseAreaBean>>() {
            }.getType());

            L.i("zzz1-baseAreaBeans.list222--->" + baseAreaBeans.size());
            if (baseAreaBeans != null && baseAreaBeans.size() > 0) {
                baseAreaBeanList.addAll(baseAreaBeans);
            } else {
                Toasty.error(AreaManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

            }
        } else {
            Toasty.normal(AreaManageActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * ??????
     */
    private void getBaseAreaList() {
        Subscribe.getBaseAreaList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //??????
                try {
                    BaseBean<List<BaseAreaBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseAreaBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseAreaBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                BaseAreaTableBean baseAreaTableBean = new BaseAreaTableBean();
                                baseAreaTableBean.id = "1";
                                baseAreaTableBean.content = GsonUtils.toJson(dataList);
                                Long rowId = AppDatabase.getInstance(AreaManageActivity.this).baseAreaTableDao().insert(baseAreaTableBean);
                                if (rowId != null){
                                    Toasty.success(AreaManageActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();
                                    readCache();
                                } else {
                                    Toasty.error(AreaManageActivity.this, R.string.download_fail, Toast.LENGTH_SHORT, true).show();
                                }

                            } else {
                                int row = AppDatabase.getInstance(AreaManageActivity.this).baseAreaTableDao().deleteAll();
                                L.i("zzz1-area-row-->" + row);
                                if (row >= 0){
                                    mList.clear();
                                    adapter.notifyDataSetChanged();
                                }
                                Toasty.error(AreaManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(AreaManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(AreaManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(AreaManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(AreaManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, AreaManageActivity.this));
    }

    /**
     * ??????????????????
     */
    private void getAreaList() {

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.areaCode, ""); // 		??????
        params.put(Constant.areaName, StringUtils.nullStrToEmpty(etSearch.getText().toString().trim())); // 	??????
        params.put(Constant.belongCompany, ""); // 	????????????
        params.put(Constant.belongDevice, ""); // ????????????
        params.put(Constant.createBy, ""); // 		?????????

        params.put(Constant.createTime, ""); // 	????????????
        params.put(Constant.eftflag, ""); // 	????????????
        params.put(Constant.id, ""); // ??????
        params.put(Constant.pageNo, "" + pageNo); // pageNo
        params.put(Constant.pageSize, "" + pageSize); // pageSize

        params.put(Constant.sysOrgCode, ""); // 	????????????
        params.put(Constant.updateBy, ""); // ?????????
        params.put(Constant.updateTime, ""); // ????????????

        Subscribe.getAreaList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //??????
                try {
                    BaseBean<AreaManageResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<AreaManageResultBean>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
                            if (pageNo == 1) {
                                mList.clear();
                            }
                            AreaManageResultBean resultBean = baseBean.getResult();
                            if (resultBean != null) {
                                List<AreaManageResultBean.Records> dataList = resultBean.getRecords();
                                if (dataList != null && dataList.size() > 0) {
                                    mList.addAll(dataList);
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
                            Toasty.error(AreaManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                            srl.finishRefresh();
                            srl.finishLoadMore();
                        }
                    } else {
                        Toasty.error(AreaManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                        srl.finishRefresh();
                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(AreaManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                    srl.finishRefresh();
                    srl.finishLoadMore();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                //??????
                Toasty.error(AreaManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
                srl.finishRefresh();
                srl.finishLoadMore();
            }
        }, AreaManageActivity.this));
    }

}