package com.equipmentmanage.app.activity;

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
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DeviceManageBean;
import com.equipmentmanage.app.netapi.Constant;
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
 * @Description: 区域管理
 * @Author: zzh
 * @CreateDate: 2021/8/11
 */
public class AreaManageActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.iv_search)
    ImageView ivSearch; //搜索

    @BindView(R.id.et_search)
    EditText etSearch; //搜索

    @BindView(R.id.imb_clear)
    ImageButton imbClear; //清除

    @BindView(R.id.ll_device_name)
    LinearLayout llDeviceName; //全部装置

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName; //全部装置

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
    private AreaAdapter adapter;
    private List<AreaManageBean> mList = new ArrayList<>();

    private int pageIndex = 1, pageSize = 10;

    private String department, deviceType;

    @Override
    public void onAttachedToWindow() {
        setShowTitle(false);
        super.onAttachedToWindow();
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


        AreaManageBean bean = new AreaManageBean();
        bean.setAreaName("111");
        bean.setStatus("1");
        AreaManageBean bean1 = new AreaManageBean();
        bean1.setAreaName("2222");
        bean1.setStatus("2");
        mList.add(bean);
        mList.add(bean1);

        DepartmentBean departmentBean = new DepartmentBean();
        departmentBean.setName("部门1");
        departmentBean.setValue("1");
        DepartmentBean departmentBean1 = new DepartmentBean();
        departmentBean1.setName("部门2");
        departmentBean1.setValue("2");
        departmentBeanList.add(departmentBean);
        departmentBeanList.add(departmentBean1);

        srl.setEnableRefresh(true);
        srl.setEnableLoadMore(true);
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
        adapter = new AreaAdapter(mList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                AreaManageBean bean = mList.get(position);
                if (bean != null) {
                    String name = bean.getAreaName();
                    DeviceManageDetailActivity.open(AreaManageActivity.this, name);
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
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_NEXT
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    L.i("zzz1--->etSearch");
//                    search();
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
//                wahId = -999;
//                ownerId = -999;
//                getDeptList();
                refresh();
            }
        });
        return notDataView;
    }

    @Override
    protected void initData() {
        refresh();
    }

    private void refresh() {
        pageIndex = 1;
        getDeviceList();
    }

    private void loadMore() {
        pageIndex++;
        getDeviceList();
    }

    /**
     * 获取装置列表
     */
    private void getDeviceList() {
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.city, "北京"); // 部门
//        params.put(Constant.department, department); // 部门
//        params.put(Constant.deviceType, deviceType); // 装置类型
        Subscribe.getDeviceList(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<AreaManageBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<AreaManageBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
                            if (pageIndex == 1) {
                                mList.clear();
                            }
                            List<AreaManageBean> dataList = baseBean.getData();
                            if (dataList != null && dataList.size() > 0) {
                                mList.addAll(dataList);
                                srl.finishRefresh();
                                srl.finishLoadMore();
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
                //失败
                Toasty.error(AreaManageActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                srl.finishRefresh();
                srl.finishLoadMore();
            }
        }, AreaManageActivity.this), params);
    }

}