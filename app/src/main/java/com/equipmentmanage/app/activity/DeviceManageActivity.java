package com.equipmentmanage.app.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.DeviceAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.DeviceBean;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.GsonUtils1;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @Description: 装置管理
 * @Author: zzh
 * @CreateDate: 2021/8/10
 */
public class DeviceManageActivity extends BaseActivity {

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

    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private DeviceAdapter adapter;
    private List<DeviceBean> mList = new ArrayList<>();

    private int pageIndex = 1, pageSize = 10;

    @Override
    protected int initLayout() {
        return R.layout.activity_device_manage;
    }

    @Override
    protected void initView() {
        srl.setEnableRefresh(true);
        srl.setEnableLoadMore(true);
        srl.setEnableFooterFollowWhenNoMoreData(true);
//        srl.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//
//            }
//        });
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
        adapter = new DeviceAdapter(mList);
        adapter.setEmptyView(getEmptyDataView());
        rvList.setAdapter(adapter);
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
//        getUserList();
    }

    private void refresh() {
        pageIndex = 1;
        getUnPickList();
    }

    private void loadMore() {
        pageIndex++;
        getUnPickList();
    }

    /**
     * 获取装置列表
     */
    private void getDeviceList() {
        Subscribe.getUserList(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
//                Toast.makeText(DeviceManageActivity.this, "请求成功！", Toast.LENGTH_SHORT).show();
//                ResultInfo resultInfo = GsonUtils.fromJson(result,
//                        ResultInfo.class);
//                tvResult.setText(result);
                try {
//                    BaseBean<List<DeviceBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<DeviceBean>>>() {
//                    }.getType());

                    BaseBean<List<DeviceBean>> baseBeanNew = GsonUtils1.fromJson(result, new TypeToken<BaseBean<List<DeviceBean>>>() {
                    }.getType());

                }catch (Exception e) {
                    e.printStackTrace();
//                    ST.show(R.string.parse_failure);
                    srl.finishRefresh();
                    srl.finishLoadMore();

                }


            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(DeviceManageActivity.this, "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }, DeviceManageActivity.this));
    }

    private void getUnPickList() {
        Map<String, String> params = new HashMap<>();
        params.put(com.ahzjgy.library_interface.Constants.hdrNo, ""); // 单号或者是任务号，非必填
        params.put(com.ahzjgy.library_interface.Constants.ownerId, "" + ownerId);
        params.put(com.ahzjgy.library_interface.Constants.wahId, "" + wahId);
        // 货状态 0-未完成 1-待拣货 2-拣货中 3-已完成
        params.put(com.ahzjgy.library_interface.Constants.status, pickStatus);
        params.put(com.ahzjgy.library_interface.Constants.pageIndex, CommonUtils.gs(pageIndex));
        params.put(com.ahzjgy.library_interface.Constants.pageSize, CommonUtils.gs(pageSize));
        params.put(com.ahzjgy.library_interface.Constants.orderBy, ""); // 默认CREATEDATE
        if (pageIndex == 1) {
            list.clear();
        }
        XHttpUtils.get(getActivity(), NetworkUtil.getFullUrl(com.ahzjgy.library_interface.Constants.GET_PICK_LIST), params, 0, 0, new ICommonCallback<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    BaseBeanNew<List<UnPickBean>> baseBeanNew = GsonUtils1.fromJson(response, new TypeToken<BaseBeanNew<List<UnPickBean>>>() {
                    }.getType());
                    if (null != baseBeanNew) {
                        if (baseBeanNew.isSuccess()) {
                            List<UnPickBean> dataList = baseBeanNew.getData();
                            if (dataList != null && dataList.size() > 0) {
                                if (pageIndex == 1) {
                                    list.clear();
                                }
                                list.addAll(dataList);
                                srl.finishRefresh();
                                srl.finishLoadMore();
                            } else {
                                srl.finishRefresh();
                                srl.finishLoadMoreWithNoMoreData();
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            ST.show(R.string.search_fail);
                            srl.finishRefresh();
                            srl.finishLoadMore();
                        }
                    } else {
                        ST.show(R.string.return_empty);
                        srl.finishRefresh();
                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ST.show(R.string.parse_failure);
                    srl.finishRefresh();
                    srl.finishLoadMore();

                }
            }

            @Override
            public void onFailure(String msg) {
                ST.show(R.string.search_fail);
                srl.finishRefresh();
                srl.finishLoadMore();

            }
        });
    }
}