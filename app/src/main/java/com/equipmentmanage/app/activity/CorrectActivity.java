package com.equipmentmanage.app.activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.adapter.DepartmentAdapter;
import com.equipmentmanage.app.adapter.SelectDeviceAdapter;
import com.equipmentmanage.app.adapter.SelectGasAdapter;
import com.equipmentmanage.app.base.BaseActivity;
import com.equipmentmanage.app.bean.BaseBean;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseGasBean;
import com.equipmentmanage.app.bean.CorrectCheckBean;
import com.equipmentmanage.app.bean.DepartmentBean;
import com.equipmentmanage.app.bean.GasTableBean;
import com.equipmentmanage.app.bean.LoginResultBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.netsubscribe.Subscribe;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultListener;
import com.equipmentmanage.app.utils.netutils.OnSuccessAndFaultSub;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 漂移校准/校准设备
 * @Author: zzh
 * @CreateDate: 2021/8/18
 */
public class CorrectActivity extends BaseActivity {

    public static void open(Context c, String type) {
        Intent i = new Intent(c, CorrectActivity.class);
        i.putExtra(ConstantValue.correct_type, type);
        c.startActivity(i);
    }

    @BindView(R.id.titleBar)
    TitleBar titleBar; //标题栏

    @BindView(R.id.cstl_standard_gas)
    ConstraintLayout cstlStandardGas; //标准气

    @BindView(R.id.tv_standard_gas)
    TextView tvStandardGas; //标准气

    @BindView(R.id.tv_std_gas_read_value)
    EditText etStdGasReadValue; //标准气读数

    @BindView(R.id.tv_ppm_threshold_value)
    EditText etPpmThresholdValue; //PPM阈值

    @BindView(R.id.et_correct_read_value)
    EditText etCorrectReadValue; //校准读数

    @BindView(R.id.et_response_time)
    EditText etResponseTime; //响应时间

    @BindView(R.id.cstl_if_pass)
    ConstraintLayout cstlIfPass; //是否通过

    @BindView(R.id.tv_if_pass)
    TextView tvIfPass; //是否通过

    @BindView(R.id.tv_submit)
    TextView tvSubmit; //提交

    private String correctType;

    //选择装置类型
    private ListPopupWindow gasTypePopupWindow;
    private String gasTypeName = "";
    private String gasypeValue = "";
    private SelectGasAdapter selectGasAdapter;
    private List<BaseGasBean> baseGasBeanList = new ArrayList<>();

    //选择是否
    private ListPopupWindow isPassTypePopupWindow;
    private String isPassTypeName = "";
    private String isPassTypeValue = "";
    private DepartmentAdapter departmentAdapter;
    private List<DepartmentBean> isPassBeanList = new ArrayList<>();

    private CorrectCheckBean correctCheckBean;

    @Override
    protected int initLayout() {
        return R.layout.activity_drift_correct;
    }

