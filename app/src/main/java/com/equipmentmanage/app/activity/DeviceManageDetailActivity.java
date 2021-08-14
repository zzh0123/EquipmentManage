package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.ChemicalDetailAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.ChemicalDetailBean;
import com.equipmentmanage.app.bean.DeviceManageResultBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 装置管理详情
 * @Author: zzh
 * @CreateDate: 2021/8/11
 */
public class DeviceManageDetailActivity extends BaseActivity {

    public static void open(Context c, DeviceManageResultBean.Records bean){
        Intent i = new Intent(c, DeviceManageDetailActivity.class);
        i.putExtra(Constant.deviceBean, bean);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName; //名称

    @BindView(R.id.tv_device_code)
    TextView tvDeviceCode; //编号

    @BindView(R.id.tv_device_type)
    TextView tvDeviceType; //装置类型

    @BindView(R.id.tv_department)
    TextView tvDepartment; //部门

    @BindView(R.id.tv_capacity)
    TextView tvCapacity; //产能

    @BindView(R.id.tv_put_into_production_date)
    TextView tvPutIntoProductionDate; //投产日期

    @BindView(R.id.tv_test_start_date)
    TextView tvTestStartDate; //开始检测日期

    @BindView(R.id.tv_enabled)
    TextView tvEnabled; //是否可用

    @BindView(R.id.tv_order)
    TextView tvOrder; //顺序

    @BindView(R.id.tv_divulge_submit_mailbox)
    TextView tvMailbox; //泄露提报邮箱


    @BindView(R.id.rv_list)
    RecyclerView rvList; //化学品明细
    private ChemicalDetailAdapter adapter;
    private List<ChemicalDetailBean> mList = new ArrayList<>();

    private int pageIndex = 1, pageSize = 10;

    private String department, deviceType;

    private DeviceManageResultBean.Records bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_device_manage_detail;
    }

    @Override
    protected void initView() {

        bean = (DeviceManageResultBean.Records) getIntent().getSerializableExtra(Constant.deviceBean);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (bean != null){
            tvDeviceName.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //名称
            tvDeviceCode.setText(StringUtils.nullStrToEmpty(bean.getDeviceCode())); //编号
            tvDeviceType.setText(StringUtils.nullStrToEmpty(bean.getDeviceType_dictText())); //装置类型
//            tvDepartment.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //部门
            tvCapacity.setText(StringUtils.nullStrToEmpty(bean.getDeviceCapacity())); //产能

            tvPutIntoProductionDate.setText(StringUtils.nullStrToEmpty(bean.getUseDate())); //投产日期
            tvTestStartDate.setText(StringUtils.nullStrToEmpty(bean.getTestSdate())); //开始检测日期
//            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
            tvOrder.setText(StringUtils.nullStrToEmpty(bean.getUseDate())); //顺序
            tvMailbox.setText(StringUtils.nullStrToEmpty(bean.getLeakingDate())); //泄露提报邮箱
        }


        ChemicalDetailBean bean = new ChemicalDetailBean();
        bean.setName("111");
        ChemicalDetailBean bean1 = new ChemicalDetailBean();
        bean1.setName("2222");
        mList.add(bean);
        mList.add(bean1);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(manager);
        adapter = new ChemicalDetailAdapter(mList);
        adapter.setEmptyView(getEmptyDataView());
        rvList.setAdapter(adapter);

    }

    @Override
    protected void initEvent() {

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
//        refresh();
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
        Subscribe.getDeviceList(params, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<ChemicalDetailBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<ChemicalDetailBean>>>() {
                    }.getType());

                    if (null != baseBean) {
//                        if (baseBean.isSuccess()) {
////                            if (pageIndex == 1) {
////                                mList.clear();
////                            }
//                            List<ChemicalDetailBean> dataList = baseBean.getData();
//                            if (dataList != null && dataList.size() > 0) {
//                                mList.addAll(dataList);
//                            } else {
//                                Toasty.error(DeviceManageDetailActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
//
//                            }
//                            adapter.notifyDataSetChanged();
//                        } else {
//                            Toasty.error(DeviceManageDetailActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
////                            srl.finishRefresh();
////                            srl.finishLoadMore();
//                        }
                    } else {
                        Toasty.error(DeviceManageDetailActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
//                        srl.finishRefresh();
//                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(DeviceManageDetailActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
//                    srl.finishRefresh();
//                    srl.finishLoadMore();

                }


            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(DeviceManageDetailActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
//                srl.finishRefresh();
//                srl.finishLoadMore();
            }
        }, DeviceManageDetailActivity.this));
    }

}