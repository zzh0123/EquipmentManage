package com.equipmentmanage.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.equipmentmanage.app.R;
import com.equipmentmanage.app.activity.ProductFlowActivity;
import com.equipmentmanage.app.activity.SealPointOnRecordActivity1;
import com.equipmentmanage.app.adapter.SelectDeviceAdapter;
import com.equipmentmanage.app.adapter.SelectMediumStateAdapter;
import com.equipmentmanage.app.bean.BaseDeviceBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.BaseMediumStateBean;
import com.equipmentmanage.app.bean.BaseMediumStateTableBean;
import com.equipmentmanage.app.bean.BaseStreamBean;
import com.equipmentmanage.app.bean.BaseStreamTableBean;
import com.equipmentmanage.app.dao.AppDatabase;
import com.equipmentmanage.app.netapi.Constant;
import com.equipmentmanage.app.netapi.ConstantValue;
import com.equipmentmanage.app.utils.L;
import com.equipmentmanage.app.utils.StringUtils;
import com.equipmentmanage.app.utils.gson.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * @Description: 新增产品流-dialog
 * @Author: zzh
 * @CreateDate: 2021/12/26
 */
public class AddProductDialog extends Dialog implements View.OnClickListener {

    private EditText et_code, et_name;
    private TextView tv_title, tvCancel, tvConfirm;

    private LinearLayout ll_device_type , ll_medium_state; //装置类型, 介质状态
    private TextView tv_device_type_name, tv_medium_state;

    private Context context;

    private OnConfirmListener listener;

    private ListPopupWindow devicetTypePopupWindow;
    private String devicetTypeName = "";
    private String deviceTypeValue = "";
    private SelectDeviceAdapter selectDeviceAdapter;
    private List<BaseDeviceBean> baseDeviceBeanList = new ArrayList<>();

    private ListPopupWindow mediumStatePopupWindow;
    private String mediumStateName = "";
    private String mediumStateValue = "";
    private SelectMediumStateAdapter selectMediumStateAdapter;
    private List<BaseMediumStateBean> mediumStateBeanList = new ArrayList<>();

    private List<BaseStreamBean> baseStreamBeanList = new ArrayList<>();

    public AddProductDialog(Context context) {
        this(context, R.style.tipDialog);
        this.context = context;
    }

