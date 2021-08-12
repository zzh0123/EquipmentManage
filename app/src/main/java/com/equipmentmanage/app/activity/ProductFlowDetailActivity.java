package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.ChemicalDetailBean;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 产品流详情
 * @Author: zzh
 * @CreateDate: 2021/8/12
 */
public class ProductFlowDetailActivity extends BaseActivity {

    public static void open(Context c, String id){
        Intent i = new Intent(c, ProductFlowDetailActivity.class);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName; //名称

    @BindView(R.id.tv_device_code)
    TextView tvDeviceCode; //编号

    @BindView(R.id.tv_belong_device)
    TextView tvBelongDevice; //所属装置

    @BindView(R.id.tv_medium_status)
    TextView tvMediumStatus; //介质状态

    @BindView(R.id.tv_if_effective)
    TextView tvIfEffective; //是否有效

    @BindView(R.id.tv_order)
    TextView tvOrder; //顺序

    private String department, deviceType;

    @Override
    protected int initLayout() {
        return R.layout.activity_product_flow_detail;
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


    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
//        refresh();
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
                    BaseBean<List<ChemicalDetailBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<ChemicalDetailBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
//                            if (pageIndex == 1) {
//                                mList.clear();
//                            }
                            List<ChemicalDetailBean> dataList = baseBean.getData();
                            if (dataList != null && dataList.size() > 0) {
//                                mList.addAll(dataList);
                            } else {
                                Toasty.error(ProductFlowDetailActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                            }
//                            adapter.notifyDataSetChanged();
                        } else {
                            Toasty.error(ProductFlowDetailActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
//                            srl.finishRefresh();
//                            srl.finishLoadMore();
                        }
                    } else {
                        Toasty.error(ProductFlowDetailActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
//                        srl.finishRefresh();
//                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(ProductFlowDetailActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
//                    srl.finishRefresh();
//                    srl.finishLoadMore();

                }


            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(ProductFlowDetailActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
//                srl.finishRefresh();
//                srl.finishLoadMore();
            }
        }, ProductFlowDetailActivity.this), params);
    }

}