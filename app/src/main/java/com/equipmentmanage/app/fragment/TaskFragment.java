package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.DeviceTypeAdapter;
import com.equipmentmanage.app.adapter.TaskAdapter;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DictItemBean;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.bean.TaskResultBean;
import com.equipmentmanage.app.db.utils.DaoUtilsStore;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 任务Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/13 17:23
 */
public class TaskFragment extends LazyFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_check_result_upload)
    TextView tvCheckResultUpload; //检测结果上传

    //选择装置类型
    private ListPopupWindow departmentPopupWindow;
    private String departmentName = "";
    private String departmentValue = "";
    private List<DepartmentBean> departmentBeanList = new ArrayList<>();
    private DepartmentAdapter departmentAdapter;

    //选择装置类型
    private ListPopupWindow devicePopupWindow;
    private String deviceTypeName = "";
    ;
    private String deviceTypeValue = "";
    ;
    private List<DictItemBean> deviceTypeBeanList = new ArrayList<>();
    private DeviceTypeAdapter deviceTypeAdapter;

    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private TaskAdapter adapter;
    private List<TaskBean> mList = new ArrayList<>();

    private int pageNo = 1, pageSize = 10;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static TaskFragment newInstance(String param1, String param2) {
//        TaskFragment fragment = new TaskFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView(View view) {
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
//                refresh();
                List<TaskBean> list = DaoUtilsStore.getInstance().getUserDaoUtils().queryAll();
                L.i("zzz1--->size--" + list.size());
                L.i("zzz1--->list0--" + list.get(0).toString());
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

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

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

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(manager);
        adapter = new TaskAdapter(mList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TaskBean bean = mList.get(position);
                if (bean != null) {
//                    DeviceManageDetailActivity.open(TaskListActivity.this, bean);
                }

            }
        });
        adapter.setEmptyView(getEmptyDataView());
        rvList.setAdapter(adapter);
    }

    private View getEmptyDataView() {
        View notDataView = getLayoutInflater().inflate(R.layout.layout_no_data1, rvList, false);
        LinearLayout llNoData = notDataView.findViewById(R.id.ll_no_data);
        llNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        return notDataView;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        refresh();
    }

    private void refresh() {
        pageNo = 1;
        getTaskList();
    }

    private void loadMore() {
        pageNo++;
        getTaskList();
    }

    @OnClick({R.id.tv_check_result_upload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_result_upload:
//                if (valid()){
//                    login();
//                }
                break;
        }
    }

    /**
     * 获取任务清单列表
     */
    private void getTaskList() {
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.areaId, ""); // 		区域
        params.put(Constant.belongCompany, ""); // 归属公司
        params.put(Constant.createBy, ""); // 	创建人
        params.put(Constant.createScale, ""); // 创建级别
        params.put(Constant.createTime, ""); // 	创建日期

        params.put(Constant.detectionEdate, ""); // 	结束日期
        params.put(Constant.detectionPeriod, ""); // 	检测期间
        params.put(Constant.detectionSdate, ""); // 开始日期
        params.put(Constant.detectionYear, ""); // 	检测年度
        params.put(Constant.deviceId, ""); // 	装置

        params.put(Constant.id, ""); // 主键
        params.put(Constant.inspectionType, ""); // 检测类型
        params.put(Constant.pageNo, "" + pageNo); // pageNo
        params.put(Constant.pageSize, "" + pageSize); // pageSize
        params.put(Constant.planType, ""); // 计划类型

        params.put(Constant.sysOrgCode, ""); // 所属部门
        params.put(Constant.taskEnd, ""); // 是否已结束
        params.put(Constant.taskName, ""); // 	任务名称
        params.put(Constant.taskNum, ""); // 任务编号
        params.put(Constant.updateBy, ""); // 更新人

        params.put(Constant.updateTime, ""); // 	更新日期

        Subscribe.getTaskList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<TaskResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<TaskResultBean>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
                            if (pageNo == 1) {
                                mList.clear();
                            }
                            TaskResultBean resultBean = baseBean.getResult();
                            if (resultBean != null) {
                                List<TaskBean> dataList = resultBean.getRecords();
                                if (dataList != null && dataList.size() > 0) {
                                    mList.addAll(dataList);
                                    // 插入sqlite
                                    DaoUtilsStore.getInstance().getUserDaoUtils().insertMultiple(mList);
                                    tvCheckResultUpload.setVisibility(View.VISIBLE);
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
                            Toasty.error(getActivity(), R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                            srl.finishRefresh();
                            srl.finishLoadMore();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                        srl.finishRefresh();
                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                    srl.finishRefresh();
                    srl.finishLoadMore();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();
                srl.finishRefresh();
                srl.finishLoadMore();
            }
        }, getActivity()));
    }


}