    @Override
    protected void initView() {

//        bean = (AreaManageResultBean.Records) getIntent().getSerializableExtra(Constant.areaBean);
        correctType = getIntent().getStringExtra(ConstantValue.correct_type);
        // 0 校准设备(日常校准), 1 漂移校准
        if (!StringUtils.isNullString(correctType) && correctType.equals(ConstantValue.correct_type_0)) {
            titleBar.setTitle(R.string.daily_correct);
        } else {
            titleBar.setTitle(R.string.drift_correct);
        }

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @SingleClick
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        if (bean != null) {
//            tvAreaName.setText(StringUtils.nullStrToEmpty(bean.getAreaName())); //名称
//            tvAreaCode.setText(StringUtils.nullStrToEmpty(bean.getAreaCode())); //编号
//            tvBelongDevice.setText(StringUtils.nullStrToEmpty(bean.getBelongDevice_dictText())); //所属装置
////            tvEnabled.setText(StringUtils.nullStrToEmpty(bean.getDeviceName())); //是否可用
//        }


        // 选择标准气
        gasTypePopupWindow = new ListPopupWindow(this);
        gasTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        gasTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        gasTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        gasTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        gasTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        gasTypePopupWindow.setAnchorView(cstlStandardGas);
        gasTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        gasTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        gasTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        selectGasAdapter = new SelectGasAdapter(this, baseGasBeanList);
        gasTypePopupWindow.setAdapter(selectGasAdapter);

        //选择是否
        isPassTypePopupWindow = new ListPopupWindow(this);
        isPassTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        isPassTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        isPassTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        isPassTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        isPassTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        isPassTypePopupWindow.setAnchorView(cstlIfPass);
        isPassTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(this, R.attr.ms_dropdown_offset));
        isPassTypePopupWindow.setListSelector(ResUtils.getDrawable(this, R.drawable.xui_config_list_item_selector));
        isPassTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(this, R.drawable.ms_drop_down_bg_radius));
        departmentAdapter = new DepartmentAdapter(this, isPassBeanList);
        isPassTypePopupWindow.setAdapter(departmentAdapter);

    }

    @Override
    protected void initEvent() {
        gasTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseGasBean bean = baseGasBeanList.get(position);
                if (bean != null) {
                    gasypeValue = bean.getId();
                    tvStandardGas.setText(StringUtils.nullStrToEmpty(bean.getGasName()));
                }
                gasTypePopupWindow.dismiss();
            }
        });

        isPassTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentBean bean = isPassBeanList.get(position);
                if (bean != null) {
                    isPassTypeValue = bean.getValue();
                    tvIfPass.setText(StringUtils.nullStrToEmpty(bean.getName()));
                }
                isPassTypePopupWindow.dismiss();
            }
        });
    }

    @Override
    protected void initData() {
        DepartmentBean departmentBean = new DepartmentBean();
        departmentBean.setValue(ConstantValue.isPass_type_0);
        departmentBean.setName(ConstantValue.isPass_type_0_name);

        DepartmentBean departmentBean1 = new DepartmentBean();
        departmentBean1.setValue(ConstantValue.isPass_type_1);
        departmentBean1.setName(ConstantValue.isPass_type_1_name);
        isPassBeanList.add(departmentBean);
        isPassBeanList.add(departmentBean1);
        departmentAdapter.notifyDataSetChanged();

//        refresh();
        correctCheckBean = new CorrectCheckBean();
    }

    @OnClick({R.id.cstl_standard_gas, R.id.cstl_if_pass, R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cstl_standard_gas: // 标准气
                getGasList();
                break;
            case R.id.cstl_if_pass: // 是否通过
                isPassTypePopupWindow.show();
                break;
            case R.id.tv_submit: // 提交

                if (StringUtils.isNullOrEmpty(tvStandardGas.getText().toString().trim())) {
                    Toasty.warning(CorrectActivity.this, "标准气为空！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(etStdGasReadValue.getText().toString().trim())) {
                    Toasty.warning(CorrectActivity.this, "标准气读数为空！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(etPpmThresholdValue.getText().toString().trim())) {
                    Toasty.warning(CorrectActivity.this, "PPM阈值为空！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (StringUtils.isNullOrEmpty(etCorrectReadValue.getText().toString().trim())) {
                    Toasty.warning(CorrectActivity.this, "校准读数为空！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (StringUtils.isNullOrEmpty(etResponseTime.getText().toString().trim())) {
                    Toasty.warning(CorrectActivity.this, "响应时间为空！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (StringUtils.isNullOrEmpty(isPassTypeValue)) {
                    Toasty.warning(CorrectActivity.this, "是否通过为空！", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                accessCorrecting();
                break;
        }
    }

    /**
     * 标准气查询
     */
    private void getGasList() {
        Subscribe.getGasList(ConstantValue.belongCompany1, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                try {
                    BaseBean<List<BaseGasBean>> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<List<BaseGasBean>>>() {
                    }.getType());

                    if (null != baseBean) {
                        if (baseBean.isSuccess()) {
                            baseGasBeanList.clear();
                            List<BaseGasBean> dataList = baseBean.getResult();
                            if (dataList != null && dataList.size() > 0) {
                                baseGasBeanList.addAll(dataList);
                                selectGasAdapter.notifyDataSetChanged();
                                gasTypePopupWindow.show();
//                                GasTableBean gasTableBean = new GasTableBean();
//                                gasTableBean.id = "1";
//                                gasTableBean.content = GsonUtils.toJson(dataList);
//                                AppDatabase.getInstance(CorrectActivity.this).baseGasTableDao().insert(gasTableBean);

                            } else {
                                Toasty.error(CorrectActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                            }

//                            deviceTypeAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(CorrectActivity.this, R.string.search_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(CorrectActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(CorrectActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();

                }

            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(CorrectActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();
            }
        }, CorrectActivity.this));
    }

    /**
     * 检测仪器校准
     */
    private void accessCorrecting() {
        CorrectCheckBean correctCheckBean = new CorrectCheckBean();
        List<CorrectCheckBean.LiveCorrectingGasList> liveCorrectingGasList = new ArrayList<>();
        CorrectCheckBean.LiveCorrectingGasList listBean = new CorrectCheckBean.LiveCorrectingGasList();
        listBean.setCalibGas(tvStandardGas.getText().toString().trim()); // 标准气 calibGas
        listBean.setGasAreading(Integer.parseInt(etStdGasReadValue.getText().toString().trim())); // calibReading 标准气读数
        listBean.setPpm(Integer.parseInt(etPpmThresholdValue.getText().toString().trim())); // ppm PPM阈值
        // 0 校准设备(日常校准), 1 漂移校准
        if (!StringUtils.isNullString(correctType) && correctType.equals(ConstantValue.correct_type_0)) {
            listBean.setGasBreading(Integer.parseInt(etCorrectReadValue.getText().toString().trim())); // gasBreading校准读数(日常)
            listBean.setResponseBtime(Integer.parseInt(etResponseTime.getText().toString().trim())); // responseBtime	响应时间(日常)
            listBean.setIsbpass(isPassTypeValue); // isbpass	是否通过(日常)
        } else {
            listBean.setGasAreading(Integer.parseInt(etCorrectReadValue.getText().toString().trim())); // gasAreading 校准读数(漂移)
            listBean.setResponseAtime(Integer.parseInt(etResponseTime.getText().toString().trim())); // responseAtime 响应时间(漂移)
            listBean.setIsapass(isPassTypeValue); // isapass 是否通过(漂移)
        }

        liveCorrectingGasList.add(listBean);
        correctCheckBean.setLiveCorrectingGasList(liveCorrectingGasList);

        Subscribe.accessCorrecting(correctCheckBean, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    BaseBean<String> baseBean = GsonUtils.fromJson(result, new TypeToken<BaseBean<String>>() {
                    }.getType());

                    if (baseBean != null) {
                        if (baseBean.isSuccess()) {
                            Toasty.success(CorrectActivity.this, R.string.submit_success, Toast.LENGTH_SHORT, true).show();
                            clearView();
                        } else {
                            Toasty.error(CorrectActivity.this, R.string.submit_fail, Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        Toasty.error(CorrectActivity.this, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(CorrectActivity.this, R.string.parse_fail, Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onFault(String errorMsg) {
                Toasty.error(CorrectActivity.this, R.string.request_fail, Toast.LENGTH_SHORT, true).show();

            }
        }, CorrectActivity.this));
    }

    private void clearView() {
        tvStandardGas.setText("");
        etStdGasReadValue.setText("");
        etPpmThresholdValue.setText("");
        etCorrectReadValue.setText("");
        etResponseTime.setText("");
        tvIfPass.setText("");
    }
}