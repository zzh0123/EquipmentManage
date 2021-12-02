package com.equipmentmanage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.AreaManageResultBean;
import com.equipmentmanage.app.bean.BaseAreaBean;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 区域管理详情
 * @Author: zzh
 * @CreateDate: 2021/8/12
 */
public class AreaManageDetailActivity extends BaseActivity {

    public static void open(Context c, BaseAreaBean bean){
        Intent i = new Intent(c, AreaManageDetailActivity.class);
        i.putExtra(Constant.areaBean, bean);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.tv_area_name)
    TextView tvAreaName; //名称

    @BindView(R.id.tv_area_code)
    TextView tvAreaCode; //编号

    @BindView(R.id.tv_belong_device)
    TextView tvBelongDevice; //所属装置

    @BindView(R.id.tv_enabled)
    TextView tvEnabled; //是否可用

    @BindView(R.id.tv_order)
    TextView tvOrder; //顺序

    @BindView(R.id.tv_product_flow)
    TextView tvProductFlow; //产品流

    @BindView(R.id.tv_pid_drawing_no)
    TextView tvPidDrawingNo; //PID图号

    private String department, deviceType;

    private BaseAreaBean bean;

    @Override
    protected int initLayout() {
        return R.layout.activity_area_manage_detail;
    }

    @Override
    protected void initView() {

        bean = (BaseAreaBean) getIntent().getSerializableExtra(Constant.areaBean);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (bean != null) {
            tvAreaName.setText(StringUtils.nullStrToEmpty(bean.getAreaName())); //名称
            tvAreaCode.setText(StringUtils.nullStrToEmpty(bean.getAreaCode())); //编号
            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //所属装置
//            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
        }

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
////                                mList.addAll(dataList);
//                            } else {
//                                Toasty.error(AreaManageDetailActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
//
//                            }
////                            adapter.notifyDataSetChanged();
//                        } else {
//                            Toasty.error(AreaManageDetailActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
////                            srl.finishRefresh();
////                            srl.finishLoadMore();
//                        }
                    } else {
                        Toasty.error(AreaManageDetailActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
//                        srl.finishRefresh();
//                        srl.finishLoadMore();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(AreaManageDetailActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
//                    srl.finishRefresh();
//                    srl.finishLoadMore();

                }


            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toasty.error(AreaManageDetailActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
//                srl.finishRefresh();
//                srl.finishLoadMore();
            }
        }, AreaManageDetailActivity.this));
    }

}