    public AddProductDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_product);
        initView();
        initListener();
    }

    // 初始化view
    private void initView() {
        L.i("zzz1---->initView");
        setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER; // BOTTOM紧贴底部
        lp.alpha = 1;
        lp.dimAmount = 0.6f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        tv_title = findViewById(R.id.tv_title);
        et_code = findViewById(R.id.et_code);
        et_name = findViewById(R.id.et_name);

        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);

        ll_device_type = findViewById(R.id.ll_device_type);
        ll_medium_state = findViewById(R.id.ll_medium_state);
        tv_device_type_name = findViewById(R.id.tv_device_type_name);
        tv_medium_state = findViewById(R.id.tv_medium_state);

        readProductCache();
    }

    // 初始化监听
    private void initListener() {
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        ll_device_type.setOnClickListener(this);
        ll_medium_state.setOnClickListener(this);

        // 选择装置类型
        devicetTypePopupWindow = new ListPopupWindow(context);
        devicetTypePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        devicetTypePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        devicetTypePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        devicetTypePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        devicetTypePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        devicetTypePopupWindow.setAnchorView(ll_device_type);
        devicetTypePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(context, R.attr.ms_dropdown_offset));
        devicetTypePopupWindow.setListSelector(ResUtils.getDrawable(context, R.drawable.xui_config_list_item_selector));
        devicetTypePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(context, R.drawable.ms_drop_down_bg_radius));
        selectDeviceAdapter = new SelectDeviceAdapter(context, baseDeviceBeanList);
        devicetTypePopupWindow.setAdapter(selectDeviceAdapter);

        devicetTypePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseDeviceBean bean = baseDeviceBeanList.get(position);
                if (bean != null) {
                    deviceTypeValue = bean.getId();
                    devicetTypeName = bean.getDeviceName();
                    tv_device_type_name.setText(StringUtils.nullStrToEmpty(bean.getDeviceName()));
                }
                devicetTypePopupWindow.dismiss();
            }
        });


        // 选择介质状态
        mediumStatePopupWindow = new ListPopupWindow(context);
        mediumStatePopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mediumStatePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mediumStatePopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        mediumStatePopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mediumStatePopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mediumStatePopupWindow.setAnchorView(ll_medium_state);
        mediumStatePopupWindow.setVerticalOffset(ThemeUtils.resolveDimension(context, R.attr.ms_dropdown_offset));
        mediumStatePopupWindow.setListSelector(ResUtils.getDrawable(context, R.drawable.xui_config_list_item_selector));
        mediumStatePopupWindow.setBackgroundDrawable(ResUtils.getDrawable(context, R.drawable.ms_drop_down_bg_radius));
        selectMediumStateAdapter = new SelectMediumStateAdapter(context, mediumStateBeanList);
        mediumStatePopupWindow.setAdapter(selectMediumStateAdapter);

        mediumStatePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseMediumStateBean bean = mediumStateBeanList.get(position);
                if (bean != null) {
                    mediumStateValue = bean.getValue();
                    mediumStateName = bean.getText();
                    tv_medium_state.setText(StringUtils.nullStrToEmpty(bean.getText()));
                }
                mediumStatePopupWindow.dismiss();
            }
        });

    }

    public void setTitle(String title){
        if (!StringUtils.isNullOrEmpty(title)){
            tv_title.setText(title);
        }
    }

    public void clearView(){
        et_code.setText("");
        et_name.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm) {
            if (StringUtils.isNullOrEmpty(et_code.getText().toString().trim())){
                Toasty.normal(context, "编号不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validUse()){
                Toasty.normal(context, "编号已存在！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (StringUtils.isNullOrEmpty(et_name.getText().toString().trim())){
                Toasty.normal(context, "名称不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (StringUtils.isNullOrEmpty(deviceTypeValue)){
                Toasty.normal(context, "请先选择装置类型！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (StringUtils.isNullOrEmpty(mediumStateValue)){
                Toasty.normal(context, "请先选择介质状态！", Toast.LENGTH_SHORT).show();
                return;
            }

            BaseStreamBean baseStreamBean = new BaseStreamBean();
            baseStreamBean.setBelongCompany(ConstantValue.belongCompany1);
            baseStreamBean.setProdStreamCode(et_code.getText().toString().trim());
            baseStreamBean.setProdStreamName(et_name.getText().toString().trim());
            baseStreamBean.setDeviceId(deviceTypeValue);
            baseStreamBean.setDeviceName(devicetTypeName);
            baseStreamBean.setMediumState(mediumStateValue);
            baseStreamBean.setMediumStateName(mediumStateName);
            baseStreamBeanList.add(baseStreamBean);

            BaseStreamTableBean baseStreamTableBean = new BaseStreamTableBean();
            baseStreamTableBean.id = "1";
            baseStreamTableBean.content = GsonUtils.toJson(baseStreamBeanList);

            Long rowId = AppDatabase.getInstance(context).baseStreamTableDao().insert(baseStreamTableBean);
            if (rowId != null){
                Toasty.success(context, "新增成功！", Toast.LENGTH_SHORT, true).show();
            } else {
                Toasty.error(context, "新增失败！", Toast.LENGTH_SHORT, true).show();
            }

            if (null != listener) {

                listener.onConfirm(et_code.getText().toString().trim(), et_name.getText().toString().trim());
//                dismiss();
            }
            dismiss();

        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
        } else if (v.getId() == R.id.ll_device_type) {
            readDeviceCache();
        } else if (v.getId() == R.id.ll_medium_state) {
            readMediumStateCache();
        }
    }

    private boolean validUse(){
        boolean isCanUse = true;
        if (baseStreamBeanList != null && baseStreamBeanList.size() > 0) {
            for (int i = 0; i < baseStreamBeanList.size(); i++){
                if (et_code.getText().toString().trim().equals(baseStreamBeanList.get(i).getProdStreamCode())){
                    isCanUse = false;
                    break;
                }
            }
        }
        return isCanUse;
    }

    private void readDeviceCache() {
        // 装置
        BaseDeviceTableBean list = AppDatabase.getInstance(context).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseDeviceBean> baseDeviceBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseDeviceBean>>() {
            }.getType());
            L.i("zzz1-baseDeviceBeans.list222--->" + baseDeviceBeans.size());
            baseDeviceBeanList.clear();
            if (baseDeviceBeans != null && baseDeviceBeans.size() > 0) {
                baseDeviceBeanList.addAll(baseDeviceBeans);
                selectDeviceAdapter.notifyDataSetChanged();
                devicetTypePopupWindow.show();
            } else {
                Toasty.warning(context, "请先下载装置的基础数据", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(context, "请先下载装置的基础数据", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void readMediumStateCache() {
        // 装置
        BaseMediumStateTableBean list = AppDatabase.getInstance(context).baseMediumStateTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)
        if (list != null) {
            List<BaseMediumStateBean> baseMediumStateBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseMediumStateBean>>() {
            }.getType());
            L.i("zzz1-mediumStateBeans.list222--->" + baseMediumStateBeans.size());
            baseDeviceBeanList.clear();
            if (baseMediumStateBeans != null && baseMediumStateBeans.size() > 0) {
                mediumStateBeanList.addAll(baseMediumStateBeans);
                selectMediumStateAdapter.notifyDataSetChanged();
                mediumStatePopupWindow.show();
            } else {
                Toasty.warning(context, "请先下载介质状态的基础数据", Toast.LENGTH_SHORT, true).show();
            }

        } else {
            Toasty.warning(context, "请先下载介质状态的基础数据", Toast.LENGTH_SHORT, true).show();
        }
    }

    private void readProductCache() {
//        BaseDeviceTableBean list = AppDatabase.getInstance(AreaManageActivity.this).baseDeviceTableDao().loadById("1");
//                L.i("zzz1-baseDevice->" + GsonUtils.toJson(list3));
//        GsonUtils.fromJson(list.content, List<BaseEquipmentBean>)

        baseStreamBeanList.clear();
        BaseStreamTableBean list = AppDatabase.getInstance(context).baseStreamTableDao().loadById("1");
//                L.i("zzz1-Area->" + GsonUtils.toJson(list3));

        if (list != null) {
            List<BaseStreamBean> baseStreamBeans = GsonUtils.fromJson(list.content, new TypeToken<List<BaseStreamBean>>() {
            }.getType());

            L.i("zzz1-baseStreamBeans.list222--->" + baseStreamBeans.size());

            if (baseStreamBeans != null && baseStreamBeans.size() > 0) {
                baseStreamBeanList.addAll(baseStreamBeans);
            } else {
                Toasty.warning(context, R.string.return_empty, Toast.LENGTH_SHORT, true).show();
            }
        }

    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }


    public interface OnConfirmListener {

        void onConfirm(String code, String name); //确定

    }
}
