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
import com.equipmentmanage.app.adapter.EquipmentAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseAreaBean;
import com.equipmentmanage.app.bean.BaseAreaTableBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseEquipmentBean;
import com.equipmentmanage.app.bean.BaseEquipmentTableBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DeviceManageBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.bean.EquipmentManageBean;
import com.equipmentmanage.app.bean.EquipmentManageResultBean;
import com.equipmentmanage.app.bean.EquipmentManageTableBean;
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
 * @Description: 设备管理
 * @Author: zzh
 * @CreateDate: 2021/8/12
 */
public class EquipmentManageActivity extends BaseActivity {
    public static void open(Context c) {
        Intent i = new Intent(c, EquipmentManageActivity.class);
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

    @BindView(R.id.ll_equipment)
    LinearLayout llEquipment; //设备

    @BindView(R.id.tv_equipment)
    TextView tvEquipment; //设备

    @BindView(R.id.ll_area)
    LinearLayout llArea; //区域

    @BindView(R.id.tv_area)
    TextView tvArea; //区域

    //选择部门
    private ListPopupWindow departmentPopupWindow;
    private String departmentName;
    private String departmentValue;
    private List<DepartmentBean> departmentBeanList = new ArrayList<>();
    private DepartmentAdapter departmentAdapter;

    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private EquipmentAdapter adapter;
    private List<EquipmentManageResultBean.Records> mList = new ArrayList<>();
    List<EquipmentManageTableBean> tableBeans = new ArrayList<>();
    private int pageNo = 1, pageSize = 10;

    private String department, deviceType;

    private List<BaseEquipmentBean> baseEquipmentBeanList = new ArrayList<>();

    @Override
    public void onAttachedToWindow() {
        setShowTitle(false);
        super.onAttachedToWindow();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_equipment_manage;
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


        DepartmentBean departmentBean = new DepartmentBean();
        departmentBean.setName("部门1");
        departmentBean.setValue("1");
        DepartmentBean departmentBean1 = new DepartmentBean();
        departmentBean1.setName("部门2");
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
        adapter = new EquipmentAdapter(baseEquipmentBeanList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BaseEquipmentBean bean = baseEquipmentBeanList.get(position);
                if (bean != null) {
                    EquipmentManageDetailActivity.open(EquipmentManageActivity.this, bean);
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
        departmentPopupWindow.setAnchorView(llEquipment);
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
                    tvEquipment.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }
                departmentPopupWindow.dismiss();
            }
        });
    }

    @OnClick({R.id.imb_clear, R.id.ll_equipment, R.id.ll_area})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_clear:
                etSearch.getText().clear();
                break;
            case R.id.ll_equipment:
                departmentPopupWindow.show();
