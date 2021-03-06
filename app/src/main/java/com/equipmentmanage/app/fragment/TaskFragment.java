package com.equipmentmanage.app.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.AreaManageActivity;
import com.equipmentmanage.app.activity.TaskDetailActivity;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.DeviceTypeAdapter;
import com.equipmentmanage.app.adapter.TaskAdapter;
import com.equipmentmanage.app.base.LazyFragment;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BarcodeTypeBean;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseCompanyResultBean;
import com.equipmentmanage.app.bean.BaseCompanyTableBean;
import com.equipmentmanage.app.bean.BaseGasBean;
import com.equipmentmanage.app.bean.CorrectCheckBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.DictItemBean;
import com.equipmentmanage.app.bean.GasTableBean;
import com.equipmentmanage.app.bean.TaskRecordsTableBean;
import com.equipmentmanage.app.bean.TaskResultBean;
//import com.equipmentmanage.app.db.utils.DaoUtilsStore;
import com.equipmentmanage.app.bean.TaskTableBean;
import com.equipmentmanage.app.bean.WeatherParamsBean;
import com.equipmentmanage.app.bean.WeatherParamsTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.DateUtil;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.Util;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.equipmentmanage.app.view.CompanyPopup;
import com.equipmentmanage.app.view.TipDialog;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.mmkv.MMKV;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.popupwindow.easypopup.HorizontalGravity;
import com.xuexiang.xui.widget.popupwindow.easypopup.VerticalGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: ??????Fragment
 * @Author: zzh
 * @CreateDate: 2021/8/13 17:23
 */
public class TaskFragment extends LazyFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar; //?????????

    @BindView(R.id.tv_check_result_upload)
    TextView tvCheckResultUpload; //??????????????????

    //??????????????????
    private ListPopupWindow departmentPopupWindow;
    private String departmentName = "";
    private String departmentValue = "";
    private List<DepartmentBean> departmentBeanList = new ArrayList<>();
    private DepartmentAdapter departmentAdapter;

    //??????????????????
    private ListPopupWindow devicePopupWindow;
    private String deviceTypeName = "";

    private String deviceTypeValue = "";

    private List<DictItemBean> deviceTypeBeanList = new ArrayList<>();
    private DeviceTypeAdapter deviceTypeAdapter;

    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private TaskAdapter adapter;
    private List<TaskResultBean.Records> mList = new ArrayList<>();
    List<TaskTableBean> taskTableBeans = new ArrayList<>();

    private int pageNo = 1, pageSize = 10;

    private TipDialog tipDialog;

    private MMKV kv = MMKV.defaultMMKV();
    private String username;
    //    private String username = "20002";
    private TaskResultBean.Records bean;
    private String mId;
    // ????????????
    private List<BaseGasBean> baseGasBeanList = new ArrayList<>();
    private List<BaseGasBean> baseGasBeanDriftList = new ArrayList<>();

    private String currentDate;

    //    private String belongCompany;
    private String is_daily_checked, is_drift_checked,
            is_weather_checked, is_seal_point_checked;

    private CompanyPopup companyPopup;
    private List<BarcodeTypeBean> barcodeTypeBeanList = new ArrayList<>();
    private String belongCompany;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
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
        // ???????????????
        if (!EventBus.getDefault().isRegistered(this)) {//????????????
            EventBus.getDefault().register(this);
        }

        username = kv.getString(Constant.username, "");
        L.i("zzz1---username->" + username);
        currentDate = DateUtil.getCurentTime();
        L.i("zzz1---currentDate->" + currentDate);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.i("zzz1---setLeftClickListener->");
//                getBaseCompanyList();
//                readCompanyCache();
//                readCompanyCache();
                companyPopup.showAtAnchorView(titleBar, VerticalGravity.ABOVE, HorizontalGravity.CENTER);
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
//                refresh();