//                List<EquipmentManageTableBean> list = AppDatabase.getInstance(EquipmentManageActivity.this).equipmentManageDao().getAll();
//                L.i("zzz1-getAll2->" + GsonUtils.toJson(list));
                break;
            case R.id.ll_area:
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
        readCache();
    }

    private void refresh() {
//        pageNo = 1;
        getBaseEquipmentList();
    }

    private void loadMore() {
//        pageNo++;
        getBaseEquipmentList();
    }

    private void readCache() {
        baseEquipmentBeanList.clear();
        BaseEquipmentTableBean list = AppDatabase.getInstance(EquipmentManageActivity.this).baseEquipmentTableDao().loadById("1");
        if (list != null) {
            List<BaseEquipmentBean> baseEquipmentBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseEquipmentBean>>() {
            }.getType());

            L.i("zzz1-baseEquipmentBeans.list222--->" + baseEquipmentBeans.size());

            if (baseEquipmentBeans != null && baseEquipmentBeans.size() > 0) {
                baseEquipmentBeanList.addAll(baseEquipmentBeans);
            } else {
                Toasty.error(EquipmentManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.normal(EquipmentManageActivity.this, "请先下载数据", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();

    }

    /**
     * 设备
     */
    private void getBaseEquipmentList() {
        Subscribe.getBaseEquipmentList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseEquipmentBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseEquipmentBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            deviceTypeBeanList.clear();
                            List<BaseEquipmentBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                L.i("zzz1-baseEquipmentBeans.list1111--->" + dataList.size());
                                BaseEquipmentTableBean baseEquipmentTableBean = new BaseEquipmentTableBean();
                                baseEquipmentTableBean.id = "1";
                                baseEquipmentTableBean.content = GsonUtils.toJson(dataList);
                                Long rowId = AppDatabase.getInstance(EquipmentManageActivity.this).baseEquipmentTableDao().insert(baseEquipmentTableBean);
                                if (rowId != null){
                                    Toasty.success(EquipmentManageActivity.this, R.string.download_success, Toast.LENGTH_SHORT, true).show();
                                    readCache();
                                } else {
                                    Toasty.error(EquipmentManageActivity.this, R.string.download_fail, Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                int row = AppDatabase.getInstance(EquipmentManageActivity.this).baseEquipmentTableDao().deleteAll();
//                                    L.i("zzz1-del-row-->" + row);
                                if (row >= 0){
                                    mList.clear();
                                    adapter.notifyDataSetChanged();
                                }
                                Toasty.error(EquipmentManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(EquipmentManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(EquipmentManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(EquipmentManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(EquipmentManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, EquipmentManageActivity.this));
    }

    /**
     * 获取设备列表
     */
    private void getEquipmentList() {

        Map<String, Object> params = new HashMap<>();
        params.put(Constant.areaId, ""); // 		所属区域
        params.put(Constant.belongCompany, ""); // 	归属公司
        params.put(Constant.createBy, ""); // 	创建人
        params.put(Constant.createTime, ""); // 创建日期
        params.put(Constant.deviceId, ""); // 	所属装置

        params.put(Constant.eftflag, ""); // 	有效状态
        params.put(Constant.equipmentCode, ""); // 	编号 value值
        params.put(Constant.equipmentName, StringUtils.nullStrToEmpty(etSearch.getText().toString().trim())); // 名称
        params.put(Constant.id, ""); // 主键

        params.put(Constant.pageNo, "" + pageNo); // pageNo
        params.put(Constant.pageSize, "" + pageSize); // pageSize
        params.put(Constant.sysOrgCode, ""); // 	所属部门
        params.put(Constant.updateBy, ""); // 更新人
        params.put(Constant.updateTime, ""); // 更新日期

        Subscribe.getEquipmentList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<EquipmentManageResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<EquipmentManageResultBean>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
                            if (pageNo == 1) {
                                mList.clear();
                            }
                            EquipmentManageResultBean resultBean = baseBean.getResult();
                            if (resultBean != null) {
                                List<EquipmentManageResultBean.Records> dataList = resultBean.getRecords();
                                if (dataList != null && dataList.size() > 0) {
                                    mList.addAll(dataList);
                                    for (int i = 0; i < mList.size(); i++) {
                                        EquipmentManageTableBean tableBean = new EquipmentManageTableBean();
                                        tableBean.id = mList.get(i).getId();
                                        tableBean.content = GsonUtils.toJson(mList.get(i));
                                        tableBeans.add(tableBean);
                                    }
                                    AppDatabase.getInstance(EquipmentManageActivity.this).equipmentManageDao().insertAll(tableBeans);

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
                            Toasty.error(EquipmentManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                            srl.finishRefresh();
                            srl.finishLoadMore();
                        }
                    } else {
                        Toasty.error(EquipmentManageActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                        srl.finishRefresh();
                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(EquipmentManageActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                    srl.finishRefresh();
                    srl.finishLoadMore();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(EquipmentManageActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
                srl.finishRefresh();
                srl.finishLoadMore();
            }
        }, EquipmentManageActivity.this));
    }

}