//                if (tipDialog == null) {
//                    tipDialog = new TipDialog(getActivity());
//                }
//                tipDialog.show();


                // ????????????-??????
//                TaskRecordsTableBean list = AppDatabase.getInstance(getActivity()).taskRecordsTableDao().loadById("1");
//                L.i("zzz1-taskRecords->" + GsonUtils.toJson(list));
////        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
//                if (list != null) {
//                    List<TaskResultBean.Records> taskRecords = GsonUtils.fromJson(list.content, new TypeToken<List<TaskResultBean.Records>>() {
//                    }.getType());
////                    L.i("zzz1-taskRecords.list uuu--->" + taskRecords.size());
//                    L.i("zzz1-taskRecords.list uuu--->" + taskRecords.get(0).getLiveTaskAppPicPageList().get(0).getLiveTaskAppAssignedPageList().size());
//                }

                // ????????????????????????
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

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(manager);
        adapter = new TaskAdapter(mList);
        adapter.addChildClickViewIds(R.id.tv_upload);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                int id = view.getId();
                bean = mList.get(position);
                mId = bean.getId();
                if (id == R.id.tv_upload) {
                    if (bean != null) {
                        if (isCanUpload()) {
                            readDailyCache();
                            readDriftCache();
                            readWeatherParamsCache();
                            readImgCheckCache();
                        }
                    }
                }
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TaskResultBean.Records bean = mList.get(position);
                if (bean != null) {
                    TaskDetailActivity.open(getActivity(), bean);
                }

            }
        });
        adapter.setEmptyView(getEmptyDataView());
        rvList.setAdapter(adapter);

        //
        tipDialog = new TipDialog(getActivity());
        tipDialog.setOnConfirmListener(new TipDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
//                List<TaskBean> list = DaoUtilsStore.getInstance().getUserDaoUtils().queryAll();
//                L.i("zzz1--->size--" + list.size());
//                L.i("zzz1--->list0--" + list.get(0).toString());
                getTaskList();
            }
        });


        // ????????????
        companyPopup = new CompanyPopup(getActivity());
        companyPopup.createPopup();
        companyPopup.setOnKeshiItemClick(new CompanyPopup.OnKeshiItemClick() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position, List<BarcodeTypeBean> list) {
                belongCompany = list.get(position).getType();
                titleBar.setLeftText(list.get(position).getName());
                ConstantValue.belongCompany1 = belongCompany;
                companyPopup.dismiss();
            }

        });
        companyPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                companyPopup.setEmpty();
            }
        });

    }

    private View getEmptyDataView() {
        View notDataView = getLayoutInflater().inflate(R.layout.layout_no_data1, rvList, false);
        LinearLayout llNoData = notDataView.findViewById(R.id.ll_no_data);
        llNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                refresh();
                readTaskListCache();
            }
        });
        return notDataView;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
//        refresh();

        readTaskListCache();
//        readCompanyCache();
    }

    private void refresh() {
//        pageNo = 1;
        getTaskList();
    }

    private void loadMore() {
//        pageNo++;
        getTaskList();
    }

    @OnClick({R.id.tv_check_result_upload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_result_upload:
                List<TaskTableBean> list = AppDatabase.getInstance(getActivity()).taskTableDao().getAll();
                L.i("zzz1-getAll->" + GsonUtils.toJson(list));
                break;
        }
    }

    /**
     * ????????????-??????
     */
    private void readCompanyCache() {
        barcodeTypeBeanList.clear();
        BaseCompanyTableBean list = AppDatabase.getInstance(getActivity()).baseCompanyTableDao().loadById("1");
        if (list != null) {
            List<BaseCompanyResultBean.Records> companyBeanList = GsonUtils.fromJson(list.content, new TypeToken<List<BaseCompanyResultBean.Records>>() {
            }.getType());

            L.i("zzz1-companyBeanList.size--->" + companyBeanList.size());

            if (companyBeanList != null && companyBeanList.size() > 0) {
                for (int i = 0; i < companyBeanList.size(); i++) {
                    BaseCompanyResultBean.Records bean = companyBeanList.get(i);
                    BarcodeTypeBean typeBean = new BarcodeTypeBean(bean.getSysOrgCode(),
                            bean.getCompanyName());
                    barcodeTypeBeanList.add(typeBean);
                }
                belongCompany = barcodeTypeBeanList.get(0).getType();
                titleBar.setLeftText(barcodeTypeBeanList.get(0).getName());
                ConstantValue.belongCompany1 = belongCompany;
            } else {
                Toasty.warning(getActivity(), "???????????????????????????", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(getActivity(), "???????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

        companyPopup.setData(barcodeTypeBeanList);

    }

    /**
     * ????????????????????????
     */
    private void getTaskList() {
        Map<String, Object> params = new HashMap<>();
        //????????????-??????
//        params.put(Constant.areaId, ""); // 		??????
//        params.put(Constant.belongCompany, ""); // ????????????
//        params.put(Constant.createBy, ""); // 	?????????
//        params.put(Constant.createScale, ""); // ????????????
//        params.put(Constant.createTime, ""); // 	????????????
//
//        params.put(Constant.detectionEdate, ""); // 	????????????
//        params.put(Constant.detectionPeriod, ""); // 	????????????
//        params.put(Constant.detectionSdate, ""); // ????????????
//        params.put(Constant.detectionYear, ""); // 	????????????
//        params.put(Constant.deviceId, ""); // 	??????
//
//        params.put(Constant.id, ""); // ??????
//        params.put(Constant.inspectionType, ""); // ????????????
//        params.put(Constant.pageNo, "" + pageNo); // pageNo
//        params.put(Constant.pageSize, "" + pageSize); // pageSize
//        params.put(Constant.planType, ""); // ????????????
//
//        params.put(Constant.sysOrgCode, ""); // ????????????
//        params.put(Constant.taskEnd, ""); // ???????????????
//        params.put(Constant.taskName, ""); // 	????????????
//        params.put(Constant.taskNum, ""); // ????????????
//        params.put(Constant.updateBy, ""); // ?????????
//
//        params.put(Constant.updateTime, ""); // 	????????????
        if (StringUtils.isNullOrEmpty(username)) {
            Toasty.warning(getActivity(), R.string.username_empty_tip, Toast.LENGTH_SHORT, false).show();
            return;
        }

        // ????????????
        String taskSdate;
        if (StringUtils.isNullOrEmpty(username)) {
            Toasty.warning(getActivity(), R.string.username_empty_tip, Toast.LENGTH_SHORT, false).show();
            return;
        }

        // ????????????
        params.put(Constant.detectionUser, username); // 	?????????????????????
//        currentDate= "2021-11-20";
        String currentDate = DateUtil.getCurentTime1(); // 2021-11-20
//        currentDate = "2021-11-27";
        params.put(Constant.taskSdate, currentDate); // 	???????????? 2021-10-13 2021-11-06

        Subscribe.getTaskList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //??????
                try {
//                    String address = Util.readAssert(getActivity(), "text1.txt");
//                    String address = Util.readAssert(getActivity(), "checktest2.txt");
                    BaseBean<TaskResultBean> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<TaskResultBean>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            if (pageNo == 1) {
//                                mList.clear();
//                            }
//                            mList.clear();
                            TaskResultBean resultBean = baseBean.getResult();
                            if (resultBean != null) {
                                List<TaskResultBean.Records> dataList = resultBean.getRecords();
                                if (dataList != null && dataList.size() > 0) {
//                                    mList.addAll(dataList);
                                    // ??????sqlite
//                                    DaoUtilsStore.getInstance().getUserDaoUtils().insertMultiple(mList);
                                    for (int i = 0; i < mList.size(); i++) {
                                        TaskTableBean taskTableBean = new TaskTableBean();
                                        taskTableBean.id = mList.get(i).getId();
                                        taskTableBean.content = GsonUtils.toJson(mList.get(i));
                                        taskTableBeans.add(taskTableBean);
                                    }

                                    TaskTableBean taskTableBean = new TaskTableBean();
                                    taskTableBean.id = "1" + username;
                                    taskTableBean.content = GsonUtils.toJson(dataList);

                                    Long rowId = AppDatabase.getInstance(getActivity()).taskTableDao().insert(taskTableBean);
                                    if (rowId != null) {
                                        Toasty.success(getActivity(), R.string.download_success, Toast.LENGTH_SHORT, true).show();
                                        readTaskListCache();
                                    } else {
                                        Toasty.error(getActivity(), R.string.download_fail, Toast.LENGTH_SHORT, true).show();
                                    }

                                    tvCheckResultUpload.setVisibility(View.GONE);
                                    srl.finishRefresh();
                                    srl.finishLoadMore();
                                } else {
                                    int row = AppDatabase.getInstance(getActivity()).taskTableDao().deleteAllByUser("1" + username);
//                                    L.i("zzz1-del-row-->" + row);
                                    if (row >= 0){
                                        mList.clear();
                                        adapter.notifyDataSetChanged();
                                    }
                                    Toasty.success(getActivity(), "?????????????????????", Toast.LENGTH_SHORT, true).show();
                                    srl.finishRefresh();
                                    srl.finishLoadMoreWithNoMoreData();
                                }
                            } else {
                                srl.finishRefresh();
                                srl.finishLoadMoreWithNoMoreData();
                            }


//                            adapter.notifyDataSetChanged();
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
                //??????
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();
                srl.finishRefresh();
                srl.finishLoadMore();
            }
        }, getActivity()));
    }

    /**
     * ????????????-??????
     */
    private void readTaskListCache() {
        mList.clear();
        TaskTableBean list = AppDatabase.getInstance(getActivity()).taskTableDao().loadById("1" + username);
        if (list != null) {
            List<TaskResultBean.Records> taskBeanList = GsonUtils.fromJson(list.content, new TypeToken<List<TaskResultBean.Records>>() {
            }.getType());

            L.i("zzz1-taskBeanList.size--->" + taskBeanList.size());

            if (taskBeanList != null && taskBeanList.size() > 0) {
                mList.addAll(taskBeanList);
            } else {
                Toasty.warning(getActivity(), "?????????????????????", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(getActivity(), "?????????????????????", Toast.LENGTH_SHORT, true).show();
        }
        adapter.notifyDataSetChanged();

    }

    private boolean isCanUpload() {
        boolean isCan = true;
        is_daily_checked = kv.getString(Constant.is_correct_checked + mId + "2", "");
        if (StringUtils.isNullOrEmpty(is_daily_checked) || (!is_daily_checked.equals(ConstantValue.correct_1))) {
            isCan = false;
            Toasty.warning(getActivity(), "?????????????????????", Toast.LENGTH_SHORT, true).show();
        }

        is_weather_checked = kv.getString(Constant.is_weather_checked + mId, "");
        if (StringUtils.isNullOrEmpty(is_weather_checked) || (!is_weather_checked.equals(ConstantValue.entered_1))) {
            isCan = false;
            Toasty.warning(getActivity(), "???????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

        is_drift_checked = kv.getString(Constant.is_correct_checked + mId + "3", "");
        if (StringUtils.isNullOrEmpty(is_drift_checked) || (!is_drift_checked.equals(ConstantValue.correct_1))) {
            isCan = false;
            Toasty.warning(getActivity(), "?????????????????????", Toast.LENGTH_SHORT, true).show();
        }

        is_seal_point_checked = kv.getString(Constant.is_seal_point_checked + mId, "");
        if (StringUtils.isNullOrEmpty(is_seal_point_checked) || (!is_seal_point_checked.equals(ConstantValue.seal_point_checked_1))) {
            isCan = false;
            Toasty.warning(getActivity(), "??????????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

        return isCan;
    }

    /**
     * ???????????????-??????
     */
    private void readDailyCache() {
        // ??????
        GasTableBean list = AppDatabase.getInstance(getActivity()).baseGasTableDao().loadById(mId + "2");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseGasBean> baseGasBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseGasBean>>() {
            }.getType());
            L.i("zzz1-baseGasBeans.list uuu--->" + baseGasBeans.size());

            baseGasBeanList.clear();
            if (baseGasBeans != null && baseGasBeans.size() > 0) {
                baseGasBeanList.addAll(baseGasBeans);

                accessCorrecting(ConstantValue.correct_type_0, baseGasBeanList);
            } else {
                Toasty.warning(getActivity(), "????????????????????????", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(getActivity(), "????????????????????????", Toast.LENGTH_SHORT, true).show();
        }


    }

    /**
     * ????????????-??????
     */
    private void readDriftCache() {
        // ??????
        GasTableBean list = AppDatabase.getInstance(getActivity()).baseGasTableDao().loadById(mId + "3");
//                L.i("zzz1-readDriftCache->" + GsonUtils.toJson(list));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseGasBean> baseGasBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseGasBean>>() {
            }.getType());
            L.i("zzz1-baseGasBeans.list uuu??????--->" + baseGasBeans.size());

            baseGasBeanDriftList.clear();
            if (baseGasBeans != null && baseGasBeans.size() > 0) {
                baseGasBeanDriftList.addAll(baseGasBeans);
                accessCorrecting1(ConstantValue.correct_type_1, baseGasBeanDriftList);
            } else {
                Toasty.warning(getActivity(), "????????????????????????", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(getActivity(), "????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

    }

    /**
     * ????????????-??????
     */
    private void readWeatherParamsCache() {
        // ??????
        WeatherParamsTableBean list = AppDatabase.getInstance(getActivity()).weatherParamsTableDao().loadById(mId);
//        L.i("zzz1-weatherParamsBean00->" + GsonUtils.toJson(list));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            WeatherParamsBean weatherParamsBean = GsonUtils.fromJson(list.content, new TypeToken<WeatherParamsBean>() {
            }.getType());
            L.i("zzz1-weatherParamsBean uuu--->" + weatherParamsBean.getTemperatures());

            if (weatherParamsBean != null) {
                accessWeather(weatherParamsBean);
            } else {
                Toasty.warning(getActivity(), "??????????????????????????????", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(getActivity(), "??????????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

    }

    /**
     * ????????????-??????
     */
    private void readImgCheckCache() {
        // ????????????
        TaskRecordsTableBean list = AppDatabase.getInstance(getActivity()).taskRecordsTableDao().loadById(mId);
//        L.i("zzz1-taskRecords->" + GsonUtils.toJson(list));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<TaskResultBean.Records> taskRecords = GsonUtils.fromJson(list.content, new TypeToken<List<TaskResultBean.Records>>() {
            }.getType());
            L.i("zzz1-taskRecords.list uuu--->" + taskRecords.size());

            if (taskRecords != null && taskRecords.size() > 0) {
                accessResultList(taskRecords);
            } else {
                Toasty.warning(getActivity(), "????????????????????????", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(getActivity(), "????????????????????????", Toast.LENGTH_SHORT, true).show();
        }

    }

    /**
     * ??????????????????
     */
    private void accessCorrecting(String correctType, List<BaseGasBean> baseGasBeans) {
        String instrumentTypeValue = kv.getString(mId + "2" + Constant.instrumentTypeValue, "");
        CorrectCheckBean correctCheckBean = new CorrectCheckBean();
        correctCheckBean.setBelongCompany(StringUtils.nullStrToEmpty(bean.getBelongCompany()));
        correctCheckBean.setTaskId(bean.getTaskId());
        correctCheckBean.setCorrectingDate(currentDate); // 2021-11-06 15:52:11
        correctCheckBean.setCorrectingDevice(instrumentTypeValue); // ??????????????????
        correctCheckBean.setCreateBy(username); // 20001
//        correctCheckBean.setCreateTime("2021-11-06 15:52:11");
        correctCheckBean.setDetectionPeople(username); // 20001
        List<CorrectCheckBean.LiveCorrectingGasList> liveCorrectingGasList = new ArrayList<>();
        for (int i = 0; i < baseGasBeans.size(); i++) {
            CorrectCheckBean.LiveCorrectingGasList listBean = new CorrectCheckBean.LiveCorrectingGasList();
            listBean.setCalibGas(baseGasBeans.get(i).getGasName()); // ????????? calibGas
            listBean.setCalibReading(Integer.parseInt(baseGasBeans.get(i).getCalibThreshold())); // calibReading ???????????????
            listBean.setPpm(Integer.parseInt(baseGasBeans.get(i).getPpm())); // ppm PPM??????
            // 0 ????????????(????????????), 1 ????????????
            if (!StringUtils.isNullString(correctType) && correctType.equals(ConstantValue.correct_type_0)) {
                listBean.setGasBreading(Integer.parseInt(baseGasBeans.get(i).getActualValue())); // gasBreading????????????(??????)
                listBean.setResponseBtime(Integer.parseInt(baseGasBeans.get(i).getResponseTime())); // responseBtime	????????????(??????)
                listBean.setIsbpass(ConstantValue.isPass_type_0); // isbpass	????????????(??????)
            } else {
                listBean.setGasAreading(Integer.parseInt(baseGasBeans.get(i).getActualValue())); // gasAreading ????????????(??????)
                listBean.setResponseAtime(Integer.parseInt(baseGasBeans.get(i).getResponseTime())); // responseAtime ????????????(??????)
                listBean.setIsapass(ConstantValue.isPass_type_1); // isapass ????????????(??????)
            }

            liveCorrectingGasList.add(listBean);
        }
        correctCheckBean.setLiveCorrectingGasList(liveCorrectingGasList);

        Subscribe.accessCorrecting(correctCheckBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
//                            Toasty.success(getActivity(), R.string.submit_success, Toast.LENGTH_SHORT, true).show();


                        } else {
                            Toasty.error(getActivity(), StringUtils.nullStrToEmpty(baseBean.getMessage()), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, getActivity()));
    }

    /**
     * ??????????????????
     */
    private void accessCorrecting1(String correctType, List<BaseGasBean> baseGasBeans) {
        String instrumentTypeValue = kv.getString(mId + "3" + Constant.instrumentTypeValue, "");
        CorrectCheckBean correctCheckBean = new CorrectCheckBean();
        correctCheckBean.setBelongCompany(ConstantValue.belongCompany1);
        correctCheckBean.setTaskId(bean.getTaskId());
        correctCheckBean.setCorrectingDate(currentDate);
        correctCheckBean.setCorrectingDevice(instrumentTypeValue); // ??????1111
        correctCheckBean.setCreateBy(username); // 20001
//        correctCheckBean.setCreateTime("2021-11-06 15:52:11");
        correctCheckBean.setDetectionPeople(username);  // 20001
        List<CorrectCheckBean.LiveCorrectingGasList> liveCorrectingGasList = new ArrayList<>();
        for (int i = 0; i < baseGasBeans.size(); i++) {
            CorrectCheckBean.LiveCorrectingGasList listBean = new CorrectCheckBean.LiveCorrectingGasList();
            listBean.setCalibGas(baseGasBeans.get(i).getGasName()); // ????????? calibGas
            listBean.setCalibReading(Integer.parseInt(baseGasBeans.get(i).getCalibThreshold())); // calibReading ???????????????
            listBean.setPpm(Integer.parseInt(baseGasBeans.get(i).getPpm())); // ppm PPM??????
            // 0 ????????????(????????????), 1 ????????????
            if (!StringUtils.isNullString(correctType) && correctType.equals(ConstantValue.correct_type_0)) {
                listBean.setGasBreading(Integer.parseInt(baseGasBeans.get(i).getActualValue())); // gasBreading????????????(??????)
                listBean.setResponseBtime(Integer.parseInt(baseGasBeans.get(i).getResponseTime())); // responseBtime	????????????(??????)
                listBean.setIsbpass(ConstantValue.isPass_type_0); // isbpass	????????????(??????)
            } else {
                listBean.setGasAreading(Integer.parseInt(baseGasBeans.get(i).getActualValue())); // gasAreading ????????????(??????)
                listBean.setResponseAtime(Integer.parseInt(baseGasBeans.get(i).getResponseTime())); // responseAtime ????????????(??????)
                listBean.setIsapass(ConstantValue.isPass_type_1); // isapass ????????????(??????)
            }

            liveCorrectingGasList.add(listBean);
        }
        correctCheckBean.setLiveCorrectingGasList(liveCorrectingGasList);

        Subscribe.accessCorrecting(correctCheckBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
//                            Toasty.success(getActivity(), R.string.submit_success, Toast.LENGTH_SHORT, true).show();

                        } else {
                            Toasty.error(getActivity(), StringUtils.nullStrToEmpty(baseBean.getMessage()), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, getActivity()));
    }

    /**
     * ????????????
     */
    private void accessWeather(WeatherParamsBean weatherParamsBean) {
        weatherParamsBean.setBelongCompany(StringUtils.nullStrToEmpty(weatherParamsBean.getBelongCompany()));
        weatherParamsBean.setTaskId(bean.getTaskId());
        weatherParamsBean.setCreateBy(username);
        weatherParamsBean.setWeatherDateStr(currentDate);
        weatherParamsBean.setDetectionPeople(username);

        Subscribe.accessWeather(weatherParamsBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
//                            Toasty.success(getActivity(), R.string.submit_success, Toast.LENGTH_SHORT, true).show();


                        } else {
                            Toasty.error(getActivity(), StringUtils.nullStrToEmpty(baseBean.getMessage()), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, getActivity()));
    }

    /**
     * ??????????????????
     */
    private void accessResultList(List<TaskResultBean.Records> records) {
        for (int i = 0; i < records.size(); i++) {
            TaskResultBean.Records record = records.get(i);
            record.setTaskId(bean.getTaskId());
            record.setDetectionUser(username);
            record.setBelongCompany(StringUtils.nullStrToEmpty(bean.getBelongCompany()));
        }
//        loginPostBean.captcha ="";
//        loginPostBean.checkKey ="";
//        loginPostBean.setUsername(etAccount.getText().toString().trim());
//        loginPostBean.setPassword(etPassword.getText().toString().trim());

//        kv.removeValuesForKeys(new String[]{Constant.token, Constant.userId, Constant.username,
//                Constant.realname, Constant.orgCode, Constant.orgCodeTxt, Constant.telephone,
//                Constant.post});

        Subscribe.accessResultList(records, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            Toasty.success(getActivity(), R.string.upload_success, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(getActivity(), StringUtils.nullStrToEmpty(baseBean.getMessage()), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(getActivity(), R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getActivity(), R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(getActivity(), R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, getActivity()));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //????????????
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(String event) {
//        Log.i("zzz1", "" + event);
//        //????????????????????????
        if (!StringUtils.isNullOrEmpty(event)) {
            if (ConstantValue.event_clear_cache.equals(event)) {
                mList.clear();
                adapter.notifyDataSetChanged();
            } else if (ConstantValue.event_belong_company.equals(event)) {
                readCompanyCache();
            }
        }
    }
}